package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
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

    public Collection<UfcgAlumnusData> getAlumniData(String token) throws Exception {
        return AlumniHolder.getInstance().getAlumniData(token);
    }

    public List<String> getAlumniNames(String token) throws Exception {
        return AlumniHolder.getInstance().getAlumniNames(token);
    }

    public List<CurrentJob> getAlumniCurrentJob(String token) throws Exception {
        return AlumniHolder.getInstance().getAlumniCurrentJob(token);
    }

    public Collection<LinkedinAlumnusData> getLinkedinAlumniData(String token) throws Exception {
        return LinkedinDataHolder.getInstance().getLinkedinAlumniData();
    }

    public List<LinkedinNameProfilePair> getLinkedinNameProfilePairs(String token) throws Exception {
        return LinkedinDataHolder.getInstance().getLinkedinNameProfilePairs(token);
    }


}
