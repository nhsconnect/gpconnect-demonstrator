package uk.gov.hscic.patient.problems.search;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.repo.ProblemRepository;

@Service
public class LegacyProblemSearch extends AbstractLegacyService implements ProblemSearch {

    @Autowired
    private ProblemRepository problemRepository;

    private final ProblemEntityToListTransformer transformer = new ProblemEntityToListTransformer();

    @Override
    public List<ProblemListHTML> findAllProblemHTMLTables(final String patientId) {

        final ProblemEntity item = problemRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
