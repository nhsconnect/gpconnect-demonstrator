package uk.gov.hscic.patient.problems.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.gov.hscic.patient.problems.model.ProblemEntity;

public interface ProblemRepository extends JpaRepository<ProblemEntity, Long> {
	
    List<ProblemEntity> findBynhsNumberOrderByStartDateDesc(String nhsNumber);
    List<ProblemEntity> findBynhsNumberAndStartDateAfterOrderByStartDateDesc(String nhsNumber, Date endDate);
    List<ProblemEntity> findBynhsNumberAndStartDateBeforeOrderByStartDateDesc(String nhsNumber, Date startDate);
    List<ProblemEntity> findBynhsNumberAndStartDateAfterAndStartDateBeforeOrderByStartDateDesc(String nhsNumber, Date startDate, Date endDate);

}
