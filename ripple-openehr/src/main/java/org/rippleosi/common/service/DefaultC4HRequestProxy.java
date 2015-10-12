package org.rippleosi.common.service;

import org.apache.commons.codec.binary.Base64;
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

    public <T> ResponseEntity<T> getWithSession(String uri, Class<T> cls) {
        String credentials = openEhrUsername + ":" + openEhrPassword;
        byte[] base64 = Base64.encodeBase64(credentials.getBytes());
        String encoded = new String(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        HttpEntity<T> request = new HttpEntity<>(headers);

        return new RestTemplate(requestFactory).exchange(uri, HttpMethod.GET, request, cls);
    }
}
