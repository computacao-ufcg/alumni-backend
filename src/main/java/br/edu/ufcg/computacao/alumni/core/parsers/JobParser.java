package br.edu.ufcg.computacao.alumni.core.parsers;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.constants.JsonFields;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;
import br.edu.ufcg.computacao.alumni.core.util.FieldLoaderUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class JobParser {
    private static Logger LOGGER = Logger.getLogger(JobParser.class);

    public JobParser() {
    }

    public static LinkedinJobData parse(String jobJsonString) throws JSONException {
        JSONObject job = new JSONObject(jobJsonString);
        String companyUrl = FieldLoaderUtil.load(job, JsonFields.COMPANY_URL);
        String companyName = FieldLoaderUtil.load(job, JsonFields.COMPANY_NAME);
        String jobTitle = FieldLoaderUtil.load(job, JsonFields.JOB_TITLE);
        String location = FieldLoaderUtil.load(job, JsonFields.LOCATION);
        String description = FieldLoaderUtil.load(job, JsonFields.DESCRIPTION);
        String range = FieldLoaderUtil.load(job, JsonFields.DATE_RANGE);
        String employerType = FieldLoaderUtil.load(job, "employerType");
        DateRange dateRange = new DateRange(range);
        EmployerResponse employer = new EmployerResponse(companyName, companyUrl, EmployerType.getType(employerType));
        LOGGER.debug(String.format(Messages.CREATING_JOB_S, companyName));
        return new LinkedinJobData(employer, jobTitle, location, description, dateRange);
//        return new LinkedinJobData(companyUrl, companyName, jobTitle, location, description, dateRange);
    }
}
