package uk.gov.hscic.medication.order.search;

import java.util.List;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.order.model.MedicationOrderDetails;

public class NotConfiguredMedicationOrderSearch implements MedicationOrderSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public MedicationOrderDetails findMedicationOrderByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(MedicationOrderSearch.class);
    }

    @Override
    public List<MedicationOrderDetails> findMedicationOrdersForPatient(Long patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationOrderSearch.class);
    }

}
