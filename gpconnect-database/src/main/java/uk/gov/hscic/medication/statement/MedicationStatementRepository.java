package uk.gov.hscic.medication.statement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationStatementRepository extends JpaRepository<MedicationStatementEntity, Long>{

	public List<MedicationStatementEntity> findByPatientId(Long patientId);
}
