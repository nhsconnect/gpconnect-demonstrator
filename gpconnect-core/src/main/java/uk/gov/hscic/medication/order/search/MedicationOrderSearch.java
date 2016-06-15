package uk.gov.hscic.medication.order.search;

import java.util.List;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.medication.order.model.MedicationOrderDetails;

public interface MedicationOrderSearch extends Repository {
    
    MedicationOrderDetails findMedicationOrderByID(Long id);
    
    List<MedicationOrderDetails> findMedicationOrdersForPatient(Long patientId);
    
}
