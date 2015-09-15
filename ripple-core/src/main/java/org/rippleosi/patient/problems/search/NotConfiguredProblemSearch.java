package org.rippleosi.patient.problems.search;

import java.util.Collections;
import java.util.Date;
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
        // TODO Replace this code with notImplemented()

        ProblemHeadline problem = new ProblemHeadline();
        problem.setSourceId("1");
        problem.setSource("openehr");
        problem.setProblem("Hay fever");

        return Collections.singletonList(problem);  // TODO
    }

    @Override
    public List<ProblemSummary> findAllProblems(String patientId) {

        ProblemSummary problem = new ProblemSummary();
        problem.setSourceId("1");
        problem.setSource("openehr");
        problem.setProblem("Hay fever");
        problem.setDateOfOnset(new Date());

        return Collections.singletonList(problem);
    }

    @Override
    public ProblemDetails findProblem(String patientId, String problemId) {

        ProblemDetails problem = new ProblemDetails();
        problem.setSourceId("1");
        problem.setSource("openehr");
        problem.setProblem("Hay fever");
        problem.setDateOfOnset(new Date());
        problem.setDescription("Hay fever");
        problem.setTerminology("Std");
        problem.setCode("00001");

        return problem;
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + ProblemStore.class.getSimpleName() + " instance");
    }
}
