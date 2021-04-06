package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.parameters.EmployerIdData;
import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;
import br.edu.ufcg.computacao.alumni.api.http.response.EmployerTypeResponse;
import br.edu.ufcg.computacao.alumni.api.parameters.EmployerClassification;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.UnknownEmployer;
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
@RequestMapping(value = Employer.ENDPOINT)
@Api(description = ApiDocumentation.Employers.API)
public class Employer {
    protected static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "employer";

    private static final Logger LOGGER = Logger.getLogger(Employer.class);

    @RequestMapping(value = "/classified/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Employers.GET_EMPLOYER_OPERATION)
    public ResponseEntity<Page<ConsolidatedEmployer>> getClassifiedEmployers(
            @ApiParam(value = ApiDocumentation.Common.PAGE)
            @PathVariable String page,
            @ApiParam(value = ApiDocumentation.Employers.TYPE)
            @RequestParam(required = false) String type,
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

            EmployerType employerType = EmployerType.getType(type);
            Page<ConsolidatedEmployer> employers = ApplicationFacade.getInstance().getClassifiedEmployers(token, employerType, p);
            return new ResponseEntity<>(employers, HttpStatus.OK);
        } catch(EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }

    }

    @RequestMapping(value = "/unknown/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Employers.GET_UNKNOWN_EMPLOYERS)
    public ResponseEntity<Page<UnknownEmployer>> getUnknownEmployers(
            @ApiParam(value = ApiDocumentation.Common.PAGE)
            @PathVariable String page,
            @RequestHeader(value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token
    ) throws EurecaException {

        try {

            int requiredPage;
            try {
                requiredPage = Integer.parseInt(page);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
            }

            Page<UnknownEmployer> unknownEmployers = ApplicationFacade.getInstance().getUnknownEmployers(token, requiredPage);
            return new ResponseEntity<>(unknownEmployers, HttpStatus.OK);
        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/unknown", method = RequestMethod.PUT)
    @ApiOperation(value = ApiDocumentation.Employers.SET_EMPLOYER_URL)
    public ResponseEntity<Void> setUnknownEmployerUrl(
            @ApiParam(value = ApiDocumentation.Employers.UNKNOWN_EMPLOYER_ID_DATA)
            @RequestBody EmployerIdData employerBody,
            @RequestHeader(value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token
    )
        throws EurecaException {
        ApplicationFacade.getInstance().setUnknownEmployerUrl(token, employerBody.getCurrentId(), employerBody.getNewId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/unclassified/{page}", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Employers.GET_EMPLOYERS_UNDEFINED)
    public ResponseEntity<Page<ConsolidatedEmployer>> getUnclassifiedEmployers(
            @ApiParam(value = ApiDocumentation.Common.PAGE)
            @PathVariable String page,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

            try {
                int p;
                try{
                    p = Integer.parseInt(page);
                }catch(NumberFormatException e) {
                    throw new InvalidParameterException(Messages.PAGE_MUST_BE_AN_INTEGER);
                }
                Page<ConsolidatedEmployer> employers = ApplicationFacade.getInstance().getUnclassifiedEmployers(token, p);
                return new ResponseEntity<>(employers, HttpStatus.OK);
            } catch (EurecaException e) {
                LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
                throw e;
            }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = ApiDocumentation.Employers.DELETE_EMPLOYER_TYPE)
    public ResponseEntity<Void> deleteEmployerType(
            @ApiParam(value = ApiDocumentation.Linkedin.LINKEDIN_ID_PARAMETER)
            @RequestBody String linkedinId,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            ApplicationFacade.getInstance().setEmployerTypeToUndefined(token, linkedinId);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @ApiOperation(value = ApiDocumentation.Employers.GET_EMPLOYER_TYPES)
    public ResponseEntity<Collection<EmployerTypeResponse>> getEmployerTypes(
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            Collection<EmployerTypeResponse> types = ApplicationFacade.getInstance().getEmployerTypes(token);
            return new ResponseEntity<>(types, HttpStatus.OK);
        } catch (EurecaException e){
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()));
            throw e;
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = ApiDocumentation.Employers.SET_EMPLOYER_TYPE)
    public ResponseEntity<Void> setEmployerType(
            @ApiParam(value = ApiDocumentation.Employers.EMPLOYER_BODY)
            @RequestBody EmployerClassification employer,
            @ApiParam(value = ApiDocumentation.Token.AUTHENTICATION_TOKEN)
            @RequestHeader(required = true, value = CommonKeys.AUTHENTICATION_TOKEN_KEY) String token)
            throws EurecaException {

        try {
            EmployerType type = EmployerType.getType(employer.getType().trim().toLowerCase());

            if (type.equals(EmployerType.UNDEFINED)) {
                throw new InvalidParameterException(Messages.TYPE_MUST_BE_AN_EMPLOYER_TYPE);
            }
            ApplicationFacade.getInstance().setEmployerType(token, employer.getLinkedinId(), type);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (EurecaException e) {
            LOGGER.info(String.format(Messages.SOMETHING_WENT_WRONG, e.getMessage()), e);
            throw e;
        }
    }
}
