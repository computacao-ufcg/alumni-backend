package br.edu.ufcg.computacao.alumni.core.parsers;

import br.edu.ufcg.computacao.alumni.constants.JsonFields;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;
import br.edu.ufcg.computacao.alumni.core.util.FieldLoaderUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlumnusParser {
    private static Logger LOGGER = Logger.getLogger(AlumnusParser.class);

    public AlumnusParser() {
    }

    public static LinkedinAlumnusData parse(String alumnusJsonString) throws JSONException {
        JSONObject alumnusObj = new JSONObject(alumnusJsonString);
        JSONObject generalObj = alumnusObj.getJSONObject(JsonFields.GENERAL);
        String fullName = FieldLoaderUtil.load(generalObj, JsonFields.FULL_NAME);
        String company = FieldLoaderUtil.load(generalObj, JsonFields.CURRENT_COMPANY);
        String description = FieldLoaderUtil.load(generalObj, JsonFields.DESCRIPTION);
        String location = FieldLoaderUtil.load(generalObj, JsonFields.LOCATION);
        LinkedinJobData jobs[] = getJobs(alumnusObj);
        LinkedinSchoolData education[] = getEducation(alumnusObj);
        JSONObject detailsObj = alumnusObj.getJSONObject(JsonFields.DETAILS);
        String linkedinProfile = FieldLoaderUtil.load(detailsObj, JsonFields.LINKEDIN_PROFILE);
        String email = FieldLoaderUtil.load(detailsObj, JsonFields.EMAIL);
        String twitterId = FieldLoaderUtil.load(detailsObj, JsonFields.TWITTER_ID);
        LOGGER.debug(String.format(Messages.CREATING_ALUMNI_S, fullName));
        return new LinkedinAlumnusData(fullName, company, description, location, jobs, education, email, linkedinProfile, twitterId);
    }

    private static LinkedinSchoolData[] getEducation(JSONObject alumnusObj) {
        JSONArray schools;
        try {
            schools = alumnusObj.getJSONArray(JsonFields.EDUCATION);
        } catch (Exception e) {
            return new LinkedinSchoolData[0];
        }
        LinkedinSchoolData[] educationList = new LinkedinSchoolData[schools.length()];

        for (int i = 0; i < schools.length(); i++) {
            try {
                JSONObject schoolJson = schools.getJSONObject(i);
                LinkedinSchoolData school = EducationParser.parse(schoolJson.toString());
                educationList[i] = school;
            } catch (JSONException e) {
                LOGGER.error(Messages.SOMETHING_WENT_WRONG, e);
                break;
            }
        }
        return educationList;
    }

    private static LinkedinJobData[] getJobs(JSONObject alumnusObj) {
        JSONArray jobs;
        try {
            jobs = alumnusObj.getJSONArray(JsonFields.JOBS);
        } catch (Exception e) {
            return new LinkedinJobData[0];
        }
        LinkedinJobData[] jobsList = new LinkedinJobData[jobs.length()];

        for (int i = 0; i < jobs.length(); i++) {
            try {
                JSONObject jobJson = jobs.getJSONObject(i);
                LinkedinJobData job = JobParser.parse(jobJson.toString());
                jobsList[i] = job;
            } catch (JSONException e) {
                LOGGER.error(Messages.SOMETHING_WENT_WRONG, e);
                break;
            }
        }
        return jobsList;
    }
}
