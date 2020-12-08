package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
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
@WebMvcTest(value = PublicKey.class, secure = false)
@PrepareForTest({ApplicationFacade.class})
public class PublicKeyTest {

    private static final String PUBLIC_KEY_ENDPOINT =  PublicKey.PUBLIC_KEY_ENDPOINT;

    private ApplicationFacade facade;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests the public key and test a successfully return. Also call
    // the facade to get the public key.
    @Test
    public void getPublicKeyTest() throws Exception {
        // set up
        Mockito.doReturn(ConfigurationPropertyKeys.ALUMNI_PUBLICKEY_FILE_KEY).when(this.facade).getPublicKey();

        HttpHeaders headers = new HttpHeaders();

        // exercise
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_KEY_ENDPOINT)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
    }
}
