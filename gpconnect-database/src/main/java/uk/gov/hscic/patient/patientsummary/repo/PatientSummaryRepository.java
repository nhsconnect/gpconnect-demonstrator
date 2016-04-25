package uk.gov.hscic.patient.patientsummary.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryEntity;

public interface PatientSummaryRepository extends JpaRepository<PatientSummaryEntity, Long> {

}
