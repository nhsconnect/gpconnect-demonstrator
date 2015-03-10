package net.nhs.esb.problem.model;

import java.util.List;

/**
 */
public class ProblemComposition {

    private String compositionId;
    private List<Problem> problems;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
