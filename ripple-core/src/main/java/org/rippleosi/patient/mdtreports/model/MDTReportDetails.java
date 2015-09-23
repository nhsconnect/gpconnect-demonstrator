package org.rippleosi.patient.mdtreports.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class MDTReportDetails implements Serializable {

    private String source;
    private String sourceId;
    private String team;
    private Date dateOfRequest;
    private Date dateOfMeeting;
    private String servicePageLink;
    private String question;
    private String meetingDiscussion;

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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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

    public String getServicePageLink() {
        return servicePageLink;
    }

    public void setServicePageLink(String servicePageLink) {
        this.servicePageLink = servicePageLink;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMeetingDiscussion() {
        return meetingDiscussion;
    }

    public void setMeetingDiscussion(String meetingDiscussion) {
        this.meetingDiscussion = meetingDiscussion;
    }
}
