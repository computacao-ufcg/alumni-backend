package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.StatisticsResponse;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.DefaultValues;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Level;
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
@RequestMapping(value = Statistics.ENDPOINT)
@Api(description = ApiDocumentation.Statistics.API)
public class Statistics {
    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "statistics";

    private static final Logger LOGGER = Logger.getLogger(Statistics.class);

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Statistics.GET_STATISTICS_OPERATION)
    public ResponseEntity<StatisticsResponse> getStatistics(
            @ApiParam(value = ApiDocumentation.Model.LEVEL)
            @RequestParam(defaultValue = DefaultValues.DEFAULT_LEVEL) String level,
            @ApiParam(value = ApiDocumentation.Model.COURSE_NAME)
            @RequestParam(defaultValue = DefaultValues.DEFAULT_COURSE_NAME) String courseName,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            LOGGER.info(Messages.RECEIVING_GET_STATISTICS);
            CourseName c = CourseName.getCourseName(courseName.toLowerCase());
            Level l = Level.getLevel(level.toLowerCase());

            if(c.getValue().equals("undefined")) {
                throw new InvalidParameterException(Messages.COURSE_NAME_PARAM_MUST_BE_A_VALID_COURSE_NAME);

            } else if(l.getValue().equals("undefined")) {
                throw new InvalidParameterException(Messages.LEVEL_PARAM_MUST_BE_A_VALID_LEVEL);
            }
            StatisticsResponse statistics = ApplicationFacade.getInstance().getStatistics(token, l, c);
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }

    }
}
