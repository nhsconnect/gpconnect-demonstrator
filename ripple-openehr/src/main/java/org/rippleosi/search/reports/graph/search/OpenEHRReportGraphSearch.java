package org.rippleosi.search.reports.graph.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.search.reports.graph.model.ReportGraphPatientSummary;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRReportGraphSearch extends AbstractOpenEhrService implements ReportGraphSearch {

    @Override
    public List<ReportGraphPatientSummary> findAllPatientsByQuery(ReportGraphQuery graphQuery) {
        ReportGraphQueryStrategy queryStrategy = new ReportGraphQueryStrategy(graphQuery);
        return queryStrategy.transform(null);

        // TODO - delete the invocation of transform() and uncomment the line below
//        return findData(queryStrategy);
    }
}
