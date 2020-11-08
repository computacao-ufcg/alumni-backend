package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinNameProfilePair;
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
@RequestMapping(value = Linkedin.ENDPOINT)
@Api(description = ApiDocumentation.Alumni.API)
public class Linkedin {

    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "linkedin";

    private static final Logger LOGGER = Logger.getLogger(Linkedin.class);

    @RequestMapping(value = "/{page}",method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_OPERATION)
    public ResponseEntity<Page<LinkedinAlumnusData>> getLinkedinData(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @PathVariable String page,
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            int p;
            try {
                p = Integer.parseInt(page);
            } catch(NumberFormatException e) {
                throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
            }
            Page<LinkedinAlumnusData> linkedinAlumniData = ApplicationFacade.getInstance().getLinkedinAlumniData(token, p);
            return new ResponseEntity<>(linkedinAlumniData, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/entries/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Alumni.GET_NAMES_OPERATION)
    public ResponseEntity<Page<LinkedinNameProfilePair>> getLinkedinNameProfilePairs(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @PathVariable String page,
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            int p;
            try {
                p = Integer.parseInt(page);
            } catch(NumberFormatException e) {
                throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
            }
            Page<LinkedinNameProfilePair> alumniNames = ApplicationFacade.getInstance().getLinkedinNameProfilePairs(token, p);
            return new ResponseEntity<>(alumniNames, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}
