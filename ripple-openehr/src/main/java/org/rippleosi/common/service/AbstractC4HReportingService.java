package org.rippleosi.common.service;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.common.model.OpenEHRDatesAndCountsResponse;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;

public abstract class AbstractC4HReportingService implements Repository {

    @Value("${repository.config.c4hOpenEHR:1000}")
    private int priority;

    @Autowired
    private DefaultC4HRequestProxy requestProxy;

    @Override
    public String getSource() {
        return "c4hOpenEHR";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <I, O> O findGraphData(C4HUriQueryStrategy<I, O> queryStrategy) {
        UriComponents uriComponents = queryStrategy.getQueryUriComponents();
        Object requestBody = queryStrategy.getRequestBody();

        ResponseEntity<ReportGraphResults> response = requestProxy.getWithSession(uriComponents.toUriString(),
                                                                                  ReportGraphResults.class,
                                                                                  requestBody);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DataNotFoundException("C4H OpenEHR query returned with status code " + response.getStatusCode());
        }

        return queryStrategy.transform((I) response.getBody());
    }

    protected <I, O> O findTableData(C4HUriQueryStrategy<I, O> queryStrategy) {
        UriComponents uriComponents = queryStrategy.getQueryUriComponents();

        ResponseEntity<OpenEHRDatesAndCountsResponse[]> response = requestProxy.postWithSession(uriComponents.toUriString(),
                                                                                                OpenEHRDatesAndCountsResponse[].class,
                                                                                                queryStrategy.getRequestBody());

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DataNotFoundException("C4H OpenEHR query returned with status code " + response.getStatusCode());
        }

        return queryStrategy.transform((I) response.getBody());
    }
}
