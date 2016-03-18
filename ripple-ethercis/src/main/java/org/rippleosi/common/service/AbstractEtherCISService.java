/*
 *  Copyright 2015 Ripple OSI
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
 *
 */
package org.rippleosi.common.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.LazyMap;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.UpdateFailedException;
import org.rippleosi.common.model.EtherCISActionRestResponse;
import org.rippleosi.common.model.EtherCISEHRResponse;
import org.rippleosi.common.model.EtherCISQueryResponse;
import org.rippleosi.common.model.EtherCISSessionResponse;
import org.rippleosi.common.repo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class AbstractEtherCISService implements Repository {

    @Value("${repository.config.etherCIS:900}")
    private int priority;

    @Value("${etherCIS.address}")
    private String etherCISAddress;

    @Value("${etherCIS.subjectNamespace}")
    private String etherCISSubjectNamespace;

    @Value("${etherCIS.user}")
    private String etherCISUser;

    @Value("${etherCIS.password}")
    private String etherCISPassword;

    @Autowired
    private EtherCISRequestProxy requestProxy;

    private final Map<String, String> idCache = Collections.synchronizedMap(LazyMap.lazyMap(new LRUMap<>(), new EtherCISEhrIdLookup()));

    private String secretSessionId;

    @Override
    public String getSource() {
        return "etherCIS";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <T> T findData(EtherCISQueryStrategy<T> queryStrategy) {

        String patientId = queryStrategy.getPatientId();

        String query = queryStrategy.getQuery(etherCISSubjectNamespace, findEhrIdByNHSNumber(patientId));

        String queryURI = getQueryURI(query);

        ResponseEntity<EtherCISQueryResponse> response = requestProxy.getWithSession(queryURI,
                                                                                     EtherCISQueryResponse.class,
                                                                                     sessionId());

        List<Map<String, Object>> results = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            results = response.getBody().getResultSet();
        }

        return queryStrategy.transform(results);
    }

    protected void createData(EtherCISCreateStrategy createStrategy) {

        String patientId = createStrategy.getPatientId();
        String ehrId = findEhrIdByNHSNumber(patientId);
        String template = createStrategy.getTemplate();
        Map<String, Object> content = createStrategy.getContent();

        String uri = getCreateURI(template, ehrId);

        ResponseEntity<EtherCISActionRestResponse> response = requestProxy.postWithSession(uri,
                                                                                           EtherCISActionRestResponse.class,
                                                                                           sessionId(),
                                                                                           content);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new UpdateFailedException("Could not create " + template + " for patient " + patientId);
        }
    }

    protected void updateData(EtherCISUpdateStrategy updateStrategy) {

        String patientId = updateStrategy.getPatientId();
        String ehrId = findEhrIdByNHSNumber(patientId);
        String compositionId = updateStrategy.getCompositionId();
        String template = updateStrategy.getTemplate();
        Map<String, Object> content = updateStrategy.getContent();

        String uri = getUpdateURI(compositionId, template, ehrId);

        ResponseEntity<EtherCISActionRestResponse> response = requestProxy.putWithSession(uri,
                                                                                          EtherCISActionRestResponse.class,
                                                                                          sessionId(),
                                                                                          content);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new UpdateFailedException("Could not update " + template + " (" + compositionId + ") for patient " + patientId);
        }
    }

    protected String sessionId() {
        if (secretSessionId == null) {
            createSession();
        }

        return secretSessionId;
    }

    private void createSession() {
        ResponseEntity<EtherCISSessionResponse> response = requestProxy.createSession(getEhrSessionIdUri(),
                                                                                      EtherCISSessionResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DataNotFoundException("Could not create session. Query returned status code: " + response.getStatusCode());
        }

        secretSessionId = response.getBody().getSessionId();
    }

    public String findEhrIdByNHSNumber(String nhsNumber) {
        return idCache.get(nhsNumber);
    }

    private String getQueryURI(String query) {
        query = encodeQueryParameter(query);

        return UriComponentsBuilder
            .fromHttpUrl(etherCISAddress + "/query")
            .queryParam("sql", query)
            .build()
            .toUriString();
    }

    private String encodeQueryParameter(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8")
                             .replace("+", "%20");
        }
        catch (UnsupportedEncodingException e) {
            return query;
        }
    }

    private String getCreateURI(String template, String ehrId) {
        return UriComponentsBuilder
            .fromHttpUrl(etherCISAddress + "/composition")
            .queryParam("templateId", template)
            .queryParam("ehrId", ehrId)
            .queryParam("format", "FLAT")
            .build()
            .toUriString();
    }

    private String getUpdateURI(String compositionId, String template, String ehrId) {
        return UriComponentsBuilder
            .fromHttpUrl(etherCISAddress + "/composition")
            .queryParam("uid", compositionId)
            .queryParam("templateId", template)
            .queryParam("ehrId", ehrId)
            .queryParam("format", "FLAT")
            .build()
            .toUriString();
    }

    private String getEhrSessionIdUri() {
        return UriComponentsBuilder
            .fromHttpUrl(etherCISAddress + "/session")
            .queryParam("username", etherCISUser)
            .queryParam("password", etherCISPassword)
            .build()
            .toUriString();
    }

    private class EtherCISEhrIdLookup implements Transformer<String, String> {

        @Override
        public String transform(String nhsNumber) {
            ResponseEntity<EtherCISEHRResponse> response = requestProxy.getWithSession(getEhrIdUri(nhsNumber),
                                                                                       EtherCISEHRResponse.class,
                                                                                       sessionId());

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new DataNotFoundException("EtherCIS query returned with status code " + response.getStatusCode());
            }

            return response.getBody().getEhrId();
        }

        private String getEhrIdUri(String nhsNumber) {
            return UriComponentsBuilder
                .fromHttpUrl(etherCISAddress + "/ehr")
                .queryParam("subjectId", nhsNumber)
                .queryParam("subjectNamespace", etherCISSubjectNamespace)
                .build()
                .toUriString();
        }
    }
}