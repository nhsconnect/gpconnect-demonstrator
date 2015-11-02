/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.LazyMap;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.UpdateFailedException;
import org.rippleosi.common.model.ActionRestResponse;
import org.rippleosi.common.model.EhrResponse;
import org.rippleosi.common.model.QueryResponse;
import org.rippleosi.common.repo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 */
public abstract class AbstractOpenEhrService implements Repository {

    @Value("${repository.config.c4hOpenEHR:1000}")
    private int priority;

    @Value("${c4hOpenEHR.address}")
    private String openEhrAddress;

    @Value("${c4hOpenEHR.subjectNamespace}")
    private String openEhrSubjectNamespace;

    @Autowired
    private RequestProxy requestProxy;

    private final Map<String, String> idCache = Collections.synchronizedMap(LazyMap.lazyMap(new LRUMap<>(), new EhrIdLookup()));

    @Override
    public String getSource() {
        return "openehr";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <T> T findData(QueryStrategy<T> queryStrategy) {

        String query = queryStrategy.getQuery(openEhrSubjectNamespace, queryStrategy.getPatientId());

        ResponseEntity<QueryResponse> response = requestProxy.getWithoutSession(getQueryURI(query), QueryResponse.class);

        List<Map<String, Object>> results = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            results = response.getBody().getResultSet();
        }

        return queryStrategy.transform(results);
    }

    protected void createData(CreateStrategy createStrategy) {

        String patientId = createStrategy.getPatientId();
        String ehrId = findEhrIdByNHSNumber(patientId);
        String template = createStrategy.getTemplate();
        Map<String, Object> content = createStrategy.getContent();

        String uri = getCreateURI(template, ehrId);

        ResponseEntity<ActionRestResponse> response = requestProxy.postWithoutSession(uri, ActionRestResponse.class, content);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new UpdateFailedException("Could not create " + template + " for patient " + patientId);
        }
    }

    protected void updateData(UpdateStrategy updateStrategy) {

        String patientId = updateStrategy.getPatientId();
        String ehrId = findEhrIdByNHSNumber(patientId);
        String compositionId = updateStrategy.getCompositionId();
        String template = updateStrategy.getTemplate();
        Map<String, Object> content = updateStrategy.getContent();

        String uri = getUpdateURI(compositionId, template, ehrId);

        ResponseEntity<ActionRestResponse> response = requestProxy.putWithoutSession(uri, ActionRestResponse.class, content);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new UpdateFailedException("Could not update " + template + " (" + compositionId + ") for patient " + patientId);
        }
    }

    public String findEhrIdByNHSNumber(String nhsNumber) {
        return idCache.get(nhsNumber);
    }

    private String getQueryURI(String query) {
        UriComponents components = UriComponentsBuilder
                                    .fromHttpUrl(openEhrAddress + "/query")
                                    .queryParam("aql", query)
                                    .build();
        return components.toUriString();
    }

    private String getCreateURI(String template, String ehrId) {
        UriComponents components = UriComponentsBuilder
                                    .fromHttpUrl(openEhrAddress + "/composition")
                                    .queryParam("templateId", template)
                                    .queryParam("ehrId", ehrId)
                                    .queryParam("format", "FLAT")
                                    .build();

        return components.toUriString();
    }

    private String getUpdateURI(String compositionId, String template, String ehrId) {
        UriComponents components = UriComponentsBuilder
                                    .fromHttpUrl(openEhrAddress + "/composition/" + compositionId)
                                    .queryParam("templateId", template)
                                    .queryParam("ehrId", ehrId)
                                    .queryParam("format", "FLAT")
                                    .build();

        return components.toUriString();
    }

    public class EhrIdLookup implements Transformer<String, String> {

        @Override
        public String transform(String nhsNumber) {
            ResponseEntity<EhrResponse> response = requestProxy.getWithoutSession(getEhrIdUri(nhsNumber), EhrResponse.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new DataNotFoundException("OpenEHR query returned with status code " + response.getStatusCode());
            }

            return response.getBody().getEhrId();
        }

        private String getEhrIdUri(String nhsNumber) {
            UriComponents components = UriComponentsBuilder
                                        .fromHttpUrl(openEhrAddress + "/ehr")
                                        .queryParam("subjectId", nhsNumber)
                                        .queryParam("subjectNamespace", openEhrSubjectNamespace)
                                        .build();
            return components.toUriString();
        }
    }
}
