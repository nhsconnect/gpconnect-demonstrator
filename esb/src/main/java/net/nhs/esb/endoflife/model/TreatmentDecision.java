package net.nhs.esb.endoflife.model;

/**
 */
public class TreatmentDecision {

    private String decisionToRefuseTreatment;
    private String dateOfDecision;
    private String comment;

    public String getDecisionToRefuseTreatment() {
        return decisionToRefuseTreatment;
    }

    public void setDecisionToRefuseTreatment(String decisionToRefuseTreatment) {
        this.decisionToRefuseTreatment = decisionToRefuseTreatment;
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
