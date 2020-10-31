package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MatchesHolder {
    private Logger LOGGER = Logger.getLogger(MatchesHolder.class);
    
    private Map<String, String> matches;
    private String matchesFilePath;
    private static MatchesHolder instance;

    private MatchesHolder() throws FatalErrorException {
        this.matchesFilePath = HomeDir.getPath() + PropertiesHolder.getInstance().
                getProperty(ConfigurationPropertyKeys.MATCHES_INPUT_KEY,
                        ConfigurationPropertyDefaults.DEFAULT_MATCHES_FILE_NAME);
        this.loadMatches(this.matchesFilePath);
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
                String[] data = row.split(",");
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
    	if (this.matches.containsKey(registration)) {
    		this.matches.replace(registration, linkedinId);
    	} else {
    		this.matches.put(registration, linkedinId);
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
    	BufferedWriter csvWriter = new BufferedWriter(new FileWriter(this.matchesFilePath));
    	// ToDo: write file
    	csvWriter.close();
    }

    public synchronized Map<String, String> getMatches() {
    	return this.matches;
    }

    public synchronized String getLinkedinId(String registration) {
        return this.matches.get(registration);
    }
}
