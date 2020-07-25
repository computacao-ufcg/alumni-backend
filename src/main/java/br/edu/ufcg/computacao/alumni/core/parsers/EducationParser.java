package br.edu.ufcg.computacao.alumni.core.parsers;

import br.edu.ufcg.computacao.alumni.constants.JsonFields;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;
import br.edu.ufcg.computacao.alumni.core.util.FieldLoaderUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class EducationParser {
    private static Logger LOGGER = Logger.getLogger(EducationParser.class);

    public EducationParser() {
    }

    public static LinkedinSchoolData parse(String jobJsonString) throws JSONException {
        JSONObject education = new JSONObject(jobJsonString);
        String schoolUrl = FieldLoaderUtil.load(education, JsonFields.SCHOOL_URL);
        String schoolName = FieldLoaderUtil.load(education, JsonFields.SCHOOL_NAME);
        String degree = FieldLoaderUtil.load(education, JsonFields.DEGREE);
        String field = FieldLoaderUtil.load(education, JsonFields.FIELD);
        String range = FieldLoaderUtil.load(education, JsonFields.DATE_RANGE);
        DateRange dateRange = new DateRange(range);
        String description = FieldLoaderUtil.load(education, JsonFields.DESCRIPTION);
        LOGGER.debug(String.format(Messages.CREATING_EDUCATION_S, schoolName));
        return new LinkedinSchoolData(schoolUrl, schoolName, degree, field, dateRange, description);
    }
}
