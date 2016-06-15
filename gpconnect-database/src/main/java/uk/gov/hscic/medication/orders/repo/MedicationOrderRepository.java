package uk.gov.hscic.medication.orders.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.medication.orders.model.MedicationOrderEntity;

public interface MedicationOrderRepository  extends JpaRepository<MedicationOrderEntity, Long> {
    
    List<MedicationOrderEntity> findByPatientId(Long patient_id);
}
