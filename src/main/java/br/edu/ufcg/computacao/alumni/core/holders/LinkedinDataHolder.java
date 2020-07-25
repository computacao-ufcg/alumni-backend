package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.core.parsers.AlumnusParser;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LinkedinDataHolder {
    private Logger LOGGER = Logger.getLogger(LinkedinDataHolder.class);

    private static LinkedinDataHolder instance;
    private Map<String, LinkedinAlumnusData> linkedinAlumniData;

    private LinkedinDataHolder() throws Exception {
        this.loadLinkedinData(SystemConstants.DEFAULT_LINKEDIN_INPUT_FILE_PATH);
    }

    private String readInput(String confFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(confFilePath)));
        return content;
    }

    public static LinkedinDataHolder getInstance() throws Exception {
        synchronized (LinkedinDataHolder.class) {
            if (instance == null) {
                instance = new LinkedinDataHolder();
            }
            return instance;
        }
    }

    public void loadLinkedinData(String filePath) throws IOException {
        String membersJsonString = readInput(filePath);
        JSONArray alumniJsonArray = new JSONArray(membersJsonString);
        LOGGER.debug(String.format(Messages.PARSING_LIST_WITH_D_ALUMNI, alumniJsonArray.length()));
        this.linkedinAlumniData = new HashMap<>();
        for (int i = 0; i < alumniJsonArray.length(); i++) {
            try {
                JSONObject alumnusJson = alumniJsonArray.getJSONObject(i);
                LinkedinAlumnusData alumnus = AlumnusParser.parse(alumnusJson.toString());
                this.linkedinAlumniData.put(alumnus.getLinkedinProfile(), alumnus);
                LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, alumnus.getFullName()));
            } catch (JSONException e) {
                LOGGER.error(String.format(Messages.SKIPING_ENTRY_D, i), e);
            }
        }
    }

    public Map<String, LinkedinAlumnusData> getLinkedinAlumniData() {
        return this.linkedinAlumniData;
    }
}
