package org.rippleosi.search.reports.table.search;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRReportTableSearch extends AbstractOpenEhrService implements ReportTableSearch {

    @Override
    public ReportTableResults findAllPatientsByQuery(ReportTableQuery tableQuery) {
        ReportTableQueryStrategy queryStrategy = new ReportTableQueryStrategy(tableQuery);
        return queryStrategy.transform(null);

        // TODO - delete the invocation of transform() and uncomment the line below
//        return findData(queryStrategy);
    }
}
