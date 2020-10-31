package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.response.VersionNumber;
import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = Version.VERSION_ENDPOINT)
@Api(description = ApiDocumentation.Version.API)
public class Version {

    public static final String VERSION_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "version";

    private static final Logger LOGGER = Logger.getLogger(Version.class);

    @ApiOperation(value = ApiDocumentation.Version.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<VersionNumber> getVersion() {
        String versionNumber = null;
        versionNumber = ApplicationFacade.getInstance().getVersionNumber();
        return new ResponseEntity<VersionNumber>(new VersionNumber(versionNumber), HttpStatus.OK);
    }
}
