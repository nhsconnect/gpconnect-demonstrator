package uk.gov.hscic.patient.allergies.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;

public interface AllergyRepository extends JpaRepository<AllergyEntity, Long> {
    List<AllergyEntity> findByNhsNumber(String patientId);
}
