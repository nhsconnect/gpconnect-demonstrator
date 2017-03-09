package uk.gov.hscic.patient.problems.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.repo.ProblemRepository;

@Service
public class ProblemSearch {

    @Autowired
    private ProblemRepository problemRepository;

    public List<ProblemListHTML> findAllProblemHTMLTables(final String patientId) {
       List<ProblemListHTML> problemsList = new ArrayList<>();

       for (ProblemEntity problemEntity : problemRepository.findBynhsNumber(patientId)) {
           ProblemListHTML problemData = new ProblemListHTML();
           problemData.setActiveOrInactive(problemEntity.getActiveOrInactive());
           problemData.setStartDate(problemEntity.getStartDate());
           problemData.setEndDate(problemEntity.getEndDate());
           problemData.setEntry(problemEntity.getEntry());
           problemData.setSignificance(problemEntity.getSignificance());
           problemData.setDetails(problemEntity.getDetails());

           problemsList.add(problemData);
       }

       return problemsList;
    }
}
