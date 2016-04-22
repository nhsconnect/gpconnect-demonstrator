package uk.gov.hscic.patient.problems.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.problems.model.ProblemEntity;

public interface ProblemRepository extends JpaRepository<ProblemEntity, Long> {

}
