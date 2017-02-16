package uk.gov.hscic.patient.allergies.model;

import java.util.Date;

public class AllergyListHTML {

    private String sourceId;
    private String nhsNumber;
    private String source;
    private String provider;
    private String html;
    private Date lastUpdated;
    private String currentOrHistoric;
    private String startDate;
    private String endDate;
    private String details;

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setStartDate(String string) {
        this.startDate = string;
    }

    public void setEndDate(String string) {
        this.endDate = string;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCurrentOrHistoric() {
        return currentOrHistoric;
    }

    public void setCurrentOrHistoric(String currentOrHistoric) {
        this.currentOrHistoric = currentOrHistoric;
    }

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String getNhsNumber() {
        return nhsNumber;
    }
    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

}
