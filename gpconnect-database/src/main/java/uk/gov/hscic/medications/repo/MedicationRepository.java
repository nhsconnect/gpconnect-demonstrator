package uk.gov.hscic.medications.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.medications.model.MedicationEntity;

public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {
    
}
