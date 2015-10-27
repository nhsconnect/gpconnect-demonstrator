/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.common.service;

import java.util.Map;

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
public class C4HReportingRequestProxy {

    @Value("${c4hOpenEHR.address}")
    private String openEhrAddress;

    @Value("${c4hOpenEHR.user}")
    private String openEhrUsername;

    @Value("${c4hOpenEHR.password}")
    private String openEhrPassword;

    public <T> ResponseEntity<T> getWithoutSession(String uri, Class<T> cls, Map<String, String> uriVars) {
        HttpEntity request = buildRequest(null);

        return restTemplate().exchange(uri, HttpMethod.GET, request, cls, uriVars);
    }

     public <T> ResponseEntity<T> getWithoutSession(String uri, Class<T> cls, Object rawBody) {
        String jsonBody = convertToJson(rawBody);
        HttpEntity request = buildRequest(jsonBody);

        return restTemplate().exchange(uri, HttpMethod.GET, request, cls);
    }

    public <T> ResponseEntity<T> postWithoutSession(String uri, Class<T> cls, Object rawBody) {
        String jsonBody = convertToJson(rawBody);
        HttpEntity request = buildRequest(jsonBody);

        return restTemplate().exchange(uri, HttpMethod.POST, request, cls);
    }

    private HttpEntity<String> buildRequest(String body) {
        String credentials = openEhrUsername + ":" + openEhrPassword;
        byte[] base64 = Base64.encodeBase64(credentials.getBytes());
        String encoded = new String(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(body, headers);
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

    private RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }
}
