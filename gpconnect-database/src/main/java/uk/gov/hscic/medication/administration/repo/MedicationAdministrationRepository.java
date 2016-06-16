package uk.gov.hscic.medication.administration.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.medication.administration.model.MedicationAdministrationEntity;

public interface MedicationAdministrationRepository  extends JpaRepository<MedicationAdministrationEntity, Long> {
    
    List<MedicationAdministrationEntity> findByPatientId(Long patient_id);
}

