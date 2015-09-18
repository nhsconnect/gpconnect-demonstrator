package org.rippleosi.patient.details.repo;

import org.rippleosi.patient.details.model.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

    @Query("SELECT patient FROM PatientEntity patient WHERE patient.nhsNumber=:patientId")
    PatientEntity findByPatientId(@Param("patientId") String patientId);
}
