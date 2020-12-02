package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    @Test
    public void getAlumniMatches() throws Exception {
        String getMatchesEndpoint = MATCH_ENDPOINT  + "/list/0";

        Mockito.doReturn(getFakePage()).when(this.facade)
                .getAlumniMatches(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(getMatchesEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniMatches(Mockito.anyString(),Mockito.anyInt());
    }

    @Test
    public void getPendingMatchesTest() throws Exception {
        String getPendingMatchesEndpoint =  MATCH_ENDPOINT + "/pending/0";

        Mockito.doReturn(getFakePage()).when(this.facade)
                .getAlumniPendingMatches(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(getPendingMatchesEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumniPendingMatches(Mockito.anyString(),Mockito.anyInt());
    }

    @Test
    public void getAlumnusMatchesTest() throws Exception {
        final String FAKE_REGISTRATION = "fake-registration1";

        String getAlumnusMatchesEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        List list = new ArrayList();
        Mockito.doReturn(list).when(this.facade)
                .getAlumnusMatches(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(getAlumnusMatchesEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getAlumnusMatches(Mockito.anyString(),Mockito.anyString());
    }

    @Test
    public void setMatchTest() throws Exception {
        final String FAKE_REGISTRATION = "fake-registration1";
        final String FAKE_LINKEIDINID = "fake-id-1";

        String setMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION + "&linkedinId=" + FAKE_LINKEIDINID;

        Mockito.doNothing().when(this.facade).setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .put(setMatchEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setMatch(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void resetMatchTest() throws Exception {
        final String FAKE_REGISTRATION = "fake-registration1";

        String resetMatchEndpoint = MATCH_ENDPOINT + "/?registration=" + FAKE_REGISTRATION;

        Mockito.doNothing().when(this.facade).resetMatch(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .delete(resetMatchEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

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
}
