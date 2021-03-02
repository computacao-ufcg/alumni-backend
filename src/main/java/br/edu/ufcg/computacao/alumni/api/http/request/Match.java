package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.api.http.response.MatchResponse;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.MatchData;
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
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = Match.ENDPOINT)
@Api(description = ApiDocumentation.Match.API)
public class Match {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "match";

    private static final Logger LOGGER = Logger.getLogger(Match.class);

    @ApiOperation(value = ApiDocumentation.Match.SET_MATCH_OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> setMatch(
            @ApiParam(value = ApiDocumentation.Alumni.MATCH_BODY)
            @RequestBody br.edu.ufcg.computacao.alumni.api.parameters.Match match,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            ApplicationFacade.getInstance().setMatch(token, match.getRegistration(), match.getLinkedinId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/list/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Match.GET_MATCHES_OPERATION)
    public ResponseEntity<Page<MatchData>> getAlumniMatches(
            @ApiParam(value = ApiDocumentation.Common.PAGE)
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

            Page<MatchData> matches = ApplicationFacade.getInstance().getAlumniMatches(token, p);
            return new ResponseEntity(matches, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/pending/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Match.GET_PENDING_MATCHES_OPERATION)
    public ResponseEntity<Collection<PendingMatch>> getPendingMatches(
            @ApiParam(value = ApiDocumentation.Common.PAGE)
            @PathVariable String page,
            @ApiParam(value = ApiDocumentation.Match.MIN_SCORE)
            @RequestParam(required = false) String minScore,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            int p;
            try{
                p = Integer.parseInt(page);
            } catch(NumberFormatException e) {
                throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
            }

            int score;
            try {
                score = (minScore != null) ? Integer.parseInt(minScore) : 0;
            } catch (NumberFormatException e) {
                throw new InvalidParameterException(Messages.MIN_SCORE_MUST_BE_AN_INTEGER);
            }

            Page<PendingMatch> pendingMatches = ApplicationFacade.getInstance().getAlumniPendingMatches(token, p, score);
            return new ResponseEntity(pendingMatches, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = ApiDocumentation.Match.RESET_MATCH_OPERATION)
    public ResponseEntity<Void> resetMatch(
            @ApiParam(value = ApiDocumentation.Alumni.REGISTRATION_PARAMETER)
            @RequestParam String registration,
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

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Match.GET_ALUMNUS_MATCHES_OPERATION)
    public ResponseEntity<List<MatchData>> getAlumnusMatches(
            @ApiParam(value = ApiDocumentation.Alumni.REGISTRATION_PARAMETER)
            @RequestParam String registration,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            MatchData response = ApplicationFacade.getInstance().getAlumnusMatches(token, registration);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/search/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Match.GET_MATCHES_SEARCH_OPERATION)
    public ResponseEntity<Page<MatchData>> getMatchesSearch(
            @ApiParam(value = ApiDocumentation.Common.PAGE)
            @PathVariable String page,
            @ApiParam(value = ApiDocumentation.Match.NAME_PARAMETER)
            @RequestParam(required = false, defaultValue = "^$") String name,
            @ApiParam(value = ApiDocumentation.Match.ADMISSION_PARAMETER)
            @RequestParam(required = false, defaultValue = "^$") String admission,
            @ApiParam(value = ApiDocumentation.Match.GRADUATION_PARAMETER)
            @RequestParam(required = false, defaultValue = "^$") String graduation,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            int p;
            try{
                p = Integer.parseInt(page);
            } catch(NumberFormatException e) {
                throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
            }
            Page<MatchData> response = ApplicationFacade.getInstance().getMatchesSearchPage(p, token, name, admission, graduation);
            return new ResponseEntity(response, HttpStatus.OK);

        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}
