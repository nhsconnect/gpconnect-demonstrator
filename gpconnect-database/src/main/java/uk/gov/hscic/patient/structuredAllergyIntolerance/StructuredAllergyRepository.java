package uk.gov.hscic.patient.structuredAllergyIntolerance;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StructuredAllergyRepository extends JpaRepository<StructuredAllergyIntoleranceEntity, Long> {
	 List<StructuredAllergyIntoleranceEntity> findByNhsNumber(String patientId);
}
