package org.rippleosi.search.reports.graph.search;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.reports.graph.model.ReportGraphDemographicSummary;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;

public interface ReportGraphSearch extends Repository {

    ReportGraphDemographicSummary findPatientDemographicsByQuery(ReportGraphQuery graphQuery);
}
