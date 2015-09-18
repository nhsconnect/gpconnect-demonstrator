package org.rippleosi.patient.problems.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;

/**
 */
public class NotConfiguredProblemSearch implements ProblemSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ProblemHeadline> findProblemHeadlines(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }

    @Override
    public List<ProblemSummary> findAllProblems(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }

    @Override
    public ProblemDetails findProblem(String patientId, String problemId) {
        throw ConfigurationException.unimplementedTransaction(ProblemSearch.class);
    }

}
