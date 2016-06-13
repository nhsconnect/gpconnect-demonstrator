package uk.gov.hscic.medication.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.model.MedicationDetails;
import uk.gov.hscic.medication.model.PatientMedicationHTML;

public class NotConfiguredMedicationSearch implements MedicationSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<PatientMedicationHTML> findPatientMedicationHTML(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationSearch.class);
    }
    
    @Override
    public MedicationDetails findMedicationByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(MedicationSearch.class);
    }
}
