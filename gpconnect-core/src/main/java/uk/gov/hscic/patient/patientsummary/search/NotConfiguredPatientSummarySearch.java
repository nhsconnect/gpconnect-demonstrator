package uk.gov.hscic.patient.patientsummary.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHtml;

public class NotConfiguredPatientSummarySearch implements PatientSummarySearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public PatientSummaryListHtml findPatientSummaryListHtml(String patientId) {
        throw ConfigurationException.unimplementedTransaction(PatientSummarySearch.class);
    }
}
