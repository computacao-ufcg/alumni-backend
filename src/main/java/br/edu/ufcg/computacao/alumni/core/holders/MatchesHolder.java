package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.MatchResponse;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.alumni.core.models.PossibleMatch;
import br.edu.ufcg.computacao.alumni.core.util.PendingMatchNumberComparator;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MatchesHolder {
    private Logger LOGGER = Logger.getLogger(MatchesHolder.class);
    private static final String FIELD_SEPARATOR = ",";

    private Map<String, String> matches;
    private Collection<PendingMatch> pendingMatches;
    private String matchesFilePath;
    private static MatchesHolder instance;

    private MatchesHolder() throws FatalErrorException {
        this.matchesFilePath = HomeDir.getPath() + PropertiesHolder.getInstance().
                getProperty(ConfigurationPropertyKeys.MATCHES_INPUT_KEY,
                        ConfigurationPropertyDefaults.DEFAULT_MATCHES_FILE_NAME);
        this.loadMatches(this.matchesFilePath);
        this.pendingMatches = new HashSet<>();
    }

    public static MatchesHolder getInstance() {
        synchronized (AlumniHolder.class) {
            if (instance == null) {
                instance = new MatchesHolder();
            }
            return instance;
        }
    }
    
    public synchronized void loadMatches(String filePath) throws FatalErrorException {
        this.matches = new HashMap<>();
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(filePath));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(FIELD_SEPARATOR);
                String registration = data[0];
                String linkedinId = data[1];
                this.matches.put(registration, linkedinId);
                LOGGER.info(String.format(Messages.LOADING_MATCH_D_S_S, this.matches.size(), registration, linkedinId));
            }
            csvReader.close();
        } catch (IOException e) {
            throw new FatalErrorException(e.getMessage());
        }
    }
    
    public synchronized void addMatch(String registration, String linkedinId) {
        String formatedUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LINKEDIN_USER_BASE_URL_KEY) + linkedinId;

    	if (this.matches.containsKey(registration)) {
    		this.matches.replace(registration, formatedUrl);
    	} else {
    		this.matches.put(registration, formatedUrl);
    	}

        try {
            this.saveMatches();
        } catch (IOException e) {
            LOGGER.error(String.format(Messages.COULD_NOT_SAVE_MATCHES_S, this.matchesFilePath));
        }
    }

    public synchronized void deleteMatch(String registration) throws InvalidParameterException {
        if (this.matches.containsKey(registration)) {
            this.matches.remove(registration);
        } else {
            throw new InvalidParameterException(String.format(Messages.NO_SUCH_MATCH_S, registration));
        }

        try {
            this.saveMatches();
        } catch (IOException e) {
            LOGGER.error(String.format(Messages.COULD_NOT_SAVE_MATCHES_S, this.matchesFilePath));
        }
    }
    
    public synchronized void saveMatches() throws IOException {
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter(this.matchesFilePath, false));
        for (Map.Entry<String, String> entry : this.matches.entrySet()) {
            String registration = entry.getKey();
            String linkedinId = entry.getValue();
            csvWriter.write(registration + FIELD_SEPARATOR + linkedinId + System.lineSeparator());
        }
        csvWriter.close();
    }

    public synchronized Page<MatchResponse> getMatchesPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > this.matches.size() ?
                this.matches.size() : (start + pageable.getPageSize()));
        List<MatchResponse> list = getMatchesList();
        Page<MatchResponse> page = new PageImpl<MatchResponse>(list.subList(start, end), pageable, list.size());
        return page;
    }

    private synchronized List<MatchResponse> getMatchesList() {
        List<MatchResponse> matchesList = new ArrayList<MatchResponse>();
        for (String key : this.matches.keySet()) {
            String fullName = AlumniHolder.getInstance().getAlumnusName(key);
            matchesList.add(new MatchResponse(key, fullName, this.matches.get(key)));
        }
        return matchesList;
    }

    public synchronized MatchResponse getAlumnusMatches(String registration) {
        String linkedinId = this.matches.get(registration);
        if (linkedinId == null) return null;

        String fullName = AlumniHolder.getInstance().getAlumnusName(registration);
        return new MatchResponse(registration, fullName, linkedinId);
    }

    public synchronized Map<String, String> getMatches() {
    	return new HashMap<>(this.matches);
    }

    public synchronized String getLinkedinId(String registration) {
        return this.matches.get(registration);
    }

    public synchronized Page<PendingMatch> getPendingMatchesPage(int requiredPage, int minScore) {
        Pageable pageable= new PageRequest(requiredPage, 10);
        List<PendingMatch> list = getPendingMatches(minScore);

        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page<PendingMatch> page = new PageImpl<PendingMatch>(list.subList(start, end), pageable, list.size());
        return page;
    }

    private synchronized List<PendingMatch> getPendingMatches(int minScore) {
        if (minScore == 0) return this.getPendingMatches();

        List<PendingMatch> pendingMatches = this.getPendingMatches();
        pendingMatches.forEach(pendingMatch -> {
            Collection<PossibleMatch> possibleMatches = pendingMatch.getPossibleMatches();
            List<PossibleMatch> filteredPossibleMatches = possibleMatches
                    .stream()
                    .filter(possibleMatch -> possibleMatch.getScore() >= minScore)
                    .collect(Collectors.toList());

            pendingMatch.setPossibleMatches(filteredPossibleMatches);
        });

        return pendingMatches;
    }

    private synchronized List<PendingMatch> getPendingMatches() {
        return new LinkedList<>(this.pendingMatches);
    }

    public synchronized void setPendingMatches(Collection<PendingMatch> newPendingMatches) {
        this.pendingMatches = newPendingMatches;
    }
}
