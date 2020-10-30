package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.EurecaAsPublicKeyHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.AlumniOperation;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.alumni.core.plugins.AuthorizationPlugin;
import br.edu.ufcg.computacao.eureca.as.core.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import java.security.GeneralSecurityException;
import org.springframework.data.domain.Page;

import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class ApplicationFacade {
    private static final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);
    private RSAPublicKey asPublicKey;
    private AuthorizationPlugin authorizationPlugin;
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

    public void setAuthorizationPlugin(AuthorizationPlugin authorizationPlugin) {
        this.authorizationPlugin = authorizationPlugin;
    }

    public Collection<UfcgAlumnusData> getAlumniData(String token) throws Exception {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI);
        return AlumniHolder.getInstance().getAlumniData();
    }

    public List<String> getAlumniNames(String token) throws Exception {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_NAMES);
        return AlumniHolder.getInstance().getAlumniNames();
    }

    public List<CurrentJob> getAlumniCurrentJob(String token) throws Exception {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_CURRENT_JOB);
        return AlumniHolder.getInstance().getAlumniCurrentJob();
    }

    public Collection<LinkedinAlumnusData> getLinkedinAlumniData(String token) throws Exception {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return LinkedinDataHolder.getInstance().getLinkedinAlumniData();
    }

    public List<LinkedinNameProfilePair> getLinkedinNameProfilePairs(String token) throws Exception {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_NAME_PROFILE_PAIRS);
        return LinkedinDataHolder.getInstance().getLinkedinNameProfilePairs(token);
    }

    public String getPublicKey() throws Exception {
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException(e.getMessage());
        }
    }

    private RSAPublicKey getAsPublicKey() throws EurecaException {
        if (this.asPublicKey == null) {
            this.asPublicKey = EurecaAsPublicKeyHolder.getInstance().getAsPublicKey();
        }
        return this.asPublicKey;
    }

    private SystemUser authenticateAndAuthorize(String token, AlumniOperation operation) throws EurecaException {
        RSAPublicKey keyRSA = getAsPublicKey();
        SystemUser requester = AuthenticationUtil.authenticate(keyRSA, token);
        this.authorizationPlugin.isAuthorized(requester, operation);
        return requester;
    }

    public Page<LinkedinNameProfilePair> getAlumniMatches(String token, int page) throws Exception {
        return LinkedinDataHolder.getInstance().getLinkedinAlumniDataPages(token, page);
    }

    public Collection<PendingMatch> getAlumniPendingMatches(String token) {
        return null;
    }

    public void setMatch(String token, String registration, String linkedinId) {

    }

    public void unsetMatch(String token, String registration){

    }
}
