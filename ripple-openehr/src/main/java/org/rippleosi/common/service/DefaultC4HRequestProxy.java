package org.rippleosi.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.rippleosi.common.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultC4HRequestProxy {

    @Value("${c4hOpenEHR.user}")
    private String openEhrUsername;

    @Value("${c4hOpenEHR.password}")
    private String openEhrPassword;

    public <T> ResponseEntity<T> getWithSession(String uri, Class<T> cls, Object rawBody) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        String jsonBody = convertToJson(rawBody);
        HttpEntity request = buildRequest(jsonBody);

        return new RestTemplate(requestFactory).exchange(uri, HttpMethod.GET, request, cls);
    }

    public <T> ResponseEntity<T> postWithSession(String uri, Class<T> cls, Object rawBody) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        String jsonBody = convertToJson(rawBody);
        HttpEntity request = buildRequest(jsonBody);

        return new RestTemplate(requestFactory).exchange(uri, HttpMethod.POST, request, cls);
    }

    private String convertToJson(Object body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(body);
        }
        catch (JsonProcessingException ex) {
            throw new InvalidDataException(ex.getMessage(), ex);
        }
    }

    private HttpEntity buildRequest(Object body) {
        String credentials = openEhrUsername + ":" + openEhrPassword;
        byte[] base64 = Base64.encodeBase64(credentials.getBytes());
        String encoded = new String(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(body, headers);
    }
}
