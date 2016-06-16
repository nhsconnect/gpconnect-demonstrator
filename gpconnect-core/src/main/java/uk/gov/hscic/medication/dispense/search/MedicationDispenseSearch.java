package uk.gov.hscic.medication.dispense.search;

import java.util.List;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseDetail;

public interface MedicationDispenseSearch extends Repository {
    
    MedicationDispenseDetail findMedicationDispenseByID(Long id);
    
    List<MedicationDispenseDetail> findMedicationDispenseForPatient(Long patientId);
    
}
