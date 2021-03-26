package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.*;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.holders.*;
import br.edu.ufcg.computacao.alumni.core.models.*;
import br.edu.ufcg.computacao.alumni.core.plugins.AuthorizationPlugin;
import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.core.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;

import java.security.interfaces.RSAPublicKey;
import java.util.Collection;

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

    public Page<UfcgAlumnusData> getAlumniData(String token, int page, String admission, String graduation) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI);
        return AlumniHolder.getInstance().getAlumniDataPage(page, admission, graduation);
    }

    public Page<String> getAlumniNames(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_NAMES);
        return AlumniHolder.getInstance().getAlumniNamesPage(page);
    }

    public Page<CurrentJob> getAlumniCurrentJob(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_CURRENT_JOB);
        return AlumniHolder.getInstance().getAlumniCurrentJobPage(page);
    }

    public Page<PendingMatch> getAlumniPendingMatches(String token, int page, MatchClassification matchClassification) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_PENDING_MATCHES);
        return MatchesHolder.getInstance().getPendingMatchesPage(page, matchClassification);
    }

    public void setMatch(String token, String registration, String linkedinId) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.SET_MATCH);
        MatchesHolder.getInstance().addMatch(registration, linkedinId);
    }

    public void resetMatch(String token, String registration) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.RESET_MATCH);
        MatchesHolder.getInstance().deleteMatch(registration);
    }

    public Page<LinkedinAlumnusData> getLinkedinAlumniData(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_ALUMNI_DATA);
        return LinkedinDataHolder.getInstance().getLinkedinAlumniDataPage(page);
    }

    public Page<MatchData> getAlumniMatches(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_MATCHES);
        try {
            return MatchesHolder.getInstance().getMatchesPage(page);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public MatchData getAlumnusMatches(String token, String registration) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_MATCHES);
        return MatchesHolder.getInstance().getAlumnusMatches(registration);
    }

    public Page<MatchData> getMatchesSearchPage(int page, String token, String name, String admission, String graduation) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_MATCHES_SEARCH);
        return MatchesHolder.getInstance().getMatchesSearchPage(page, name, admission, graduation);
    }

    public Page<LinkedinNameProfilePair> getLinkedinNameProfilePairs(String token, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_LINKEDIN_NAME_PROFILE_PAIRS);
        return LinkedinDataHolder.getInstance().getLinkedinNameProfilePairsPage(page);
    }

    public String getPublicKey() throws EurecaException {
        return ServiceAsymmetricKeysHolder.getInstance().getPublicKeyString();
    }

    public Page<ConsolidatedEmployer> getClassifiedEmployers(String token, EmployerType employerType, int page) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_EMPLOYERS);
        return EmployersHolder.getInstance().getClassifiedEmployersPage(employerType, page);
    }

    public Collection<UnknownEmployer> getUnknownEmployers(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_UNKNOWN_EMPLOYERS);
        return EmployersHolder.getInstance().getUnknownEmployers();
    }

    public void setUnknownEmployerUrl(String token, String currentLinkedinId, String newLinkedinId) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.SET_UNKNOWN_EMPLOYER_URL);
        EmployersHolder.getInstance().setUnknownEmployerUrl(currentLinkedinId, newLinkedinId);
    }

    public Page<ConsolidatedEmployer> getUnclassifiedEmployers(String token, int page) throws EurecaException {
//        authenticateAndAuthorize(token, AlumniOperation.GET_EMPLOYERS_UNDEFINED);
        return EmployersHolder.getInstance().getUnclassifiedEmployersPage(page);
    }

    public Collection<EmployerTypeResponse> getEmployerTypes(String token) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_EMPLOYER_TYPES);
        return EmployersHolder.getInstance().getEmployerTypes();
    }

    public void setEmployerTypeToUndefined(String token, String linkedinId) throws EurecaException{
        authenticateAndAuthorize(token, AlumniOperation.SET_EMPLOYER_TYPE_TO_UNDEFINED);
        EmployersHolder.getInstance().resetEmployerType(linkedinId);
    }

    public void setEmployerType(String token, String linkedinId, EmployerType employerType) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.SET_EMPLOYER_TYPE);
        EmployersHolder.getInstance().setEmployerType(linkedinId, employerType);
    }

    public StatisticsResponse getStatistics(String token, Level level, CourseName courseName) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_STATISTICS);
        StatisticsModel statisticsCourse = StatisticsHolder.getInstance().getStatistics(courseName);
        StatisticsModel statisticsLevel = StatisticsHolder.getInstance().getStatistics(level);
        return new StatisticsResponse(statisticsCourse, statisticsLevel);
    }

    public AlumniSiteStatisticsResponse getAlumniSiteStatistics(String token, CourseName courseName, Level level) throws EurecaException {
        authenticateAndAuthorize(token, AlumniOperation.GET_ALUMNI_SITE_STATISTICS);
        StatisticsModel statisticsCourse = StatisticsHolder.getInstance().getStatistics(courseName);
        StatisticsModel statisticsLevel = StatisticsHolder.getInstance().getStatistics(level);
        return new AlumniSiteStatisticsResponse(statisticsCourse, statisticsLevel);

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