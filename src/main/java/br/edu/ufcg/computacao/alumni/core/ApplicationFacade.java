package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.*;
import org.apache.log4j.Logger;

import java.util.*;

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

    public void reloadUfcgData(String token, String filePath) throws Exception {
        AlumniHolder.getInstance().loadAlumni(filePath);
    }

    public Collection<UfcgAlumnusData> getAlumniData(String token) throws Exception {
        UfcgAlumnusData[] alumni = AlumniHolder.getInstance().getAlumni();
        Collection<UfcgAlumnusData> alumniCollection = new LinkedList<>();

        for(int i = 0; i < alumni.length; i++) {
            alumniCollection.add(alumni[i]);
        }
        return alumniCollection;
    }

    public List<String> getAlumniNames(String token) throws Exception {
        UfcgAlumnusData[] alumni = AlumniHolder.getInstance().getAlumni();
        List<String> alumniNames = new LinkedList<>();

        for(int i = 0; i < alumni.length; i++) {
            alumniNames.add(alumni[i].getFullName());
        }
        return alumniNames;
    }

    public List<CurrentJob> getAlumniCurrentJob(String token) throws Exception {
        UfcgAlumnusData[] alumni = AlumniHolder.getInstance().getAlumni();
        List<CurrentJob> alumniCurrentJob = new LinkedList<>();

        for(int i = 0; i < alumni.length; i++) {
            alumniCurrentJob.add(getAlumnusCurrentJob(alumni[i].getFullName(), alumni[i].getLinkedinId()));
        }
        return alumniCurrentJob;
    }

    public void reloadLinkedinData(String token, String filePath) throws Exception {
        LinkedinDataHolder.getInstance().loadLinkedinData(filePath);
    }

    public Collection<LinkedinAlumnusData> getLinkedinAlumniData(String token) throws Exception {
        return LinkedinDataHolder.getInstance().getLinkedinAlumniData().values();
    }

    public List<LinkedinNameProfilePair> getLinkedinNameProfilePairs(String token) throws Exception {
        Collection<LinkedinAlumnusData> linkedinAlumniData = LinkedinDataHolder.getInstance().getLinkedinAlumniData().values();
        List<LinkedinNameProfilePair> pairs = new ArrayList<>(linkedinAlumniData.size());

        Iterator<LinkedinAlumnusData> iterator = linkedinAlumniData.iterator();
        while(iterator.hasNext()) {
            LinkedinAlumnusData alumnus = iterator.next();
            pairs.add(new LinkedinNameProfilePair(alumnus.getFullName(), alumnus.getLinkedinProfile()));
        }
        return pairs;
    }

    private CurrentJob getAlumnusCurrentJob(String fullName, String linkedinId) throws Exception {
        LinkedinAlumnusData alumnus = LinkedinDataHolder.getInstance().getLinkedinAlumniData().get(linkedinId);
        if (alumnus == null) {
            return new CurrentJob(fullName, "not available", "not available");
        }
        String job = "";
        String startYear = "";
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
}
