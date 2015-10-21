package org.rippleosi.search.reports.table.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.search.reports.table.model.ReportTableQuery;

public class NotConfiguredReportTableSearch implements ReportTableSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<String> findAllPatientsByQuery(ReportTableQuery tableQuery) {
        throw ConfigurationException.unimplementedTransaction(ReportTableSearch.class);
    }
}
