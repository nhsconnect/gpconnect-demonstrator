package org.rippleosi.search.reports.graph.search;

import org.rippleosi.common.service.AbstractC4HReportingService;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRReportGraphSearch extends AbstractC4HReportingService implements ReportGraphSearch {

    @Autowired
    private ReportGraphQueryStrategy queryStrategy;

    @Override
    public ReportGraphResults findPatientDemographicsByQuery(ReportGraphQuery graphQuery) {
        queryStrategy.setGraphQuery(graphQuery);
        return findGraphData(queryStrategy);
    }
}
