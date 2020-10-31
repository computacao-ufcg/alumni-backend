package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.api.http.response.MatchResponse;
import br.edu.ufcg.computacao.alumni.api.parameters.MatchParameter;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping(value = Match.ENDPOINT)
@Api(description = ApiDocumentation.Match.API)
public class Match {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "match";

    private static final Logger LOGGER = Logger.getLogger(Match.class);

    @ApiOperation(value = ApiDocumentation.Match.SET_MATCH_OPERATION)
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> setMatch(
            @ApiParam(value = ApiDocumentation.Match.MATCH_PARAMETER)
            @RequestBody MatchParameter matchParameter,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            System.out.println("Putting " + matchParameter.getRegistration() + ":" + matchParameter.getLinkedinId());
            ApplicationFacade.getInstance().setMatch(token, matchParameter.getRegistration(), matchParameter.getLinkedinId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/list/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Match.GET_MATCHES_OPERATION)
    public ResponseEntity<Page<LinkedinNameProfilePair>> getAlumniMatches(
            @ApiParam(value = ApiDocumentation.Match.PAGE)
            @PathVariable String page,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            int p;
            try {
                p = Integer.parseInt(page);
            } catch(NumberFormatException e) {
                throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
            }

            Page<MatchResponse> matches = ApplicationFacade.getInstance().getAlumniMatches(token, p);
            return new ResponseEntity(matches, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Match.GET_PENDING_MATCHES_OPERATION)
    public ResponseEntity<Collection<PendingMatch>> getPendingMatches(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            Collection<PendingMatch> pendingMatches = ApplicationFacade.getInstance().getAlumniPendingMatches(token);
            return new ResponseEntity<>(pendingMatches, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/{registration}", method = RequestMethod.DELETE)
    @ApiOperation(value = ApiDocumentation.Match.RESET_MATCH_OPERATION)
    public ResponseEntity<Void> resetMatch(
            @ApiParam(value = ApiDocumentation.Match.REGISTRATION_PARAMETER)
            @PathVariable String registration,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            ApplicationFacade.getInstance().resetMatch(token, registration.toString());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}
