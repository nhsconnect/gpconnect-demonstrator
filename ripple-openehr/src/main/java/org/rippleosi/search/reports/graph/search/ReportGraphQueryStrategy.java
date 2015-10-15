package org.rippleosi.search.reports.graph.search;

import org.rippleosi.common.service.C4HUriQueryStrategy;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ReportGraphQueryStrategy implements C4HUriQueryStrategy<ReportGraphResults, ReportGraphResults> {

    @Value("${c4hOpenEHR.address}")
    private String c4hOpenEHRAddress;

    private ReportGraphQuery graphQuery;

    public void setGraphQuery(ReportGraphQuery graphQuery) {
        this.graphQuery = graphQuery;
    }

    @Override
    public UriComponents getQueryUriComponents() {
        // TODO - use graphQuery and remove hard coded ehrID, template, and SNOMED CT code once implemented on C4H
        return UriComponentsBuilder
            .fromHttpUrl(c4hOpenEHRAddress + "/view")
            .path("/e98be9e4-4e09-4f0b-9fce-6b1b83fc638b" + "/ProblemAgeBands")
            .queryParam("targetCompositions", "Problem list")
            .queryParam("targetCodes", "22298006")
            .queryParam("minAge", "0")
            .queryParam("maxAge", "200")
            .build();
    }

    @Override
    public String getRequestBody() {
        return null;
    }

    @Override
    public ReportGraphResults transform(ReportGraphResults resultSet) {
        resultSet.setSource("c4hOpenEHR");
        return resultSet;
    }
}
