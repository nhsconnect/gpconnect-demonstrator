package org.rippleosi.patient.problems.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRProblemSearch extends AbstractOpenEhrService implements ProblemSearch {

    @Override
    public List<ProblemHeadline> findProblemHeadlines(String patientId) {
        ProblemHeadlineQueryStrategy query = new ProblemHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public List<ProblemSummary> findAllProblems(String patientId) {
        ProblemSummaryQueryStrategy query = new ProblemSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public ProblemDetails findProblem(String patientId, String problemId) {
        ProblemDetailsQueryStrategy query = new ProblemDetailsQueryStrategy(patientId, problemId);

        return findData(query);
    }
}
