package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(value = Version.class, secure = false)
@PrepareForTest({ApplicationFacade.class})
public class VersionTest {

    private static final String VERSION_END_POINT =  Version.VERSION_ENDPOINT;

    private ApplicationFacade facade;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests the version number and test a successfully return. Checks the response ans also call
    // the facade to get the version number.
    @Test
    public void getVersionNumberTest() throws Exception {
        // set up
        Mockito.doReturn(SystemConstants.API_VERSION_NUMBER).when(this.facade).getVersionNumber();

        HttpHeaders headers = new HttpHeaders();

        // exercise
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(VERSION_END_POINT)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();
        String expectedResponse = String.format("{\"version\":\"%s\"}", SystemConstants.API_VERSION_NUMBER);

        String versionNumber = result.getResponse().getContentAsString();

        Assert.assertEquals(expectedResponse, versionNumber);
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

    }
}
