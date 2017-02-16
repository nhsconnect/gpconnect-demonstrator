package uk.gov.hscic.patient.allergies.repo;

import uk.gov.hscic.patient.allergies.model.AllergyEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<AllergyEntity, Long> {
    List<AllergyEntity> findBynhsNumber(String patientId);

    

}
