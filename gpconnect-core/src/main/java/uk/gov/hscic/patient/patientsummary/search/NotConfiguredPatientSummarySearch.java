package uk.gov.hscic.patient.patientsummary.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;

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
    public List<PatientSummaryListHTML> findAllPatientSummaryHTMLTables(String patientId) {
        throw ConfigurationException.unimplementedTransaction(PatientSummarySearch.class);
    }

}
