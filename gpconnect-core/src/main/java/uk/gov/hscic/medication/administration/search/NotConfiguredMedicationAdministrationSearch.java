package uk.gov.hscic.medication.administration.search;

import java.util.List;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.administration.model.MedicationAdministrationDetail;

public class NotConfiguredMedicationAdministrationSearch implements MedicationAdministrationSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public MedicationAdministrationDetail findMedicationAdministrationByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(MedicationAdministrationSearch.class);
    }

    @Override
    public List<MedicationAdministrationDetail> findMedicationAdministrationForPatient(Long patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationAdministrationSearch.class);
    }
}
