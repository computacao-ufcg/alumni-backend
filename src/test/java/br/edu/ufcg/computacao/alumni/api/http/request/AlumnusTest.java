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

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(value = Alumnus.class, secure = false)
@PrepareForTest(ApplicationFacade.class)
public class AlumnusTest {

    private static final String ALUMNUS_ENDPOINT = Alumnus.ENDPOINT;

    @Autowired
    private MockMvc mockMvc;

    private ApplicationFacade facade;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests a page of alumni data and tests a successfully return. Checks the response and also call
    // the facade to get alumni data
    @Test
    public void getAlumniTest() throws Exception {
        // set up
        String getAlumniEndpoint = ALUMNUS_ENDPOINT + "/0";

        Mockito.doReturn(createFakePage()).when(this.facade).getAlumniData(Mockito.anyString(),Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniData(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a page of alumni names and tests  a successfully return. Checks the response and also call
    // the facade to get alumni names
    @Test
    public void getAlumniNamesTest() throws Exception {
        // set up
        String getAlumniNamesEndpoint = ALUMNUS_ENDPOINT + "/names/0";

        Mockito.doReturn(createFakePage()).when(this.facade).getAlumniNames(Mockito.anyString(),Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniNamesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniNames(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a page of alumni current job and tests a successfully return. Checks the response and also call
    // the facade to get alumni current job
    @Test
    public void getAlumniCurrentJobTest() throws Exception {
        // set up
        String getAlumniCurrentJobEndpoint = ALUMNUS_ENDPOINT + "/currentJob/0";

        Mockito.doReturn(createFakePage()).when(this.facade).getAlumniCurrentJob(Mockito.anyString(),Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniCurrentJobEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniCurrentJob(Mockito.anyString(),Mockito.anyInt());

    }

    // Test case: Requests a page of alumni data with an unauthorized user. Also call
    // the facade to get alumni data.
    @Test
    public void getAlumniUnauthorizedExceptionTest() throws Exception {
        // set up
        String getAlumniEndpoint = ALUMNUS_ENDPOINT + "/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getAlumniData(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniData(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of alumni names with an unauthorized user. Also call
    // the facade to get alumni names.
    @Test
    public void getAlumniNamesUnauthorizedExceptionTest() throws Exception {
        // set up
        String getAlumniNamesEndpoint = ALUMNUS_ENDPOINT + "/names/0";
        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getAlumniNames(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniNamesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniNames(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of alumni current job with an unauthorized user. Also call
    // the facade to get alumni current job.
    @Test
    public void getAlumniCurrentJobUnauthorizedExceptionTest() throws Exception {
        // set up
        String getAlumniCurrentJobEndpoint = ALUMNUS_ENDPOINT + "/currentJob/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getAlumniCurrentJob(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniCurrentJobEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniCurrentJob(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of alumni data with an unauthenticated user. Also call
    // the facade to get alumni data.
    @Test
    public void getAlumniUnauthenticatedExceptionTest() throws Exception {
        // set up
        String getAlumniEndpoint = ALUMNUS_ENDPOINT + "/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getAlumniData(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniData(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page os alumni names with an unauthenticated user. Also call
    // the facade to get alumni names.
    @Test
    public void getAlumniNamesUnauthenticatedExceptionTest() throws Exception {
        // set up
        String getAlumniNamesEndpoint = ALUMNUS_ENDPOINT + "/names/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getAlumniNames(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniNamesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniNames(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests a page of alumni current job with an unauthenticated user. Also call
    // the facade to get alumni current job.
    @Test
    public void getAlumniCurrentjobUnauthenticatedExceptionTest() throws Exception {
        // set up
        String getAlumniCurrentJobEndpoint = ALUMNUS_ENDPOINT + "/currentJob/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getAlumniCurrentJob(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumniCurrentJobEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniCurrentJob(Mockito.anyString(), Mockito.anyInt());
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
