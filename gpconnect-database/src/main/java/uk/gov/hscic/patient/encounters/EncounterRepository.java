package uk.gov.hscic.patient.encounters;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterRepository extends JpaRepository<EncounterEntity, Long> {
    List<EncounterEntity> findBynhsNumberOrderBySectionDateDesc(String patientNHSNumber);
    List<EncounterEntity> findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(String patientNHSNumber, Date startDate);
    List<EncounterEntity> findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(String patientNHSNumber, Date endDate);
    List<EncounterEntity> findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(String patientNHSNumber, Date startDate, Date endDate);
}
