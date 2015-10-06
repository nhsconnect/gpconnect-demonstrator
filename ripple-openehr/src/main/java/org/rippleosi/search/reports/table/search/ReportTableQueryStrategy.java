package org.rippleosi.search.reports.table.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.search.reports.table.model.ReportTablePatientDetails;
import org.rippleosi.search.reports.table.model.ReportTableQuery;

public class ReportTableQueryStrategy extends AbstractQueryStrategy<List<ReportTablePatientDetails>> {

    private ReportTableQuery tableQuery;

    public ReportTableQueryStrategy(ReportTableQuery tableQuery) {
        super(null);
        this.tableQuery = tableQuery;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        // TODO - use tableQuery object to populate the query
        return null;
    }

    @Override
    public List<ReportTablePatientDetails> transform(List<Map<String, Object>> resultSet) {
        
        return new ArrayList<>();

        // TODO - delete dummy data above and uncomment the line below
//        return CollectionUtils.collect(resultSet, new ReportTablePatientDetailsTransformer(), new ArrayList<>());
    }
}
