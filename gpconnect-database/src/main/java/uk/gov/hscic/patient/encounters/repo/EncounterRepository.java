package uk.gov.hscic.patient.encounters.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;

public interface EncounterRepository extends JpaRepository<EncounterEntity, Long> {

    List<EncounterEntity> findBynhsNumberOrderBySectionDateDesc(Long patientNHSNumber);
    List<EncounterEntity> findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long patientNHSNumber, Date startDate);
    List<EncounterEntity> findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long patientNHSNumber, Date endDate);
    List<EncounterEntity> findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(Long patientNHSNumber, Date startDate, Date endDate);
    
}
