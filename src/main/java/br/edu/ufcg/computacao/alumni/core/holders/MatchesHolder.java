package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MatchesHolder {
    private Logger LOGGER = Logger.getLogger(MatchesHolder.class);
    private Map<String, String> matches;
    private static MatchesHolder instance;

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

    public synchronized String getLinkedinId(String registration) {
        return this.matches.get(registration);
    }
}
