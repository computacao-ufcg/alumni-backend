package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.*;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.holders.*;
import br.edu.ufcg.computacao.alumni.core.models.AlumniOperation;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.alumni.core.plugins.AuthorizationPlugin;
import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.core.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.ConfigurationErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
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

    public Collection<UfcgAlumnusData> getAlumniData(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI);
        return AlumniHolder.getInstance().getAlumniData();
    }

    public List<String> getAlumniNames(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_NAMES);
        return AlumniHolder.getInstance().getAlumniNames();
    }

    public List<CurrentJob> getAlumniCurrentJob(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_CURRENT_JOB);
        return AlumniHolder.getInstance().getAlumniCurrentJob();
    }

    public Page<PendingMatch> getAlumniPendingMatches(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_PENDING_MATCHES);
        return PendingMatchesHolder.getInstance().getPendingMatchesPage(page);
    }

    public void setMatch(String token, String registration, String linkedinId) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.SET_MATCH);
        MatchesHolder.getInstance().addMatch(registration, linkedinId);
    }

    public void resetMatch(String token, String registration) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.RESET_MATCH);
        MatchesHolder.getInstance().deleteMatch(registration);
    }

    public Collection<LinkedinAlumnusData> getLinkedinAlumniData(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return LinkedinDataHolder.getInstance().getLinkedinAlumniData();
    }

    public Page<MatchResponse> getAlumniMatches(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_MATCHES);
        try {
            return MatchesHolder.getInstance().getMatchesPage(page);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<LinkedinNameProfilePair> getLinkedinNameProfilePairs(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_NAME_PROFILE_PAIRS);
        return LinkedinDataHolder.getInstance().getLinkedinNameProfilePairs(token);
    }

    public String getPublicKey() throws EurecaException {
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new ConfigurationErrorException(e.getMessage());
        }
    }

    public Page<EmployerResponse> getEmployers(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return EmployersHolder.getInstance().getEmployers(page);
    }

    public Page<EmployerResponse> getEmployersByType(String token, int page, EmployerType type) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return EmployersHolder.getInstance().getEmployers(type, page);
    }

    public Page<EmployerResponse> getEmployersUndefined(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return EmployersHolder.getInstance().getEmployersUndefined(page);
    }

    public void setEmployerTypeToUndefined(String token, String linkedinId) throws EurecaException{
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return EmployersHolder.getInstance().setEmployerTypeToUndefined(linkedinId);
    }

    public void setEmployerType(String token, EmployerType type, String linkedinId) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return EmployersHolder.getInstance().setEmployerType(type, linkedinId);
    }

    public StatisticsResponse getStatistics(String token, String level, String courseName) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return StatisticsHolder.getInstance().setEmployerType(level, courseName);
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

    public String getVersionNumber() {
        String buildNumber = null;
        buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                    ConfigurationPropertyDefaults.BUILD_NUMBER);
        return SystemConstants.API_VERSION_NUMBER + "-" + buildNumber;
    }
}
