package org.rippleosi.search.reports.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractC4HReportingService;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRReportTableSearch extends AbstractC4HReportingService implements ReportTableSearch {

    @Override
    public List<String> findAllPatientsByQuery(ReportTableQuery tableQuery) {
        ReportTableQueryStrategy queryStrategy = new ReportTableQueryStrategy(tableQuery);
        return findTableData(queryStrategy, queryStrategy.getUriVariables());
    }
}
