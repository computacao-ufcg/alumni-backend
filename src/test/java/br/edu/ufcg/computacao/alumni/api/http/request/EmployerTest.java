package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
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

    private static final String EMPLOYERS_ENDPOINT = Employer.ENDPOINT;

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
        String classifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/classified/0";

        Mockito.doReturn(getFakePage()).when(this.facade).getClassifiedEmployers(Mockito.anyString(),Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(classifiedEmployersEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(),Mockito.anyInt());
    }



    @Test
    public void getClassifiedEmployersByTypeTest() throws Exception {
        String classifiedEmployersByTypeEndpoint = EMPLOYERS_ENDPOINT + "/classifiedByType/0?type=academy";

        Mockito.doReturn(getFakePage()).when(this.facade)
                .getClassifiedEmployersByType(Mockito.anyString(), Mockito.anyInt(), Mockito.any(EmployerType.class));

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(classifiedEmployersByTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployersByType(Mockito.anyString(),Mockito.anyInt(), Mockito.any(EmployerType.class));

    }

    @Test
    public void getUnclassifiedEmployersTest() throws Exception {
        String unclassifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/unclassified/0";

        Mockito.doReturn(getFakePage()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(unclassifiedEmployersEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(),Mockito.anyInt());
    }

    @Test
    public void deleteEmployerTypeTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=" + FAKE_ID;

        Mockito.doNothing().when(this.facade).setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete(deleteEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void deleteEmployerTypeNotFoundTest() throws Exception {

        final String FAKE_ID = "fake-Id-1";

        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=" + FAKE_ID;

        Mockito.doThrow(new InvalidParameterException()).when(this.facade)
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete(deleteEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void setEmployerTypeTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=" + FAKE_ID;

        Mockito.doNothing().when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(setEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());

    }

    @Test
    public void setTypeEmployerNotFoundTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=" + FAKE_ID;

        Mockito.doThrow(new InvalidParameterException()).when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(setEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());
    }

    @Test
    public void getClassifiedEmployersUnauthorizedExceptionTest() throws Exception {
        String classifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/classified/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(classifiedEmployersEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void getClassifiedEmployersByTypeUnauthorizedExceptionTest() throws Exception {
        String classifiedEmployersByTypeEndpoint = EMPLOYERS_ENDPOINT + "/classifiedByType/0?type=academy";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getClassifiedEmployersByType(Mockito.anyString(), Mockito.anyInt(), Mockito.any(EmployerType.class));

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(classifiedEmployersByTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployersByType(Mockito.anyString(), Mockito.anyInt(), Mockito.any(EmployerType.class));
    }

    @Test
    public void getUnclassifiedEmployersByTypeUnauthorizedExceptionTest() throws Exception {
        String unclassifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/unclassified/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(unclassifiedEmployersEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void deleteEmployerTypeUnauthorizedExceptionTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=" + FAKE_ID;

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete(deleteEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void setEmployerTypeUnauthorizedExceptionTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=" + FAKE_ID;

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(setEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());
    }

    @Test
    public void getClassifiedEmployersUnauthenticatedExceptionTest() throws Exception {
        String classifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/classified/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(classifiedEmployersEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void getClassifiedEmployersByTypeUnauthenticatedExceptionTest() throws Exception {
        String classifiedEmployersByTypeEndpoint = EMPLOYERS_ENDPOINT + "/classifiedByType/0?type=academy";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getClassifiedEmployersByType(Mockito.anyString(), Mockito.anyInt(), Mockito.any(EmployerType.class));

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(classifiedEmployersByTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployersByType(Mockito.anyString(), Mockito.anyInt(), Mockito.any(EmployerType.class));
    }

    @Test
    public void getUnclassifiedEmployersByTypeUnauthenticatedExceptionTest() throws Exception {
        String unclassifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/unclassified/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(unclassifiedEmployersEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void deleteEmployerTypeUnauthenticatedExceptionTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=" + FAKE_ID;

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete(deleteEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void setEmployerTypeUnauthenticatedExceptionTest() throws Exception {
        final String FAKE_ID = "fake-Id-1";

        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=" + FAKE_ID;

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyString());

        HttpHeaders headers = getHttpHeaders();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(setEmployerTypeEndpoint)
                .headers(headers).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.any(EmployerType.class),Mockito.anyString());
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
