package uk.gov.hscic.medication.dispense.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseEntity;

public interface MedicationDispenseRepository extends JpaRepository<MedicationDispenseEntity, Long> {
    
    List<MedicationDispenseEntity> findByPatientId(Long patient_id);
}
