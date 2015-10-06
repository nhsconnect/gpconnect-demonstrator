package org.rippleosi.search.reports.graph.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.reports.graph.model.ReportGraphPatientSummary;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;

public interface ReportGraphSearch extends Repository {

    List<ReportGraphPatientSummary> findAllPatientsByQuery(ReportGraphQuery graphQuery);
}
