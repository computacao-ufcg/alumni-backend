package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.StatisticsResponse;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@WebMvcTest(value = Statistics.class, secure = false)
@PrepareForTest(ApplicationFacade.class)
public class StatisticsTest {

    private static final String STATISTICS_ENDPOINT = Statistics.ENDPOINT;

    @Autowired
    private MockMvc mockMvc;

    private ApplicationFacade facade;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests the statistics and test a successfully return. Also call
    // the facade to get the statistics
    @Test
    public void getStatisticsTest() throws Exception {
        // set up
        String statisticsEndpoint = STATISTICS_ENDPOINT + "/?level=undergraduate&courseName=computing-science";

        Mockito.doReturn(createFakeStatistics()).when(this.facade)
                .getStatistics(Mockito.anyString(),Mockito.any(Level.class), Mockito.any(CourseName.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, statisticsEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Assert.assertEquals("{\"numberAcademyEmployedCourse\":0,\"numberAcademyEmployedLevel\":0,\"numberAlumniCourse\":0,\"numberAlumniLevel\":0," +
                        "\"numberGovernmentEmployedCourse\":0,\"numberGovernmentEmployedLevel\":0,\"numberIndustryEmployedCourse\":0,\"numberIndustryEmployedLevel\":0," +
                        "\"numberMappedAlumniCourse\":0,\"numberMappedAlumniLevel\":0," +
                        "\"numberOngEmployedCourse\":0,\"numberOngEmployedLevel\":0,\"numberOthersEmployedCourse\":0,\"numberOthersEmployedLevel\":0}",
                result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));
    }

    // Test case: Requests the statistics with unauthorized user. Also call
    // the facade to get the statistics.
    @Test
    public void getStatisticsUnauthorizedExceptionTest() throws Exception {
        // set up
        String statisticsEndpoint = STATISTICS_ENDPOINT + "/?level=undergraduate&courseName=computing-science";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, statisticsEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));
    }

    // Test case: Requests the statistics with unauthenticated user. Also call
    // the facade to get the statistics.
    @Test
    public void getStatisticsUnauthenticatedExceptionTest() throws Exception {
        // set up
        String statisticsEndpoint = STATISTICS_ENDPOINT + "/?level=undergraduate&courseName=computing-science";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, statisticsEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));
    }

    // Test case: Requests the statistics with invalid level parameter and checks the response.
    @Test
    public void getStatisticsInvalidLevelParameterTest() throws Exception {
        // set up
        String statisticsEndpointWrongParameter = STATISTICS_ENDPOINT + "/?level=k&courseName=computing-science";

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, statisticsEndpointWrongParameter, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        assertEquals(expectedStatus, result.getResponse().getStatus());

        assertEquals("{\"details\":\"uri=/statistics/\",\"message\":\"Level must be one of Level options\"}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(0))
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));
    }

    // Test case: Requests the statistics with invalid courseName parameter and checks the response.
    @Test
    public void getStatisticsInvalidCourseNameParameterTest() throws Exception {
        // set up
        String statisticsEndpointWrongParameter = STATISTICS_ENDPOINT + "/?level=undergraduate&courseName=k";

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, statisticsEndpointWrongParameter, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        assertEquals(expectedStatus, result.getResponse().getStatus());

        assertEquals("{\"details\":\"uri=/statistics/\",\"message\":\"Course name must be one of CourseName options\"}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(0))
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String fakeUserToken = "fake-access-id";
        headers.set(CommonKeys.AUTHENTICATION_TOKEN_KEY, fakeUserToken);
        return headers;
    }

    private StatisticsResponse createFakeStatistics() {
        StatisticsModel modelCourse =  new StatisticsModel(0,0,0
                ,0, 0, 0, 0,
                0, 0);
        StatisticsModel modelLevel = new StatisticsModel(0, 0
                , 0, 0, 0, 0, 0,
                0, 0);
        StatisticsResponse statistics = new StatisticsResponse(modelCourse, modelLevel);
        return statistics;
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
