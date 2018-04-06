package uk.gov.hscic.patient.allergies;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AllergyRepository extends JpaRepository<AllergyEntity, Long> {
    List<AllergyEntity> findByNhsNumber(String patientId);

    @Query(value="SELECT DISTINCT a.details FROM gpconnect.allergies as a, gpconnect.patients as p WHERE  p.nhs_number = a.nhsNumber AND p.nhs_number =?1", nativeQuery = true)
    List<String> findDistinctAllergiesByNhsNumber(String patientId);
}
