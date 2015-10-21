package org.rippleosi.search.patient.stats.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.PageableTableQuery;
import org.rippleosi.search.patient.stats.model.SearchTableResults;

public class NotConfiguredPatientStatsSearch implements PatientStatsSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public SearchTableResults findAssociatedPatientData(PageableTableQuery tableQuery,
                                                        List<PatientSummary> patientSummaries) {
        throw ConfigurationException.unimplementedTransaction(PatientStatsSearch.class);
    }
}
