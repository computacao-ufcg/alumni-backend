package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
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

@CrossOrigin
@RestController
@RequestMapping(value = Alumnus.ENDPOINT)
@Api(description = ApiDocumentation.Alumni.API)
public class Alumnus {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "alumnus";

    private static final Logger LOGGER = Logger.getLogger(Alumnus.class);

    @RequestMapping(value = "/{page}",method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_OPERATION)
    public ResponseEntity<Page<UfcgAlumnusData>> getAlumni(
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
            Page<UfcgAlumnusData> ufcgAlumniData = ApplicationFacade.getInstance().getAlumniData(token, p);
            return new ResponseEntity<>(ufcgAlumniData, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/names/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_NAMES_OPERATION)
    public ResponseEntity<Page<String>> getAlumniNames(
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
            Page<String> alumniNames = ApplicationFacade.getInstance().getAlumniNames(token, p);
            return new ResponseEntity<>(alumniNames, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/currentJob/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_CURRENT_JOB_OPERATION)
    public ResponseEntity<Page<CurrentJob>> getAlumniCurrentJob(
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
            Page<CurrentJob> currentJobs = ApplicationFacade.getInstance().getAlumniCurrentJob(token, p);
            return new ResponseEntity<>(currentJobs, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.debug(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}
