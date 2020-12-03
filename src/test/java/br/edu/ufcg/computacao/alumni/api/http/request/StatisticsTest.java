package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.http.response.StatisticsResponse;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
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

    @Test
    public void getStatisticsTest() throws Exception {
        String getStatisticsEndpoint = STATISTICS_ENDPOINT + "/?level=undergraduate&courseName=computing-science";
        Mockito.doReturn(getFakeStatistics()).when(this.facade)
                .getStatistics(Mockito.anyString(),Mockito.any(Level.class), Mockito.any(CourseName.class));
        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(getStatisticsEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getStatistics(Mockito.anyString(), Mockito.any(Level.class), Mockito.any(CourseName.class));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String fakeUserToken = "fake-access-id";
        headers.set(CommonKeys.AUTHENTICATION_TOKEN_KEY, fakeUserToken);
        return headers;
    }

    private StatisticsResponse getFakeStatistics() {
        StatisticsModel modelCourse =  new StatisticsModel(0,0,0
                ,0, 0, 0, 0);
        StatisticsModel modelLevel = new StatisticsModel(0, 0
                , 0, 0, 0, 0, 0);
        StatisticsResponse statistics = new StatisticsResponse(modelCourse, modelLevel);
        return statistics;
    }
}
