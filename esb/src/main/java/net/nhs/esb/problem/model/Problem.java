package net.nhs.esb.problem.model;

/**
 */
public class Problem {

    private String problem;
    private String description;
    private String code;
    private String terminology;
    private String dateOfOnset;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTerminology() {
        return terminology;
    }

    public void setTerminology(String terminology) {
        this.terminology = terminology;
    }

    public String getDateOfOnset() {
        return dateOfOnset;
    }

    public void setDateOfOnset(String dateOfOnset) {
        this.dateOfOnset = dateOfOnset;
    }
}
