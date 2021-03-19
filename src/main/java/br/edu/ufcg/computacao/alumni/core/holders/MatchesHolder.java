package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.*;
import br.edu.ufcg.computacao.alumni.core.util.ClassificationCalculator;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MatchesHolder {
    private Logger LOGGER = Logger.getLogger(MatchesHolder.class);
    private static final String FIELD_SEPARATOR = ",";

    private Map<String, MatchData> matches;
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

                UfcgAlumnusData alumnusData = AlumniHolder.getInstance().getAlumniMap().get(registration);
                String name = alumnusData.getFullName();
                String admission = alumnusData.getAdmission();
                String graduation = alumnusData.getGraduation();
                MatchData matchData = new MatchData(registration, name, linkedinId, admission, graduation);

                this.matches.put(registration, matchData);
                LOGGER.info(String.format(Messages.LOADING_MATCH_D_S_S, this.matches.size(), registration, linkedinId));
            }
            csvReader.close();
        } catch (IOException e) {
            throw new FatalErrorException(e.getMessage());
        }
    }

    private PendingMatch getPendingMatch(String registration) {
        for (PendingMatch pendingMatch : this.pendingMatches) {
            if (pendingMatch.getAlumnus().getRegistration().equals(registration))
                return pendingMatch;
        }
        return null;
    }
    
    public synchronized void addMatch(String registration, String linkedinId) throws InvalidParameterException {
        PendingMatch pendingMatch = this.getPendingMatch(registration);
        if (pendingMatch == null) {
            throw new InvalidParameterException(String.format(Messages.NO_SUCH_PENDING_MATCH_S, registration));
        }

        UfcgAlumnusData alumnusData = AlumniHolder.getInstance().getAlumniMap().get(registration);
        String name = alumnusData.getFullName();
        String admission = alumnusData.getAdmission();
        String graduation = alumnusData.getGraduation();
        MatchData matchData = new MatchData(registration, name, linkedinId, admission, graduation);

    	if (this.matches.containsKey(registration)) {
    		this.matches.replace(registration, matchData);
    	} else {
    		this.matches.put(registration, matchData);
    	}

    	this.pendingMatches.remove(pendingMatch);

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
        for (Map.Entry<String, MatchData> entry : this.matches.entrySet()) {
            String registration = entry.getKey();
            String linkedinId = entry.getValue().getLinkedinId();
            csvWriter.write(registration + FIELD_SEPARATOR + linkedinId + System.lineSeparator());
        }
        csvWriter.close();
    }

    public synchronized Page<MatchData> getMatchesPage(int requiredPage) {
        Pageable pageable= new PageRequest(requiredPage, 10);
        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), this.matches.size()));
        List<MatchData> list = getMatchesList();
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private synchronized List<MatchData> getMatchesList() {
        return new ArrayList<>(this.matches.values());
    }

    public synchronized MatchData getAlumnusMatches(String registration) {
        String linkedinId = this.getLinkedinId(registration);
        if (linkedinId == null) {
            return null;
        } else {
            return this.matches.get(registration);
        }
    }

    public synchronized Map<String, MatchData> getMatches() {
    	return new HashMap<>(this.matches);
    }

    public synchronized String getLinkedinId(String registration) {
        if (!this.matches.containsKey(registration)) return null;

        return this.matches.get(registration).getLinkedinId();
    }

    public synchronized Page<PendingMatch> getPendingMatchesPage(int requiredPage, MatchClassification matchClassification) {
        Pageable pageable= new PageRequest(requiredPage, 10);
        List<PendingMatch> list = getPendingMatches(matchClassification);

        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), list.size()));

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private synchronized void setMatchClassification(Collection<PossibleMatch> possibleMatches) {
        possibleMatches.forEach(possibleMatch -> {
            MatchClassification matchClassification = ClassificationCalculator.getInstance().getClassification(possibleMatch, this.getPendingMatches());
            possibleMatch.setMatchClassification(matchClassification);
        });
    }

    private synchronized List<PendingMatch> getPendingMatches(MatchClassification classification) {
        if (classification == null) {
            classification = MatchClassification.VERY_UNLIKELY;
        }

        List<PendingMatch> pendingMatches = this.getPendingMatches();
        MatchClassification finalClassification = classification;
        pendingMatches.forEach(pendingMatch -> {
            List<PossibleMatch> filteredPossibleMatches = pendingMatch
                    .getPossibleMatches()
                    .stream()
                    .filter(possibleMatch -> possibleMatch.getMatchClassification().getPriority() >= finalClassification.getPriority())
                    .collect(Collectors.toList());

            pendingMatch.setPossibleMatches(filteredPossibleMatches);
        });

        return pendingMatches
                .stream()
                .filter(item -> !item.getPossibleMatches().isEmpty())
                .collect(Collectors.toList());
    }

    private synchronized List<PendingMatch> getPendingMatches() {
        return new LinkedList<>(this.pendingMatches);
    }

    private synchronized void classificatePossibleMatches(Collection<PendingMatch> pendingMatches) {
        for (PendingMatch pendingMatch : pendingMatches) {
            this.setMatchClassification(pendingMatch.getPossibleMatches());
        }
    }

    public synchronized void setPendingMatches(Collection<PendingMatch> newPendingMatches) {
        this.pendingMatches = newPendingMatches;
        this.classificatePossibleMatches(this.pendingMatches);
    }

    public synchronized Page<MatchData> getMatchesSearchPage(int requiredPage, String name, String admission, String graduation) {
        Pageable pageable= new PageRequest(requiredPage, 10);
        List<MatchData> list = getMatchesSearch(name, admission, graduation);

        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), list.size()));

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private synchronized List<MatchData> getMatchesSearch(String name, String admission, String graduation) {
        List<MatchData> matches = getMatchesList();
        List<MatchData> search =  new ArrayList<>();

        Pattern namePattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        Pattern admissionPattern = Pattern.compile(admission, Pattern.CASE_INSENSITIVE);
        Pattern graduationPattern = Pattern.compile(graduation, Pattern.CASE_INSENSITIVE);

        for( MatchData match: matches) {
            Matcher nameMatcher = namePattern.matcher(match.getName());
            Matcher admissionMatcher = admissionPattern.matcher(match.getAdmission());
            Matcher graduationMatcher = graduationPattern.matcher(match.getGraduation());
            if(nameMatcher.find() || admissionMatcher.find() || graduationMatcher.find()) {
                search.add(match);
            }
        }
        return search;

    }
}
