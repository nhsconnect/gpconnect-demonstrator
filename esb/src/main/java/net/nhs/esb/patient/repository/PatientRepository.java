package net.nhs.esb.patient.repository;

import net.nhs.esb.patient.model.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 */
public interface PatientRepository extends JpaRepository<PatientDetails, Long> {

}
