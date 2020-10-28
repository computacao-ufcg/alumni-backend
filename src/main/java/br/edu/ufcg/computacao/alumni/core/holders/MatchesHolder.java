package br.edu.ufcg.computacao.alumni.core.holders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;

public class MatchesHolder {
    private Logger LOGGER = Logger.getLogger(MatchesHolder.class);
    
    private Map<String, String> matches;
    private static MatchesHolder instance;
    private long lastModificationDate;

    private MatchesHolder() throws Exception {
        this.loadMatches(SystemConstants.DEFAULT_MATCHES_FILE_PATH);
    }

    public static MatchesHolder getInstance() throws Exception {
        synchronized (AlumniHolder.class) {
            if (instance == null) {
                instance = new MatchesHolder();
            }
            return instance;
        }
    }
    
    public synchronized void loadMatches(String filePath) throws IOException {
    	if (!dataHasChanged(filePath)) {
    		return;
    	}
    	
        this.matches = new HashMap<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String registration = data[0];
            String linkedinId = data[1];
            this.matches.put(registration, linkedinId);
            LOGGER.info(String.format(Messages.LOADING_MATCH_D_S_S, this.matches.size(), registration, linkedinId));
        }
        csvReader.close();
    }
    
    public synchronized void addMatch(String filePath, String registration, String linkedinId) throws IOException {
    	if (this.matches.containsKey(registration)) {
    		this.matches.replace(registration, linkedinId);
    	} else {
    		this.matches.put(registration, linkedinId);
    	}
    	
    	this.saveMatch(filePath, registration, linkedinId);
    	this.lastModificationDate = new Date().getTime();
    }
    
    public synchronized void saveMatch(String filePath, String registration, String linkedinId) throws IOException {
    	BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filePath, true));
    	
    	csvWriter.write(registration + "," + linkedinId);
    	
    	csvWriter.newLine();
    	csvWriter.close();
    }
    
    private boolean dataHasChanged(String filePath) throws IOException {
    	BasicFileAttributes attr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
    	long currentDate = attr.lastModifiedTime().toMillis();
    	if (currentDate > this.lastModificationDate) {
    		this.lastModificationDate = currentDate;
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public synchronized Map<String, String> getMatches() {
    	return this.matches;
    }

    public synchronized String getLinkedinId(String registration) {
        return this.matches.get(registration);
    }
}
