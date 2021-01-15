package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.constants.*;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Degree;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AlumniHolder extends Thread {
    private Logger LOGGER = Logger.getLogger(AlumniHolder.class);

    private static AlumniHolder instance;
    private long lastModificationDate;
    private Map<String, UfcgAlumnusData> alumni;
    private AlumniHolder() {
        this.lastModificationDate = 0;
    }

    public static AlumniHolder getInstance() {
        synchronized (AlumniHolder.class) {
            if (instance == null) {
                instance = new AlumniHolder();
            }
            return instance;
        }
    }

    private synchronized CourseName getCourseName(String courseNameCode, String row) {
        switch (courseNameCode) {
            case CourseNameCode.DATA_PROCESSING:
                return CourseName.DATA_PROCESSING;
            case CourseNameCode.COMPUTING_SCIENCE:
                return CourseName.COMPUTING_SCIENCE;
            case CourseNameCode.INFORMATICS:
                return CourseName.INFORMATICS;
            default:
                LOGGER.error(String.format(Messages.INVALID_INPUT_S, row));
                return null;
        }
    } 

    private synchronized Level getLevel(String levelCode, String row) {
        switch (levelCode) {
            case LevelCode.UNDERGRADUATE:
                return Level.UNDERGRADUATE;
            case LevelCode.MASTER:
                return Level.MASTER;
            case LevelCode.DOCTORATE:
                return Level.DOCTORATE;
            default:
                LOGGER.error(String.format(Messages.INVALID_INPUT_S, row));
                return null;
            }
    }

    public synchronized void loadAlumni(String filePath) throws IOException {
        if (!dataHasChanged(filePath)) {
            return;
        }
        List<UfcgAlumnusData> alumniList = new LinkedList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String registration = data[0];
            String name = data[1];
            String courseNameCode = data[2];
            String levelCode = data[3];
            String admission = data[4];
            String graduation = data[5];

            CourseName courseName = getCourseName(courseNameCode, row);
            Level level = getLevel(levelCode, row);

            Degree[] degrees = new Degree[1];
            degrees[0] = new Degree(courseName, level, admission, graduation);
            UfcgAlumnusData alumnus = new UfcgAlumnusData(registration, name, degrees);
            alumniList.add(alumnus);
        }
        csvReader.close();
        this.alumni = new HashMap<>();
        for(int i = 0; i < alumniList.size(); i++) {
            UfcgAlumnusData alumnus = alumniList.get(i);
            this.alumni.put(alumnus.getRegistration(), alumnus);
            LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, alumnus.getFullName()));
        }
    }

    public synchronized Collection<UfcgAlumnusData> getAlumniData() {
        return this.alumni.values();
    }

    public synchronized Page<UfcgAlumnusData> getAlumniDataPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);

        List<UfcgAlumnusData> list = new ArrayList<>(this.getAlumniData());
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<UfcgAlumnusData> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }

    public synchronized List<String> getAlumniNames() {
        return alumni
                .values()
                .stream()
                .map(UfcgAlumnusData::getFullName)
                .collect(Collectors.toList());
    }

    public synchronized Page<String> getAlumniNamesPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);

        List<String> list = this.getAlumniNames();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<String> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }

    public List<CurrentJob> getAlumniCurrentJob() {
        List<CurrentJob> alumniCurrentJob = new LinkedList<>();

        for(UfcgAlumnusData alumnus : this.alumni.values()) {
            String linkedinId = MatchesHolder.getInstance().getLinkedinId(alumnus.getRegistration());
            CurrentJob current = LinkedinDataHolder.getInstance().getAlumnusCurrentJob(alumnus.getFullName(),
                    linkedinId);
            if (!current.getCurrentJob().equals("bad match") && !current.getCurrentJob().equals("not available") &&
                                                                !current.getCurrentJob().equals("not matched")) {
                alumniCurrentJob.add(current);
            }
        }
        return alumniCurrentJob;
    }
    public Page<CurrentJob> getAlumniCurrentJobPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);

        List<CurrentJob> list = this.getAlumniCurrentJob();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<CurrentJob> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }
    
    private boolean dataHasChanged(String filePath) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
        long currentDate = attr.lastModifiedTime().toMillis();
        if (currentDate > this.lastModificationDate) {
            this.lastModificationDate = currentDate;
            return true;
        } else {
            return false;
        }
    }

    public synchronized String getAlumnusName(String registration) {
        return this.alumni.get(registration).getFullName();
    }

    /**
     * From time to time, updates Linkedin data with data recovered by the external scraping engine
     */
    @Override
    public void run() {
        boolean isActive = true;

        while (isActive) {
            try {
                String filePath = HomeDir.getPath() + PropertiesHolder.getInstance().
                        getProperty(ConfigurationPropertyKeys.ALUMNI_INPUT_FILE_KEY,
                        ConfigurationPropertyDefaults.DEFAULT_ALUMNI_FILE_NAME);
                this.loadAlumni(filePath);
                Thread.sleep(Long.parseLong(Long.toString(TimeUnit.SECONDS.toMillis(30))));
            } catch (InterruptedException e) {
                isActive = false;
                LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
            } catch (IOException e) {
                LOGGER.error(Messages.COULD_NOT_LOAD_ALUMNI_DATA, e);
            }
        }
    }
}
