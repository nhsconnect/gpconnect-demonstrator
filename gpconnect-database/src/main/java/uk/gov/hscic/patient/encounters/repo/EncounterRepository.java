package uk.gov.hscic.patient.encounters.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;

public interface EncounterRepository extends JpaRepository<EncounterEntity, Long> {

    List<EncounterEntity> findBynhsNumber(Long patientNHSNumber);
    List<EncounterEntity> findBynhsNumberAndSectionDateAfter(Long patientNHSNumber, Date startDate);
    List<EncounterEntity> findBynhsNumberAndSectionDateBefore(Long patientNHSNumber, Date endDate);
    List<EncounterEntity> findBynhsNumberAndSectionDateAfterAndSectionDateBefore(Long patientNHSNumber, Date startDate, Date endDate);
    
}
