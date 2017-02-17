package uk.gov.hscic.patient.allergies.model;

public class AllergyData {

    private String sourceId;
    private String nhsNumber;
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

    public String getNhsNumber() {
        return nhsNumber;
    }
    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

}
