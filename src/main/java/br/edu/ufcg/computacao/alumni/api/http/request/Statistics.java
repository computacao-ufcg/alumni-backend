package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.StatisticsResponse;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
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
@Api(description = ApiDocumentation.Alumni.API)
public class Statistics {
    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "statistics";

    private static final Logger LOGGER = Logger.getLogger(Statistics.class);

    @RequestMapping(value = "/{level}/{courseName}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Statistics.GET_STATISTICS_OPERATION)
    public ResponseEntity<StatisticsResponse> getStatistics(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @PathVariable String level,
            @PathVariable String courseName,
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            CourseName c = CourseName.valueOf(level);
            Level l = Level.valueOf(courseName);
            StatisticsResponse statistics = ApplicationFacade.getInstance().getStatistics(token,l , c);
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }

    }
}
