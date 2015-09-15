package org.rippleosi.patient.problems.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;

/**
 */
public interface ProblemSearch extends Repository {

    List<ProblemHeadline> findProblemHeadlines(String patientId);

    List<ProblemSummary> findAllProblems(String patientId);

    ProblemDetails findProblem(String patientId, String problemId);
}
