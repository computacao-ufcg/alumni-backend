package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthenticatedUserException;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthorizedRequestException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(value = Linkedin.class, secure = false)
@PrepareForTest(ApplicationFacade.class)
public class LinkedinTest {

    private static final String LINKEDIN_ENDPOINT = Linkedin.ENDPOINT;

    @Autowired
    private MockMvc mockMvc;

    private ApplicationFacade facade;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests a page of linkedin data and test a successfully return. Also call
    // the facade to get linkedin data
    @Test
    public void getLinkedinDataTest() throws Exception {
        // set up
        String linkedinDataEndpoint = LINKEDIN_ENDPOINT + "/0";
        Mockito.doReturn(createFakePage()).when(this.facade)
                .getLinkedinAlumniData(Mockito.anyString(),Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinDataEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getLinkedinAlumniData(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a page of linkedin data with page value that is not a number and checks the response.
    @Test
    public void getLinkedinDataWithInvalidPageParameterTest() throws Exception {
        // set up
        String linkedinDataEndpointWrongParameter = LINKEDIN_ENDPOINT + "/k";

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinDataEndpointWrongParameter, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        assertEquals(expectedStatus, result.getResponse().getStatus());

        assertEquals("{\"message\":\"Page parameter must be an integer.\",\"details\":\"uri=/linkedin/k\"}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(0))
                .getLinkedinAlumniData(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a page of linkedin linkedin name profile pairs and test a successfully return. Also call
    // the facade to get linkedin name profile pairs
    @Test
    public void getLinkedinNameProfilePairsTest() throws Exception {
        // set up
        String linkedinNameProfilePairsEndpoint = LINKEDIN_ENDPOINT + "/entries/0";

        Mockito.doReturn(createFakePage()).when(this.facade)
                .getLinkedinNameProfilePairs(Mockito.anyString(),Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinNameProfilePairsEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1)).
                getLinkedinNameProfilePairs(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a page of linkedin data with unauthorized user. Also call the facade
    // to get linkedin data
    @Test
    public void getLinkedinDataUnauthorizedExceptionTest() throws Exception {
        // set up
        String linkedinDataEndpoint = LINKEDIN_ENDPOINT + "/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getLinkedinAlumniData(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinDataEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getLinkedinAlumniData(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of linkedin name profile pairs with unauthorized user. Also
    // call the facade to get linkedin name profile pairs
    @Test
    public void getLinkedinNameProfilePairsUnauthorizedExceptionTest() throws Exception {
        // set up
        String linkedinProfilePairsEndpoint = LINKEDIN_ENDPOINT + "/entries/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getLinkedinNameProfilePairs(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinProfilePairsEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getLinkedinNameProfilePairs(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of linkedin data with unauthenticated user. Also call
    // the facade to get linkedin data
    @Test
    public void getLinkedindataUnauthenticatedExceptionTest() throws Exception {
        // set up
        String linkedinDataEndpoint = LINKEDIN_ENDPOINT + "/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getLinkedinAlumniData(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinDataEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getLinkedinAlumniData(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of linkedin name profile pairs with unauthenticated user. Also call
    // the facade to get linkedin name profile pairs
    @Test
    public void getLinkedinNameProfilePairsUnauthenticatedExceptionTest() throws Exception {
        // set up
        String linkedinNameProfilePairsEndpoint = LINKEDIN_ENDPOINT + "/entries/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getLinkedinNameProfilePairs(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, linkedinNameProfilePairsEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getLinkedinNameProfilePairs(Mockito.anyString(), Mockito.anyInt());
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String fakeUserToken = "fake-access-id";
        headers.set(CommonKeys.AUTHENTICATION_TOKEN_KEY, fakeUserToken);
        return headers;
    }

    private Page createFakePage() {
        Pageable pageable= new PageRequest(0, 10);

        List list = new ArrayList<>();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > list.size() ?
                list.size() : (start + pageable.getPageSize()));

        Page page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;

    }

    private RequestBuilder createRequestBuilder(HttpMethod method, String urlTemplate, HttpHeaders headers, String body) {
        switch (method) {
            case GET:
                return MockMvcRequestBuilders.get(urlTemplate)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON);
            default:
                return null;
        }
    }
}
