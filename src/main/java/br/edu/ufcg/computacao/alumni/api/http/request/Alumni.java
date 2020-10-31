package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
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
@RequestMapping(value = Alumni.ENDPOINT)
@Api(description = ApiDocumentation.Alumni.API)
public class Alumni {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "alumni";

    private static final Logger LOGGER = Logger.getLogger(Alumni.class);

    @ApiOperation(value = ApiDocumentation.Alumni.DESCRIPTION)
    @GetMapping
    public ResponseEntity<Collection<UfcgAlumnusData>> getAlumni(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            Collection<UfcgAlumnusData> ufcgAlumniData = ApplicationFacade.getInstance().getAlumniData(token);
            return new ResponseEntity<>(ufcgAlumniData, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_NAMES_OPERATION)
    public ResponseEntity<List<String>> getAlumniNames(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            List<String> alumniNames = ApplicationFacade.getInstance().getAlumniNames(token);
            return new ResponseEntity<>(alumniNames, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/currentJob", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_CURRENT_JOB_OPERATION)
    public ResponseEntity<List<CurrentJob>> getAlumniCurrentJob(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            List<CurrentJob> currentJobs = ApplicationFacade.getInstance().getAlumniCurrentJob(token);
            return new ResponseEntity<>(currentJobs, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.debug(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/matches", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_MATCHES_OPERATION)
    public ResponseEntity<Page<LinkedinNameProfilePair>> getAlumniMatches(
            @RequestParam(required = true, value = "page") int page,
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            Page<LinkedinNameProfilePair> matches = ApplicationFacade.getInstance().getAlumniMatches(token, page);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/pendingMatches", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_PENDING_MATCHES_OPERATION)
    public ResponseEntity<Collection<PendingMatch>> getPendingMatches(
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

    @RequestMapping(value = "/setMatch", method = RequestMethod.PUT)
    @ApiOperation(value = ApiDocumentation.Alumni.SET_MATCH_OPERATION)
    public ResponseEntity<Void> setMatch(
            @RequestBody(required = true) String registration,
            @RequestBody(required = true) String linkedinId,
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            ApplicationFacade.getInstance().setMatch(token, registration, linkedinId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/unsetMatch", method = RequestMethod.PUT)
    @ApiOperation(value = ApiDocumentation.Alumni.UNSET_MATCH_OPERATION)
    public ResponseEntity<Void> unsetMatch(
            @RequestBody(required = true) String registration,
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            ApplicationFacade.getInstance().resetMatch(token, registration);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}
