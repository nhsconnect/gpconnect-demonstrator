package uk.gov.hscic.patient.medication.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;

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
    public List<MedicationListHTML> findMedicationHTMLTables(String patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationSearch.class);
    }
}
