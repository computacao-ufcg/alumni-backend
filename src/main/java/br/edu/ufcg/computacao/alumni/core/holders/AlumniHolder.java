package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlumniHolder extends Thread {
    private Logger LOGGER = Logger.getLogger(AlumniHolder.class);

    private static AlumniHolder instance;
    private long lastModificationDate;
    private UfcgAlumnusData[] alumni;

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

            CourseName courseName = null;
            Level level = null;
            switch (courseNameCode) {
                case CourseNameCode.DATA_PROCESSING:
                    courseName = CourseName.DATA_PROCESSING;
                    break;
                case CourseNameCode.COMPUTING_SCIENCE:
                    courseName = CourseName.COMPUTING_SCIENCE;
                    break;
                case CourseNameCode.INFORMATICS:
                    courseName = CourseName.INFORMATICS;
                    break;
                default:
                    LOGGER.error(String.format(Messages.INVALID_INPUT_S, row));
                    break;
            }
            switch (levelCode) {
                case LevelCode.UNDERGRADUATE:
                    level = Level.UNDERGRADUATE;
                    break;
                case LevelCode.MASTER:
                    level = Level.MASTER;
                    break;
                case LevelCode.DOCTORATE:
                    level = Level.DOCTORATE;
                    break;
                default:
                    LOGGER.error(String.format(Messages.INVALID_INPUT_S, row));
                    break;
            }
            Degree[] degrees = new Degree[1];
            degrees[0] = new Degree(courseName, level, admission, graduation);
            UfcgAlumnusData alumnus = new UfcgAlumnusData(registration, name, degrees);
            alumniList.add(alumnus);
        }
        csvReader.close();
        this.alumni = new UfcgAlumnusData[alumniList.size()];
        for(int i = 0; i < alumniList.size(); i++) {
            this.alumni[i] = alumniList.get(i);
            LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, this.alumni[i].getFullName()));
        }
    }

    public synchronized Collection<UfcgAlumnusData> getAlumniData() {
        Collection<UfcgAlumnusData> alumniCollection = new LinkedList<>();

        for(int i = 0; i < this.alumni.length; i++) {
            alumniCollection.add(this.alumni[i]);
        }
        return alumniCollection;
    }

    public synchronized Page<UfcgAlumnusData> getAlumniDataPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);

        List<UfcgAlumnusData> list = this.getAlumniDataList();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<UfcgAlumnusData> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }

    private synchronized List<UfcgAlumnusData> getAlumniDataList() {
        List<UfcgAlumnusData> alumniList = new ArrayList<>();
        for (UfcgAlumnusData alumnus : this.getAlumniData()) {
            alumniList.add(alumnus);
        }
        return alumniList;
    }

    public synchronized List<String> getAlumniNames() {
        List<String> alumniNames = new LinkedList<>();

        for(int i = 0; i < this.alumni.length; i++) {
            alumniNames.add(this.alumni[i].getFullName());
        }
        return alumniNames;
    }

    public List<CurrentJob> getAlumniCurrentJob() {
        List<CurrentJob> alumniCurrentJob = new LinkedList<>();

        for(int i = 0; i < this.alumni.length; i++) {
            String linkedinId = MatchesHolder.getInstance().getLinkedinId(this.alumni[i].getRegistration());
            CurrentJob current = LinkedinDataHolder.getInstance().getAlumnusCurrentJob(this.alumni[i].getFullName(),
                    linkedinId);
            if (!current.getCurrentJob().equals("bad match") && !current.getCurrentJob().equals("not available") &&
                                                                !current.getCurrentJob().equals("not matched")) {
                alumniCurrentJob.add(current);
            }
        }
        return alumniCurrentJob;
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
