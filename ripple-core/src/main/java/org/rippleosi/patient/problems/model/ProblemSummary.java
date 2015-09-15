package org.rippleosi.patient.problems.model;

import java.util.Date;

/**
 */
public class ProblemSummary {

    private String sourceId;
    private String source;
    private String problem;
    private Date dateOfOnset;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Date getDateOfOnset() {
        return dateOfOnset;
    }

    public void setDateOfOnset(Date dateOfOnset) {
        this.dateOfOnset = dateOfOnset;
    }
}
