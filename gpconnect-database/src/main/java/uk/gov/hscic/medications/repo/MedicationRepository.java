package uk.gov.hscic.medications.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;

public interface MedicationRepository extends JpaRepository<PatientMedicationHtmlEntity, Long> {

}
