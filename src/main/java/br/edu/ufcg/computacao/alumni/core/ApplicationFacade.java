package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.models.AlumnusData;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.JobData;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFacade {
    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;

    private ApplicationFacade() {
    }

    public static ApplicationFacade getInstance() {
        synchronized (ApplicationFacade.class) {
            if (instance == null) {
                instance = new ApplicationFacade();
            }
            return instance;
        }
    }

    public void reload(String token, String filePath) throws Exception {
        AlumniHolder.getInstance().loadAlumni(filePath);
    }

    public AlumnusData[] getAlumni(String token) throws Exception {
        return AlumniHolder.getInstance().getAlumni();
    }

    public List<String> getAlumniNames(String token) throws Exception {
        AlumnusData[] alumni = AlumniHolder.getInstance().getAlumni();
        List<String> names = new ArrayList<>(alumni.length);

        for (int i = 0; i < alumni.length; i++) {
            names.add(alumni[i].getFullName());
        }
        return names;
    }

    public List<CurrentJob> getAlumniCurrentJob(String token) throws Exception {
        AlumnusData[] alumni = AlumniHolder.getInstance().getAlumni();
        List<CurrentJob> currentJobs = new ArrayList<>(alumni.length);

        for (int i = 0; i < alumni.length; i++) {
            CurrentJob currentJob = getAlumnusCurrentJob(alumni[i]);
            currentJobs.add(currentJob);
        }
        return currentJobs;
    }

    private CurrentJob getAlumnusCurrentJob(AlumnusData alumnus) {
        String name = alumnus.getFullName();
        String job = "";
        String startYear = "";
        JobData[] jobs = alumnus.getJobs();
        for (int i =0; i < jobs.length; i++) {
            DateRange dateRange = jobs[i].getDateRange();
            if (dateRange.isCurrent()) {
                job = jobs[i].getCompanyName();
                startYear = dateRange.getStartYear();
            }
        }
        return new CurrentJob(name, job, startYear);
    }
}
