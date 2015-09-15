package org.rippleosi.patient.referral.model;

import java.util.Date;

/**
 */
public class ReferralSummary {

    private String sourceId;
    private String source;
    private Date dateOfReferral;
    private String referralFrom;
    private String referralTo;

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

    public Date getDateOfReferral() {
        return dateOfReferral;
    }

    public void setDateOfReferral(Date dateOfReferral) {
        this.dateOfReferral = dateOfReferral;
    }

    public String getReferralFrom() {
        return referralFrom;
    }

    public void setReferralFrom(String referralFrom) {
        this.referralFrom = referralFrom;
    }

    public String getReferralTo() {
        return referralTo;
    }

    public void setReferralTo(String referralTo) {
        this.referralTo = referralTo;
    }
}
