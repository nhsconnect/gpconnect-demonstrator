package uk.gov.hscic.patient.problems.search;

import uk.gov.hscic.patient.allergies.search.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hscic.common.service.AbstractLegacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.repo.ProblemRepository;

@Service
public class LegacyProblemSearch extends AbstractLegacyService implements ProblemSearch {

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public List<ProblemListHTML> findAllProblemHTMLTables(String patientId) {
        List<ProblemEntity> problemLists = problemRepository.findAll();

        return CollectionUtils.collect(problemLists, new ProblemEntityToListTransformer(), new ArrayList<>());
    }
}
