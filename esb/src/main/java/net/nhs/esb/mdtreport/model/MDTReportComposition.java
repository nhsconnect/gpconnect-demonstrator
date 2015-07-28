package net.nhs.esb.mdtreport.model;

/**
 */
public class MDTReportComposition {

    private String compositionId;
    private String service;
    private String dateOfRequest;
    private String dateOfMeeting;
    private String timeOfMeeting;
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

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public String getDateOfMeeting() {
        return dateOfMeeting;
    }

    public void setDateOfMeeting(String dateOfMeeting) {
        this.dateOfMeeting = dateOfMeeting;
    }

    public String getTimeOfMeeting() {
        return timeOfMeeting;
    }

    public void setTimeOfMeeting(String timeOfMeeting) {
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
