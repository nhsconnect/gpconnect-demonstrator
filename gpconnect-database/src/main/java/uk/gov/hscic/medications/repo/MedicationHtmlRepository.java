package uk.gov.hscic.medications.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;

public interface MedicationHtmlRepository extends JpaRepository<PatientMedicationHtmlEntity, Long> {
    List<PatientMedicationHtmlEntity> findBynhsNumber(String patientId);
}
