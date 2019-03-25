package uk.gov.hscic.patient.problems.search;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.repo.ProblemRepository;

@Service
public class ProblemSearch {

    @Autowired
    private ProblemRepository problemRepository;

    public List<ProblemEntity> findProblems(final String nhsNumber, Date fromDate, Date toDate) {
    	if (fromDate != null && toDate != null) {
            return problemRepository.findBynhsNumberAndStartDateAfterAndStartDateBeforeOrderByStartDateDesc(nhsNumber, fromDate, toDate);
        } else if (fromDate != null) {
            return problemRepository.findBynhsNumberAndStartDateAfterOrderByStartDateDesc(nhsNumber, fromDate);
        } else if (toDate != null) {
            return problemRepository.findBynhsNumberAndStartDateBeforeOrderByStartDateDesc(nhsNumber, toDate);
        } else {
        	return findProblems(nhsNumber);
        }
    }
    
    public List<ProblemEntity> findProblems(final String nhsNumber) {
    	    	
    	return problemRepository.findBynhsNumberOrderByStartDateDesc(nhsNumber);
    	
    }
}
