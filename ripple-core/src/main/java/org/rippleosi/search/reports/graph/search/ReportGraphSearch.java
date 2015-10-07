package org.rippleosi.search.reports.graph.search;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;

public interface ReportGraphSearch extends Repository {

    ReportGraphResults findPatientDemographicsByQuery(ReportGraphQuery graphQuery);
}
