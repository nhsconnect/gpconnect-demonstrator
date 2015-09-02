package org.rippleosi.common.service;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.rippleosi.common.exception.InvalidDataException;
import org.rippleosi.common.model.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultRequestProxy implements RequestProxy {

    @Value("${openehr.address}")
    private String openEhrAddress;

    @Value("${openehr.user}")
    private String openEhrUsername;

    @Value("${openehr.password}")
    private String openEhrPassword;

    private String sessionId;

    @Override
    public <T> ResponseEntity<T> getWithSession(String uri, Class<T> cls) {

        HttpEntity<String> request = buildRequestWithSession();

        return restTemplate().exchange(uri, HttpMethod.GET, request, cls);
    }

    @Override
    public <T> ResponseEntity<T> postWithSession(String uri, Class<T> cls, Object body) {

        String json = convertToJson(body);

        HttpEntity<String> request = buildRequestWithSession(json);

        return restTemplate().exchange(uri, HttpMethod.POST, request, cls);
    }

    @Override
    public <T> ResponseEntity<T> putWithSession(String uri, Class<T> cls, Object body) {

        String json = convertToJson(body);

        HttpEntity<String> request = buildRequestWithSession(json);

        return restTemplate().exchange(uri, HttpMethod.PUT, request, cls);
    }

    private HttpEntity<String> buildRequestWithSession() {
        return buildRequestWithSession(null);
    }

    private HttpEntity<String> buildRequestWithSession(String body) {
        return buildRequest(body, Collections.singletonMap("Ehr-Session", session()));
    }

    private HttpEntity<String> buildRequestWithoutSession() {
        return buildRequest(null, Collections.<String, String>emptyMap());
    }

    private HttpEntity<String> buildRequest(String body, Map<String, String> additionalHeaders) {

        String credentials = openEhrUsername + ":" + openEhrPassword;
        byte[] base64 = Base64.encodeBase64(credentials.getBytes());
        String encoded = new String(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }

        return new HttpEntity<>(body, headers);
    }

    private String session() {

        if (sessionId == null) {
            HttpEntity<String> request = buildRequestWithoutSession();

            ResponseEntity<LoginResponse> response = restTemplate().exchange(sessionUri(), HttpMethod.POST, request, LoginResponse.class);

            sessionId = response.getBody().getSessionId();
        }

        return sessionId;
    }

    private RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    private String sessionUri() {
        UriComponents components = UriComponentsBuilder
                                    .fromHttpUrl(openEhrAddress + "/session")
                                    .queryParam("username", openEhrUsername)
                                    .queryParam("password", openEhrPassword)
                                    .build();
        return components.toUriString();
    }

    private String convertToJson(Object body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException ex) {
            throw new InvalidDataException(ex.getMessage(), ex);
        }
    }
}
