package net.nhs.esb.mdtreport.model;

import java.util.Date;

/**
 */
public class MDTReportComposition {

    private String compositionId;
    private String service;
    private Date dateOfRequest;
    private Date dateOfMeeting;
    private Date timeOfMeeting;
    private String servicePageLink;
    private String questionForMDT;
    private String meetingDiscussion;
    private String source;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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

    public Date getTimeOfMeeting() {
        return timeOfMeeting;
    }

    public void setTimeOfMeeting(Date timeOfMeeting) {
        this.timeOfMeeting = timeOfMeeting;
    }

    public String getServicePageLink() {
        return servicePageLink;
    }

    public void setServicePageLink(String servicePageLink) {
        this.servicePageLink = servicePageLink;
    }

    public String getQuestionForMDT() {
        return questionForMDT;
    }

    public void setQuestionForMDT(String questionForMDT) {
        this.questionForMDT = questionForMDT;
    }

    public String getMeetingDiscussion() {
        return meetingDiscussion;
    }

    public void setMeetingDiscussion(String meetingDiscussion) {
        this.meetingDiscussion = meetingDiscussion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
