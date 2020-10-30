package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.holders.MatchesHolder;
import br.edu.ufcg.computacao.alumni.core.holders.PropertiesHolder;
import br.edu.ufcg.computacao.alumni.core.models.SystemUser;
import br.edu.ufcg.computacao.alumni.core.util.AuthenticationUtil;
import org.apache.log4j.Logger;
import br.edu.ufcg.computacao.alumni.core.util.CryptoUtil;
import br.edu.ufcg.computacao.alumni.core.util.ServiceAsymmetricKeysHolder;
import java.security.GeneralSecurityException;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import org.springframework.data.domain.Page;

import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class ApplicationFacade {
    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);
    private RSAPublicKey asPublicKey;
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

    public String getPublicKey() throws Exception {
        ServiceAsymmetricKeysHolder service = ServiceAsymmetricKeysHolder.getInstance();
        service.setPublicKeyFilePath(PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ALUMNI_PUBLIC_KEY));

        try {
            return CryptoUtil.toBase64(service.getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException(e.getMessage());
        }
    }

    public Page<LinkedinNameProfilePair> getAlumniMatches(String token, int page) throws Exception {
        return LinkedinDataHolder.getInstance().getLinkedinAlumniDataPages(token, page);
    }

    public Collection<PendingMatch> getAlumniPendingMatches(String token) {

    }

    public void setMatch(String token, String registration, String linkedinId) {

    }

    public void unsetMatch(String token, String registration){

    }


}
