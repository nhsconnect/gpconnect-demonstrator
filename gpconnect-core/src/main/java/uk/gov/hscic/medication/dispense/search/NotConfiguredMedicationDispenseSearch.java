package uk.gov.hscic.medication.dispense.search;

import java.util.List;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseDetail;

public class NotConfiguredMedicationDispenseSearch implements MedicationDispenseSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public MedicationDispenseDetail findMedicationDispenseByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(MedicationDispenseSearch.class);
    }

    @Override
    public List<MedicationDispenseDetail> findMedicationDispenseForPatient(Long patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationDispenseSearch.class);
    }
}
