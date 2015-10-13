package org.rippleosi.search.patient.table.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.SearchTablePatientDetails;
import org.rippleosi.search.reports.table.model.ReportTableResults;

public class PatientTableQueryStrategy extends AbstractQueryStrategy<ReportTableResults> {

    private List<PatientSummary> patientSummaries;

    public PatientTableQueryStrategy(List<PatientSummary> patientSummaries) {
        super(null);
        this.patientSummaries = patientSummaries;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return null;
    }

    @Override
    public ReportTableResults transform(List<Map<String, Object>> resultSet) {
        ReportTableResults results = new ReportTableResults();
        List<SearchTablePatientDetails> details = CollectionUtils.collect(patientSummaries,
                                                                          new PatientTablePatientDetailsTransformer(),
                                                                          new ArrayList<>());
        results.setPatientDetails(details);
        return results;
    }
}
