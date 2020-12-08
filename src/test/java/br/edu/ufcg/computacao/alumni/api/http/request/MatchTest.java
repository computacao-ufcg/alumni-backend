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
@WebMvcTest(value = Match.class, secure = false)
@PrepareForTest(ApplicationFacade.class)
public class MatchTest {

    private static final String MATCH_ENDPOINT = Match.ENDPOINT;

    @Autowired
    private MockMvc mockMvc;

    private ApplicationFacade facade;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests a page of alumni matches and test a successfully return. Also call
    // the facade to get alumni matches.
    @Test
    public void getAlumniMatches() throws Exception {
        // set up
        String getMatchesEndpoint = MATCH_ENDPOINT  + "/list/0";

        Mockito.doReturn(getFakePage()).when(this.facade)
                .getAlumniMatches(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniMatches(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a page of pending matches and test a successfully return. Also call
    // the facade to get the pending matches.
    @Test
    public void getPendingMatchesTest() throws Exception {
        // set up
        String getPendingMatchesEndpoint =  MATCH_ENDPOINT + "/pending/0";

        Mockito.doReturn(getFakePage()).when(this.facade)
                .getAlumniPendingMatches(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getPendingMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniPendingMatches(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Requests a list of alumnus matches and test a successfully return. Also call
    // the facade to get the alumnus matches.
    @Test
    public void getAlumnusMatchesTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";

        String getAlumnusMatchesEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        List list = new ArrayList();
        Mockito.doReturn(list).when(this.facade)
                .getAlumnusMatches(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumnusMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumnusMatches(Mockito.anyString(),Mockito.anyString());
    }

    // Test case: Set an alumnus match and test a successfully return. Also call
    // the facade to set alumnus match.
    @Test
    public void setMatchTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";
        final String FAKE_LINKEIDINID = "fake-id-1";

        String setMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION + "&linkedinId=" + FAKE_LINKEIDINID;

        Mockito.doNothing().when(this.facade).setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setMatchEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Reset an alumnus match and test a successfully return. Also call
    // the facade to reset a match.
    @Test
    public void resetMatchTest() throws Exception {
        // set up
        String FAKE_REGISTRATION = "fake-registration1";

        String resetMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        Mockito.doNothing().when(this.facade).resetMatch(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, resetMatchEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .resetMatch(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Requests alumni matches with unauthorized user. Also call
    // the facade to get alumni matches.
    @Test
    public void getAlumniMatchesUnauthorizedExceptionTest() throws Exception {
        // set up
        String getMatchesEndpoint = MATCH_ENDPOINT  + "/list/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getAlumniMatches(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniMatches(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests alumni pending matches with unauthorized user. Also call
    // the facade to get pending matches.
    @Test
    public void getAlumniPendingMatchesUnauthorizedExceptionTest() throws Exception {
        // set up
        String getPendingMatchesEndpoint =  MATCH_ENDPOINT + "/pending/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getAlumniPendingMatches(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getPendingMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniPendingMatches(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests alumnus matches with unauthorized user. Also call
    // the facade to get alumnus matches.
    @Test
    public void getAlumnusMatchesUnauthorizedExceptionTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";

        String getAlumnusMatchesEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getAlumnusMatches(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumnusMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumnusMatches(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Set an alumnus match with unauthorized user. Also call
    // the facade to set a match.
    @Test
    public void setMatchUnauthorizedExceptionTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";
        final String FAKE_LINKEIDINID = "fake-id-1";

        String setMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION + "&linkedinId=" + FAKE_LINKEIDINID;

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setMatchEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Reset an alumnus match with unauthorized user. Also call
    // the facade to reset a match.
    @Test
    public void resetMatchUnauthorizedExceptionTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";

        String resetMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .resetMatch(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, resetMatchEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .resetMatch(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Request alumni matches with unauthenticated user. Also call
    // the facade to get alumni matches.
    @Test
    public void getAlumniMatchesUnauthenticatedExceptionTest() throws Exception {
        // set up
        String getMatchesEndpoint = MATCH_ENDPOINT  + "/list/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getAlumniMatches(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniMatches(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Requests alumni pending matches with unauthenticated user. Also call
    // the facade to get alumni pending matches.
    @Test
    public void getPendingMatchesUnauthenticatedExceptionTest() throws Exception {
        // set up
        String getPendingMatchesEndpoint =  MATCH_ENDPOINT + "/pending/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getAlumniPendingMatches(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getPendingMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniPendingMatches(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Request alumnus matches with unauthenticated user. Also call
    // the facade to get alumnus matches.
    @Test
    public void getAlumnusMatchesUnauthenticatedExceptionTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";

        String getAlumnusMatchesEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getAlumnusMatches(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, getAlumnusMatchesEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumnusMatches(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Set an alumnus match with unauthenticated user. Also call
    // the facade to set a match.
    @Test
    public void setMatchUnauthenticatedExceptionTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";
        final String FAKE_LINKEIDINID = "fake-id-1";

        String setMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION + "&linkedinId=" + FAKE_LINKEIDINID;

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setMatchEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Reset an alumnus match with unauthenticated user. Also call
    // the facade to reset a match.
    @Test
    public void resetMatchUnauthenticatedExceptionTest() throws Exception {
        // set up
        final String FAKE_REGISTRATION = "fake-registration1";

        String resetMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .resetMatch(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, resetMatchEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .resetMatch(Mockito.anyString(), Mockito.anyString());
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String fakeUserToken = "fake-access-id";
        headers.set(CommonKeys.AUTHENTICATION_TOKEN_KEY, fakeUserToken);
        return headers;
    }

    private Page getFakePage() {
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
            case DELETE:
                return MockMvcRequestBuilders.delete(urlTemplate)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON);
            case PUT:
                return MockMvcRequestBuilders.put(urlTemplate)
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON);
            default:
                return null;
        }
    }
}
