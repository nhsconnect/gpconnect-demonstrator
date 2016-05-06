package uk.gov.hscic.patient.procedures.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.procedures.model.ProcedureEntity;

public interface ProcedureRepository extends JpaRepository<ProcedureEntity, Long> {

}
