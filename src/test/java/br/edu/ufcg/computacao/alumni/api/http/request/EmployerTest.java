package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
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
@WebMvcTest(value = Employer.class, secure = false)
@PrepareForTest(ApplicationFacade.class)
public class EmployerTest {

    private static final String CLASSIFIED_EMPLOYERS_ENDPOINT = Employer.ENDPOINT + "/classified";
    private static final String CLASSIFIED_EMPLOYERS_BY_TYPE_SUFIX = Employer.ENDPOINT + "/classifiedByType";
    private static final String UNCLASSIFIED_EMPLOYERS_SUFIX = Employer.ENDPOINT + "/unclassified";

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
    public void getClassifiedEmployersTest() throws Exception {
        Mockito.doReturn(getFakePage()).when(this.facade).getClassifiedEmployers(Mockito.anyString(),Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(CLASSIFIED_EMPLOYERS_ENDPOINT + "/0")
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(),Mockito.anyInt());
    }

    @Test
    public void getClassifiedEmployersByTypeTest() throws Exception {
        EmployerType type = EmployerType.getType("academy");
        Mockito.doReturn(getFakePage()).when(this.facade)
                .getClassifiedEmployersByType(Mockito.anyString(), Mockito.anyInt(), Mockito.anyObject());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(CLASSIFIED_EMPLOYERS_BY_TYPE_SUFIX + "/0" + "?type=academy" )
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployersByType(Mockito.anyString(),Mockito.anyInt(), Mockito.anyObject());

    }

    @Test
    public void getUnclassifiedEmployersTest() throws Exception {
        Mockito.doReturn(getFakePage()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(UNCLASSIFIED_EMPLOYERS_SUFIX + "/0")
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(),Mockito.anyInt());
    }

    @Test
    public void deleteEmployerTypeTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";
        Mockito.doNothing().when(this.facade).setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/employer/?linkedinId=" + FAKE_ID )
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1)).setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void setEmployerTypeTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";
        Mockito.doNothing().when(this.facade).setEmployerType(Mockito.anyString(), Mockito.anyObject(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/employer/?type=academy&linkedinId=" + FAKE_ID )
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.anyObject(), Mockito.anyString());

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
