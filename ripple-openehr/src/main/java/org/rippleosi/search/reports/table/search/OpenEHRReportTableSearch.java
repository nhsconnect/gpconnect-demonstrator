package org.rippleosi.search.reports.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.search.reports.table.model.ReportTablePatientDetails;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRReportTableSearch extends AbstractOpenEhrService implements ReportTableSearch {

    @Override
    public List<ReportTablePatientDetails> findAllPatientsByQuery(ReportTableQuery tableQuery) {
        ReportTableQueryStrategy queryStrategy = new ReportTableQueryStrategy(tableQuery);
        return queryStrategy.transform(null);

        // TODO - delete the invocation of transform() and uncomment the line below
//        return findData(queryStrategy);
    }
}
