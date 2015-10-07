package org.rippleosi.search.reports.graph.search;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;

public class NotConfiguredReportGraphSearch implements ReportGraphSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public ReportGraphResults findPatientDemographicsByQuery(ReportGraphQuery graphQuery) {
        throw ConfigurationException.unimplementedTransaction(ReportGraphSearch.class);
    }
}
