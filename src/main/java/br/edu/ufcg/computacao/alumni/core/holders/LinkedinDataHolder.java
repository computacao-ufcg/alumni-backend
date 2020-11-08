package br.edu.ufcg.computacao.alumni.core.holders;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;
import br.edu.ufcg.computacao.alumni.core.parsers.AlumnusParser;
import br.edu.ufcg.computacao.alumni.core.util.ChecksumGenerator;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class LinkedinDataHolder extends Thread {
    private Logger LOGGER = Logger.getLogger(LinkedinDataHolder.class);

    private static LinkedinDataHolder instance;
    private long checksum;
    private Map<String, LinkedinAlumnusData> linkedinAlumniData;

    private LinkedinDataHolder() {
        this.checksum = 0;
        try {
            loadLinkedinData();
        } catch (IOException e) {
            this.linkedinAlumniData = new HashMap<>();
            LOGGER.error(Messages.COULD_NOT_LOAD_LINKEDIN_DATA);
        }
    }

    private String readInput(String confFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(confFilePath)));
        return content;
    }

    public static LinkedinDataHolder getInstance() {
        synchronized (LinkedinDataHolder.class) {
            if (instance == null) {
                instance = new LinkedinDataHolder();
            }
            return instance;
        }
    }

    public synchronized void loadLinkedinData() throws IOException {
    String linkedinURL = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LINKEDIN_SOURCE_URL_KEY);
    String filePath = HomeDir.getPath() + PropertiesHolder.getInstance().
            getProperty(ConfigurationPropertyKeys.LINKEDIN_INPUT_FILE_KEY,
                    ConfigurationPropertyDefaults.DEFAULT_LINKEDIN_INPUT_FILE_NAME);

        // Download linkedin data
        BufferedInputStream in = new BufferedInputStream(new URL(linkedinURL).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        // ingest linkedin data
        String membersJsonString = readInput(filePath);
        long newChecksum = ChecksumGenerator.getCRC32Checksum(membersJsonString.getBytes());
        if (newChecksum != this.checksum) {
            this.checksum = newChecksum;
            JSONArray alumniJsonArray = new JSONArray(membersJsonString);
            LOGGER.debug(String.format(Messages.PARSING_LIST_WITH_D_ALUMNI, alumniJsonArray.length()));
            this.linkedinAlumniData = new HashMap<>();
            for (int i = 0; i < alumniJsonArray.length(); i++) {
                try {
                    JSONObject alumnusJson = alumniJsonArray.getJSONObject(i);
                    LinkedinAlumnusData alumnus = AlumnusParser.parse(alumnusJson.toString());
                    this.linkedinAlumniData.put(alumnus.getLinkedinProfile(), alumnus);
                    LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, alumnus.getFullName()));
                } catch (JSONException e) {
                    LOGGER.error(String.format(Messages.SKIPING_ENTRY_D, i), e);
                }
            }
        }
    }

    public synchronized CurrentJob getAlumnusCurrentJob(String fullName, String linkedinId) {
        if (linkedinId == null) {
            return new CurrentJob(fullName, "not matched", "not matched");
        }
        LinkedinAlumnusData alumnus = this.linkedinAlumniData.get(linkedinId);
        if (alumnus == null) {
            return new CurrentJob(fullName, "bad match", "bad match");
        }
        String job = "not available";
        String startYear = "not available";
        LinkedinJobData[] jobs = alumnus.getJobs();
        for (int i =0; i < jobs.length; i++) {
            DateRange dateRange = jobs[i].getDateRange();
            if (dateRange.isCurrent()) {
                job = jobs[i].getCompanyName();
                startYear = dateRange.getStartYear();
            }
        }
        return new CurrentJob(fullName, job, startYear);
    }

    public synchronized Collection<LinkedinAlumnusData> getLinkedinAlumniData() {
        return this.linkedinAlumniData.values();
    }

    public synchronized Page<LinkedinAlumnusData> getLinkedinAlumniDataPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);

        List<LinkedinAlumnusData> list = this.getLinkedinAlumniDataList();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<LinkedinAlumnusData> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }

    private synchronized List<LinkedinAlumnusData> getLinkedinAlumniDataList() {
        List<LinkedinAlumnusData> linkedinList = new ArrayList<>();
        for (LinkedinAlumnusData alumnus : this.getLinkedinAlumniData()) {
            linkedinList.add(alumnus);
        }
        return linkedinList;
    }

    public synchronized List<LinkedinNameProfilePair> getLinkedinNameProfilePairs() {
        Collection<LinkedinAlumnusData> linkedinAlumniData = this.linkedinAlumniData.values();
        List<LinkedinNameProfilePair> pairs = new ArrayList<>(linkedinAlumniData.size());

        Iterator<LinkedinAlumnusData> iterator = linkedinAlumniData.iterator();
        while(iterator.hasNext()) {
            LinkedinAlumnusData alumnus = iterator.next();
            pairs.add(new LinkedinNameProfilePair(alumnus.getFullName(), alumnus.getLinkedinProfile()));
        }
        return pairs;
    }

    public synchronized Page<LinkedinNameProfilePair> getLinkedinNameProfilePairsPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);

        List<LinkedinNameProfilePair> list = this.getLinkedinNameProfilePairs();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<LinkedinNameProfilePair> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }
    
    /**
     * From time to time, updates Linkedin data with data recovered by the external scraping engine
     */
    @Override
    public void run() {
        boolean isActive = true;
        while (isActive) {
            try {
                loadLinkedinData();
                Thread.sleep(TimeUnit.MINUTES.toMillis(10));
            } catch (InterruptedException e) {
                isActive = false;
                LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
            } catch (IOException e) {
                LOGGER.error(Messages.COULD_NOT_REFRESH_LINKEDIN_DATA);
            }
        }
    }
}
