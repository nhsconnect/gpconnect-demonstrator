package uk.gov.hscic.patient.medications.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.medications.model.MedicationHtmlEntity;

public interface MedicationRepository extends JpaRepository<MedicationHtmlEntity, Long> {

}
