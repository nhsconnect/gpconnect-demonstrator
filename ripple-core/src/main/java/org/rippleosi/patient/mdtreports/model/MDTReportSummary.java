package org.rippleosi.patient.mdtreports.model;

import java.util.Date;

/**
 */
public class MDTReportSummary {

    private String source;
    private String sourceId;
    private String serviceTeam;
    private Date dateOfRequest;
    private Date dateOfMeeting;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getServiceTeam() {
        return serviceTeam;
    }

    public void setServiceTeam(String serviceTeam) {
        this.serviceTeam = serviceTeam;
    }

    public Date getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(Date dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public Date getDateOfMeeting() {
        return dateOfMeeting;
    }

    public void setDateOfMeeting(Date dateOfMeeting) {
        this.dateOfMeeting = dateOfMeeting;
    }
}
