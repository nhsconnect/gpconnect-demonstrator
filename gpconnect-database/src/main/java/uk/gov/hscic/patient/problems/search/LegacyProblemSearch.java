package uk.gov.hscic.patient.problems.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.model.HTMLProblemObject;
import uk.gov.hscic.patient.problems.repo.ProblemRepository;

@Service
public class LegacyProblemSearch extends AbstractLegacyService implements ProblemSearch {

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public List<HTMLProblemObject> findAllProblemHTMLTables(final String patientId) {

       List<ProblemEntity> items = problemRepository.findBynhsNumber(patientId);
       List<HTMLProblemObject> problemsList = new ArrayList<>();
       
       for(int i = 0 ; i < items.size(); i++ ){
           HTMLProblemObject problemData = new HTMLProblemObject();
           problemData.setActiveOrInactive(items.get(i).getActiveOrInactive());
           problemData.setStartDate(items.get(i).getStartDate());
           problemData.setEndDate(items.get(i).getEndDate());
           problemData.setEntry(items.get(i).getEntry());
           problemData.setSignificance(items.get(i).getSignificance());
           problemData.setDetails(items.get(i).getDetails());
           
           problemsList.add(problemData);
       }
       
       return problemsList;
    }
}
