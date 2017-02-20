package uk.gov.hscic.patient.problems.model;

import java.util.Date;

public class ProblemListHTML {

    private String sourceId;
    private String nhsNumber;
    private String activeOrInactive;
    private Date startDate;
    private Date endDate;
    private String entry;
    private String significance;
    private String details;
    


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

    public String getActiveOrInactive() {
        return activeOrInactive;
    }

    public void setActiveOrInactive(String activeOrInactive) {
        this.activeOrInactive = activeOrInactive;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

  
}
