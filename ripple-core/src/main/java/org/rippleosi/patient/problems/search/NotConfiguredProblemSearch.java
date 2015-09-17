package org.rippleosi.patient.problems.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;
import org.rippleosi.patient.problems.store.ProblemStore;

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
        throw notImplemented();
    }

    @Override
    public List<ProblemSummary> findAllProblems(String patientId) {
        throw notImplemented();
    }

    @Override
    public ProblemDetails findProblem(String patientId, String problemId) {
        throw notImplemented();
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + ProblemStore.class.getSimpleName() + " instance");
    }
}
