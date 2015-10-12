package org.rippleosi.common.service;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.model.C4HRestQueryResponse;
import org.rippleosi.common.repo.Repository;
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
        return "C4HOpenEHR";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <T> T findChartData(C4HReportQueryStrategy<T> queryStrategy) {
        UriComponents uriComponents = queryStrategy.getQueryUriComponents();

        ResponseEntity<ReportGraphResults> response = requestProxy.getWithSession(uriComponents.toUriString(),
                                                                                  ReportGraphResults.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DataNotFoundException("C4H OpenEHR query returned with status code " + response.getStatusCode());
        }

        return queryStrategy.transform(response.getBody());
    }
}
