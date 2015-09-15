package org.rippleosi.common.service;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.LazyMap;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.UpdateFailedException;
import org.rippleosi.common.model.ActionRestResponse;
import org.rippleosi.common.model.EhrResponse;
import org.rippleosi.common.model.QueryResponse;
import org.rippleosi.common.search.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 */
public abstract class AbstractOpenEhrService implements Repository {

    @Value("${repository.config.openehr:1000}")
    private int priority;

    @Value("${openehr.address}")
    private String openEhrAddress;

    @Value("${openehr.subjectNamespace}")
    private String openEhrSubjectNamespace;

    @Autowired
    private RequestProxy requestProxy;

    private final Map<String, String> idCache = Collections.synchronizedMap(LazyMap.lazyMap(new LRUMap<String, String>(), new EhrIdLookup()));

    @Override
    public String getSource() {
        return "openehr";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <T> T findData(QueryStrategy<T> queryStrategy) {

        String ehrId = findEhrIdByNHSNumber(queryStrategy.getPatientId());
        String query = queryStrategy.getQuery(ehrId);

        ResponseEntity<QueryResponse> response = requestProxy.getWithSession(getQueryURI(query), QueryResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DataNotFoundException("OpenEHR query returned with status code " + response.getStatusCode());
        }

        return queryStrategy.transform(response.getBody().getResultSet());
    }

    protected void createData(CreateStrategy<?> createStrategy) {

        String patientId = createStrategy.getPatientId();
        String ehrId = findEhrIdByNHSNumber(patientId);
        String template = createStrategy.getTemplate();
        Map<String,String> content = createStrategy.getContent();

        String uri = getCreateURI(template, ehrId);

        ResponseEntity<ActionRestResponse> response = requestProxy.postWithSession(uri, ActionRestResponse.class, content);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new UpdateFailedException("Could not create " + template + " for patient " + patientId);
        }
    }

    protected void updateData(UpdateStrategy<?> updateStrategy) {

        String patientId = updateStrategy.getPatientId();
        String ehrId = findEhrIdByNHSNumber(patientId);
        String compositionId = updateStrategy.getCompositionId();
        String template = updateStrategy.getTemplate();
        Map<String,String> content = updateStrategy.getContent();

        String uri = getUpdateURI(compositionId, template, ehrId);

        ResponseEntity<ActionRestResponse> response = requestProxy.putWithSession(uri, ActionRestResponse.class, content);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new UpdateFailedException("Could not update " + template + " (" + compositionId + ") for patient " + patientId);
        }
    }

    private String findEhrIdByNHSNumber(String nhsNumber) {
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

    private class EhrIdLookup implements Transformer<String, String> {

        @Override
        public String transform(String nhsNumber) {
            ResponseEntity<EhrResponse> response = requestProxy.getWithSession(getEhrIdUri(nhsNumber), EhrResponse.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new DataNotFoundException("OpenEHR query returned with status code " + response.getStatusCode());
            }

            return response.getBody().getEhrId();
        }

        private String getEhrIdUri(String nhsNumber) {
            UriComponents components = UriComponentsBuilder
                                        .fromHttpUrl(openEhrAddress + "/ehr")
                                        .queryParam("subjectNamespace", openEhrSubjectNamespace)
                                        .queryParam("subjectId", nhsNumber)
                                        .build();
            return components.toUriString();
        }
    }
}
