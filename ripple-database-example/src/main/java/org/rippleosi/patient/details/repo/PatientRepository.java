package org.rippleosi.patient.details.repo;

import org.rippleosi.patient.details.model.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

}
