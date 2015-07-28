package net.nhs.esb.endoflife.model;

/**
 */
public class CprDecision {

    private String cprDecision;
    private String dateOfDecision;
    private String comment;

    public String getCprDecision() {
        return cprDecision;
    }

    public void setCprDecision(String cprDecision) {
        this.cprDecision = cprDecision;
    }

    public String getDateOfDecision() {
        return dateOfDecision;
    }

    public void setDateOfDecision(String dateOfDecision) {
        this.dateOfDecision = dateOfDecision;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
