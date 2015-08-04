package net.nhs.esb.endoflife.model;

import java.util.Date;

/**
 */
public class TreatmentDecision {

    private String decisionToRefuseTreatment;
    private Date dateOfDecision;
    private String comment;

    public String getDecisionToRefuseTreatment() {
        return decisionToRefuseTreatment;
    }

    public void setDecisionToRefuseTreatment(String decisionToRefuseTreatment) {
        this.decisionToRefuseTreatment = decisionToRefuseTreatment;
    }

    public Date getDateOfDecision() {
        return dateOfDecision;
    }

    public void setDateOfDecision(Date dateOfDecision) {
        this.dateOfDecision = dateOfDecision;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
