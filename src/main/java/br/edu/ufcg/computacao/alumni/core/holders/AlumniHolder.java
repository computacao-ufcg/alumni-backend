package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.models.AlumnusData;
import br.edu.ufcg.computacao.alumni.core.parsers.AlumnusParser;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AlumniHolder {
    private Logger LOGGER = Logger.getLogger(AlumniHolder.class);

    private static AlumniHolder instance;
    private AlumnusData[] alumni;

    private AlumniHolder() throws Exception {
        this.loadAlumni(SystemConstants.DEFAUT_INPUT_FILE_PATH);
    }

    private String readInput(String confFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(confFilePath)));
        return content;
    }

    public static AlumniHolder getInstance() throws Exception {
        synchronized (AlumniHolder.class) {
            if (instance == null) {
                instance = new AlumniHolder();
            }
            return instance;
        }
    }

    public void loadAlumni(String filePath) throws IOException {
        String membersJsonString = readInput(filePath);
        JSONArray alumniJsonArray = new JSONArray(membersJsonString);
        LOGGER.debug(String.format(Messages.PARSING_LIST_WITH_D_ALUMNI, alumniJsonArray.length()));
        ArrayList<AlumnusData> alumniList = new ArrayList<AlumnusData>();
        for (int i = 0; i < alumniJsonArray.length(); i++) {
            try {
                JSONObject alumnusJson = alumniJsonArray.getJSONObject(i);
                AlumnusData alumnus = AlumnusParser.parse(alumnusJson.toString());
                alumniList.add(alumnus);
            } catch (JSONException e) {
                LOGGER.error(String.format(Messages.SKIPING_ENTRY_D, i), e);
            }
        }
        this.alumni = new AlumnusData[alumniList.size()];
        for(int i = 0; i < alumniList.size(); i++) {
            this.alumni[i] = alumniList.get(i);
            LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, this.alumni[i].getFullName()));
        }
    }

    public AlumnusData[] getAlumni() {
        return this.alumni;
    }
}
