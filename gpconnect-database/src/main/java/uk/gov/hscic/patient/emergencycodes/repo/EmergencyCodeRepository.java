package uk.gov.hscic.patient.emergencycodes.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.emergencycodes.model.EmergencyCodeEntity;

public interface EmergencyCodeRepository extends JpaRepository<EmergencyCodeEntity, Long> {

    List<EmergencyCodeEntity> findBynhsNumberOrderByEmergencyCodeDateDesc(String patientNHSNumber);
}
