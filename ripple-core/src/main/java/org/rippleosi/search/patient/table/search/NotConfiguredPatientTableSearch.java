package org.rippleosi.search.patient.table.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.patient.table.model.PatientTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;

public class NotConfiguredPatientTableSearch implements PatientTableSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public ReportTableResults findAssociatedPatientData(PatientTableQuery tableQuery,
                                                        List<PatientSummary> patientSummaries) {
        throw ConfigurationException.unimplementedTransaction(PatientTableSearch.class);
    }
}
