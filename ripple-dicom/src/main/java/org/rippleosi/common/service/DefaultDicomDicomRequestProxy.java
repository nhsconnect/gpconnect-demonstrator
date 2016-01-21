/*
 *   Copyright 2016 Ripple OSI
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
public class DefaultDicomDicomRequestProxy implements DicomRequestProxy {

    private RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> getWithoutSession(String uri, Class<T> cls) {
        HttpEntity<String> request = buildRequestWithoutSession(null);

        return restTemplate().exchange(uri, HttpMethod.GET, request, cls);
    }

    private HttpEntity<String> buildRequestWithoutSession(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE);

        return new HttpEntity<>(body, headers);
    }

    private RestTemplate restTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        }

        return restTemplate;
    }
}
