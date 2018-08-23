package com.example.resttemplate.encodig;

import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertThat;

@Slf4j
public class RestTemplateEncodingTest {

    private static final String payload = "{\"physicalDocument\":{\"document\":\"4nMVYW0/bSBR+96+YR1oBnbs9v" +
            "JV2W2l326oN0mpV+mCc+LtjXX3XR//fFOQRlPEaFuqj0RDgwxbkDe49eVavu/MD0\",\"mimeType\":\"UTF-8\","
            + ",\"expirationDate\":\"20181212\",\"documentMetadata\":{\"documentType\":\"0\",\"documentSubType\":\"0\"" +
            ",\"description\":\"description\",\"documentTypeMetadata\":[{\"metadataName\":\"\",\"metadataCode\":\"1596_2\"" +
            ",\"metadataValue\":\"ACTIVO\"},{\"metadataName\":\"Póliza\",\"metadataCode\":\"\",\"metadataValue\":\"Activo\"}," +
            "{\"metadataName\":\"\",\"metadataCode\":\"49383_5\",\"metadataValue\":\"Juana Ejemplo\"},{\"metadataName\":\"\"" +
            ",\"metadataCode\":\"41_3\",\"metadataValue\":\"Física\"}]}},\"policyDocument\":{\"LoB\":\"10\"" +
            ",\"LoBDescription\":\"Descripción del LoB\",\"LoBSubunit\":\"10\"}}";
    private static final String REQUEST_ACCENT_FIELD = "Còmlícated REÜÉST Pólicy";
    private static final String RESPONSE_ACCENT_FIELD = "Còmlícated RÉSPÖNSE Pólicy";
    private static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON.toString();
    private static final Options wireMockOptions = WireMockConfiguration.options().jettyStopTimeout(1000L).port(8089);
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockOptions);

    @Before
    public void setUp() {
        stubMyResourceStringPost();
        stubMyResourceFooPost();
    }

    @Test(expected = VerificationException.class)
    public void basicTextRestTemplateSample() {
        HttpEntity<String> entity = getStringAsTextHttpEntity(MediaType.TEXT_PLAIN);

        ResponseEntity<Bar> response = doRestTemplatePost(entity);

        verifyResponse(response);
        verifyStringRequest();
        //Verification should fail because default string encoding is ISO_8859_1, is not specified in the header, and wiremock server considers by default UTF-8
    }

    @Test
    public void basicJsonObjectRestTemplateSample() {
        HttpEntity<Foo> entity = getFooJsonHttpEntity(MediaType.APPLICATION_JSON);

        ResponseEntity<Bar> response = doRestTemplatePost(entity);

        verifyResponse(response);
        verifyFooRequest();
    }

    @Test
    public void utf8AsTextEncoding() {
        HttpEntity<String> entity = getStringAsTextHttpEntity(UTF_8);

        ResponseEntity<Bar> response = doRestTemplatePost(entity);

        verifyResponse(response);
        verifyStringRequest();
    }

    @Test
    public void utf8AsJsonObjectEncoding() {
        HttpEntity<Foo> entity = getFooJsonHttpEntity(UTF_8);

        ResponseEntity<Bar> response = doRestTemplatePost(entity);

        verifyResponse(response);
        verifyFooRequest();
    }

    @Test
    public void iso88591AsTextEncoding() {
        HttpEntity<String> entity = getStringAsTextHttpEntity(ISO_8859_1);

        ResponseEntity<Bar> response = doRestTemplatePost(entity);

        verifyResponse(response);
        verifyStringRequest();
    }

    @Test(expected = VerificationException.class)
    public void iso88591AsJsonObjectEncoding() {
        HttpEntity<Foo> entity = getFooJsonHttpEntity(ISO_8859_1);

        ResponseEntity<Bar> response = doRestTemplatePost(entity);

        verifyResponse(response);
        verifyFooRequest();
        //Verification should fail because header said encoding is ISO_8859_1, but jackson converter always use UTF-8
        // as standard, so, header confuses wiremock and body content, by encoding, does not match
    }

    private ResponseEntity<Bar> doRestTemplatePost(HttpEntity entity) {
        RestTemplate restTemplate = getRestTemplate();
        log.info("START REQUEST");
        ResponseEntity<Bar> response = restTemplate
                .exchange("http://localhost:8089/my/resource/55", HttpMethod.POST, entity, Bar.class);
        log.info("FINISH REQUEST");
        return response;
    }

    private RestTemplate getRestTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        restTemplate.setMessageConverters(getMessageConverters());
        return restTemplate;
    }

    private void verifyResponse(ResponseEntity<Bar> response) {
        assertThat(response.getStatusCode(), org.hamcrest.CoreMatchers.equalTo(HttpStatus.CREATED));
        Bar responseBar = response.getBody();
        assertThat(responseBar.responseValue, org.hamcrest.CoreMatchers.equalTo(RESPONSE_ACCENT_FIELD));
    }

    private void verifyStringRequest() {
        verify(postRequestedFor(urlMatching("/my/resource/[a-z0-9]+"))
                .withRequestBody(equalTo(payload))
                .withHeader("Content-Type", notMatching("application/xml")));
    }

    private void verifyFooRequest() {
        verify(postRequestedFor(urlMatching("/my/resource/[a-z0-9]+"))
                .withRequestBody(matching(".*\"intVal\":4.*"))
                .withRequestBody(matchingJsonPath(String.format("$.[?(@.stringVal == '%s')]", REQUEST_ACCENT_FIELD)))
                .withHeader("Content-Type", notMatching("application/xml")));
    }

    private void stubMyResourceFooPost() {
        stubFor(post(urlEqualTo("/my/resource/55"))
                .withRequestBody(matching(".*\"intVal\":4.*"))
//                .withRequestBody(matchingJsonPath(String.format("$.[?(@.stringVal == '%s')]", REQUEST_ACCENT_FIELD)))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", CONTENT_TYPE_JSON)
                        .withBody(String.format("{\"responseValue\":\"%s\"}", RESPONSE_ACCENT_FIELD))));
    }

    private void stubMyResourceStringPost() {
        stubFor(post(urlEqualTo("/my/resource/55"))
//                .withRequestBody(equalTo(payload))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", CONTENT_TYPE_JSON)
                        .withBody(String.format("{\"responseValue\":\"%s\"}", RESPONSE_ACCENT_FIELD))));
    }

    private HttpEntity<Foo> getFooJsonHttpEntity(Charset charset) {
        return getFooJsonHttpEntity(new MediaType(MediaType.APPLICATION_JSON, charset));
    }

    private HttpEntity<Foo> getFooJsonHttpEntity(MediaType mediaType) {
        Foo resource = new Foo(4, REQUEST_ACCENT_FIELD);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((mediaType));
        return new HttpEntity<>(resource, headers);
    }

    private HttpEntity<String> getStringAsTextHttpEntity(Charset charset) {
        return getStringAsTextHttpEntity(new MediaType(MediaType.TEXT_PLAIN, charset));
    }

    private HttpEntity<String> getStringAsTextHttpEntity(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((mediaType));
        return new HttpEntity<>(payload, headers);
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //You can override the default charset like: jsonHttpMessageConverter.setDefaultCharset(ISO_8859_1);
        //But json output is always UTF-8, it is required by the standard
        //A Jackson related issue https://github.com/FasterXML/jackson-core/issues/222
        //The encoding standard point https://tools.ietf.org/html/rfc8259#section-8.1
        log.info(String.format("JSON DEFAULT CHARSET: %s", jsonHttpMessageConverter.getDefaultCharset()));
        converters.add(jsonHttpMessageConverter);
        //note: if yoy specify the charset inside the StringHttpMessageConverter you can force the encoding for all String body that is not declared in the headers.setContentType
        converters.add(new StringHttpMessageConverter(ISO_8859_1));
        return converters;
    }

}
