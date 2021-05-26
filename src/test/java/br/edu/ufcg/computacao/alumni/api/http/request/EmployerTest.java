package br.edu.ufcg.computacao.alumni.api.http.request;

import br.edu.ufcg.computacao.alumni.api.http.CommonKeys;
import br.edu.ufcg.computacao.alumni.api.parameters.EmployerClassification;
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
@WebMvcTest(value = Employer.class, secure = false)
@PrepareForTest(ApplicationFacade.class)
public class EmployerTest {

    private static final String EMPLOYERS_ENDPOINT = Employer.ENDPOINT;
    private static final String EXPECTED_RESPONSE = "{\"content\":[],\"first\":true,\"last\":true,\"number\":0,\"numberOfElements\":0,\"size\":10,\"sort\":null,\"totalElements\":0,\"totalPages\":0}";

    @Autowired
    private MockMvc mockMvc;

    private ApplicationFacade facade;

    @Before
    public void setUp() {
        this.facade = Mockito.spy(ApplicationFacade.class);
        PowerMockito.mockStatic(ApplicationFacade.class);
        BDDMockito.given(ApplicationFacade.getInstance()).willReturn(this.facade);
    }

    // Test case: Requests a page of classified employers and tests a successfully return. Checks the response and also call
    // the facade to get classified employers.
    @Test
    public void getClassifiedEmployersTest() throws Exception {
        // set up
        String classifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/classified/0";

        Mockito.doReturn(createFakePage()).when(this.facade).getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals(EXPECTED_RESPONSE, result.getResponse().getContentAsString());
//        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
//                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
//                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }

    // Test case: Requests a page of employers with page value that is not a number and checks the response.
    @Test
    public void getClassifiedEmployersWithInvalidPageParameterTest() throws Exception {
        // set up
        String classifiedEmployersEndpointWrongParameter = EMPLOYERS_ENDPOINT + "/classified/k";

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersEndpointWrongParameter, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        assertEquals(expectedStatus, result.getResponse().getStatus());

        assertEquals("{\"details\":\"uri=/employer/classified/k\",\"message\":\"Page parameter must be an integer.\"}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(0))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }


    // Test case: Requests a page of classified employers by type and tests a successfully return. Checks the
    // response and also call the facade to get classified by type employers.
    @Test
    public void getClassifiedEmployersByTypeTest() throws Exception {
        // set up
        String classifiedEmployersByTypeEndpoint = EMPLOYERS_ENDPOINT + "/classified/0?type=academy";

        Mockito.doReturn(createFakePage()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersByTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals(EXPECTED_RESPONSE, result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

    }

    // Test case: Requests a page of classified employers by type and tests with invalid type parameter and checks the response
    @Test
    public void getClassifiedByTypeEmployersWithInvalidTypeParameter() throws Exception {
        // set up
        String classifiedEmployersEndpointWrongParameter = EMPLOYERS_ENDPOINT + "/classified/0?type=k";

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersEndpointWrongParameter, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        assertEquals(expectedStatus, result.getResponse().getStatus());

        assertEquals("{\"details\":\"uri=alumni/employer/classifiedByType/0\",\"message\":\"Type must be one of employer types\"}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(0))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }

    // Test case: Set employer type with invalid type parameter and checks the response.
    @Test
    public void setEmployerTypeInvalidTypeParameterTest() throws Exception {
        // set up
        String setEmployerTypeWrongparameterEndpoint = EMPLOYERS_ENDPOINT + "?type=k&linkedinId=";

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setEmployerTypeWrongparameterEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        assertEquals(expectedStatus, result.getResponse().getStatus());

        assertEquals("{\"details\":\"uri=/employer\",\"message\":\"Type must be one of employer types\"}",
                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(0))
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));
    }

    // Test case: Requests a page of unclassified employers and tests a successfully return. Checks the response and also call
    // the facade to get unclassified employers
    @Test
    public void getUnclassifiedEmployersTest() throws Exception {
        // set up
        String unclassifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/unclassified/0";

        Mockito.doReturn(createFakePage()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, unclassifiedEmployersEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Assert.assertEquals(EXPECTED_RESPONSE, result.getResponse().getContentAsString());
//        Assert.assertEquals("{\"content\":[],\"totalPages\":0,\"totalElements\":0,\"last\":true,\"sort\":null," +
//                        "\"numberOfElements\":0,\"first\":true,\"size\":10,\"number\":0}",
//                result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(),Mockito.anyInt());
    }

    // Test case: Delete an employer type and test successfully return. Also call the facade
    // to delete employer type
    @Test
    public void deleteEmployerTypeTest() throws Exception {
        // set up
        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=";

        Mockito.doNothing().when(this.facade).setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, deleteEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Delete an employer type with invalid parameter. Checks the response and also call the facade
    // to delete employer type
    @Test
    public void deleteEmployerTypeInvalidParameter() throws Exception {
        // set up
        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=";

        Mockito.doThrow(new InvalidParameterException()).when(this.facade)
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, deleteEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Assert.assertEquals("{\"details\":\"uri=/employer\",\"message\":\"Unexpected error.\"}", result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Set an employer type and test successfully return. Also call the
    // facade to set employer type
    @Test
    public void setEmployerTypeTest() throws Exception {
        // set up
        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=";

        Mockito.doNothing().when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.OK.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));

    }

    // Test case: Set an employer type with invalid parameter. Checks the response and also call the facade
    // to set employer type
    @Test
    public void setEmployerTypeInvalidParameter() throws Exception {
        // set up
        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=";

        Mockito.doThrow(new InvalidParameterException()).when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.BAD_REQUEST.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Assert.assertEquals("{\"details\":\"uri=/employer\",\"message\":\"Unexpected error.\"}", result.getResponse().getContentAsString());

        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));
    }

    // Test case: Requests a page of classified employers with unauthorized user. Also call
     // the facade to get classified employers
    @Test
    public void getClassifiedEmployersUnauthorizedExceptionTest() throws Exception {
        // set up
        String classifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/classified/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }

    // Test case: Requests a page of classified employers by type with unauthorized user. Also call the
    // facade to get classified employers by type.
    @Test
    public void getClassifiedEmployersByTypeUnauthorizedExceptionTest() throws Exception {
        // set up
        String classifiedEmployersByTypeEndpoint = EMPLOYERS_ENDPOINT + "/classified/0?type=academy";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersByTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }

    // Test case: Requests a page of unclassified employers with unauthorized user. Also call the
    // facade to get unclassified employers.
    @Test
    public void getUnclassifiedEmployersByTypeUnauthorizedExceptionTest() throws Exception {
        // set up
        String unclassifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/unclassified/0";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, unclassifiedEmployersEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Delete an employer type with unauthorized user. Also call the
    // facade to delete an employer type.
    @Test
    public void deleteEmployerTypeUnauthorizedExceptionTest() throws Exception {
        // set up
        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, deleteEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Set employer type with unauthorized user. Also call the
    // facade to set an employer type.
    @Test
    public void setEmployerTypeUnauthorizedExceptionTest() throws Exception {
        // set up
        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=";

        Mockito.doThrow(new UnauthorizedRequestException()).when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.FORBIDDEN.value();

        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));
    }

    // Test case: Requests a page of classified employers with unauthenticated user. Also call the
    // facade to get classified employers.
    @Test
    public void getClassifiedEmployersUnauthenticatedExceptionTest() throws Exception {
        // set up
        String classifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/classified/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }

    // Test case: Requests a page of employers classified by type with unauthenticated user. Also call the
    // facade to get classified employers by type.
    @Test
    public void getClassifiedEmployersByTypeUnauthenticatedExceptionTest() throws Exception {
        // set up
        String classifiedEmployersByTypeEndpoint = EMPLOYERS_ENDPOINT + "/classifiedByType/0?type=academy";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, classifiedEmployersByTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getClassifiedEmployers(Mockito.anyString(), Mockito.any(EmployerType.class), Mockito.anyInt());
    }

    // Test case: Requests a page of unclassified employers with unauthenticated user. Also call the
    // facade to get unclassified employers by type.
    @Test
    public void getUnclassifiedEmployersByTypeUnauthenticatedExceptionTest() throws Exception {
        // set up
        String unclassifiedEmployersEndpoint = EMPLOYERS_ENDPOINT + "/unclassified/0";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.GET, unclassifiedEmployersEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .getUnclassifiedEmployers(Mockito.anyString(), Mockito.anyInt());
    }

    // Test case: Delete employer type with unauthenticated user. Also call the
    // facade to delete an employer type.
    @Test
    public void deleteEmployerTypeUnauthenticatedExceptionTest() throws Exception {
        // set up
        String deleteEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?linkedinId=";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.DELETE, deleteEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        // verify
        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerTypeToUndefined(Mockito.anyString(), Mockito.anyString());
    }

    // Test case: Set employer type with unauthenticated user. Also call the
    // facade to set an employer type.
    @Test
    public void setEmployerTypeUnauthenticatedExceptionTest() throws Exception {
        // set up
        String setEmployerTypeEndpoint = EMPLOYERS_ENDPOINT + "?type=academy&linkedinId=";

        Mockito.doThrow(new UnauthenticatedUserException()).when(this.facade)
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));

        RequestBuilder requestBuilder = createRequestBuilder(HttpMethod.PUT, setEmployerTypeEndpoint, getHttpHeaders(), "");

        // exercise
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        // verify
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        Assert.assertEquals(expectedStatus, result.getResponse().getStatus());

        Mockito.verify(this.facade, Mockito.times(1))
                .setEmployerType(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmployerType.class));
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
