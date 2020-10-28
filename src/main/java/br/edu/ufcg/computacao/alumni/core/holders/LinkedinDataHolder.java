package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;
import br.edu.ufcg.computacao.alumni.core.parsers.AlumnusParser;
import br.edu.ufcg.computacao.alumni.core.util.ChecksumGenerator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LinkedinDataHolder extends Thread{
    private Logger LOGGER = Logger.getLogger(LinkedinDataHolder.class);

    private static LinkedinDataHolder instance;
    private long checksum;
    private Map<String, LinkedinAlumnusData> linkedinAlumniData;

    private LinkedinDataHolder() {
        this.checksum = 0;
    }

    private String readInput(String confFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(confFilePath)));
        return content;
    }

    public static LinkedinDataHolder getInstance() throws Exception {
        synchronized (LinkedinDataHolder.class) {
            if (instance == null) {
                instance = new LinkedinDataHolder();
            }
            return instance;
        }
    }

    public synchronized void loadLinkedinData(String linkedinURL, String filePath) throws IOException {
        // Download linkedin data
        try (BufferedInputStream in = new BufferedInputStream(new URL(linkedinURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
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

    public synchronized List<LinkedinNameProfilePair> getLinkedinNameProfilePairs(String token) throws Exception {
        Collection<LinkedinAlumnusData> linkedinAlumniData = this.linkedinAlumniData.values();
        List<LinkedinNameProfilePair> pairs = new ArrayList<>(linkedinAlumniData.size());

        Iterator<LinkedinAlumnusData> iterator = linkedinAlumniData.iterator();
        while(iterator.hasNext()) {
            LinkedinAlumnusData alumnus = iterator.next();
            pairs.add(new LinkedinNameProfilePair(alumnus.getFullName(), alumnus.getLinkedinProfile()));
        }
        return pairs;
    }

    public synchronized CurrentJob getAlumnusCurrentJob(String fullName, String linkedinId) throws Exception {
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


    public synchronized  Page<LinkedinNameProfilePair> getLinkedinAlumniDataPages(String token, int requiredPage) throws Exception {
        Pageable pageable= new PageRequest(requiredPage, 10);
        int start = (int) pageable.getOffset();

        int end = (int) ((start + pageable.getPageSize()) > this.linkedinAlumniData.size() ? linkedinAlumniData.size()
                : (start + pageable.getPageSize()));

        Page<LinkedinNameProfilePair> page
                = new PageImpl<LinkedinNameProfilePair>(getLinkedinNameProfilePairs(token).subList(start, end), pageable, this.linkedinAlumniData.size());
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
                this.loadLinkedinData(PropertiesHolder.getInstance().getProperty(
                        ConfigurationPropertyKeys.LINKEDIN_URL_KEY),
                        SystemConstants.DEFAULT_LINKEDIN_INPUT_FILE_PATH);
                Thread.sleep(Long.parseLong(Long.toString(TimeUnit.SECONDS.toMillis(30))));
            } catch (InterruptedException e) {
                isActive = false;
                LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
            } catch (IOException e) {
                LOGGER.error(Messages.COULD_NOT_LOAD_LINKEDIN_DATA, e);
            }
        }
    }
}
