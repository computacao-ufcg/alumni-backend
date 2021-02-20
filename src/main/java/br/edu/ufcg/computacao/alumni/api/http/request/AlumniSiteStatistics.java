package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.AlumniSiteStatisticsResponse;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = AlumniSiteStatistics.ENDPOINT)
@Api(description = ApiDocumentation.AlumniSiteStatistics.API)
public class AlumniSiteStatistics {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "alumniSiteStatistics";

    private static final Logger LOGGER = Logger.getLogger(AlumniSiteStatistics.class);

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.AlumniSiteStatistics.GET_ALUMNI_SITE_STATISTICS_OPERATION)
    public ResponseEntity<AlumniSiteStatisticsResponse> getAlumniSiteStatistics(
            @RequestParam String courseName,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            CourseName c = CourseName.getCourseName(courseName.toLowerCase());
            if(c.getValue().equals("undefined")) {
                throw new InvalidParameterException(Messages.COURSE_NAME_PARAM_MUST_BE_A_VALID_COURSE_NAME);
            }
            AlumniSiteStatisticsResponse alumniSiteStatisticsResponse = ApplicationFacade.getInstance().getAlumniSiteStatistics(token, c);
            return new ResponseEntity<>(alumniSiteStatisticsResponse, HttpStatus.OK);

        } catch(Exception e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}

