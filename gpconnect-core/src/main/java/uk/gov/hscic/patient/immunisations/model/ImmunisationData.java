package uk.gov.hscic.patient.immunisations.model;

import java.util.Date;



public class ImmunisationData {

    private String sourceId;
    private String nhsNumber;
    private Date dateOfVac;
    private String vaccination;
    private String part;
    private String contents;
    private String details;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

   
    public Date getDateOfVac() {
        return dateOfVac;
    }

    public void setDateOfVac(Date dateOfVac) {
        this.dateOfVac = dateOfVac;
    }

    public String getVaccination() {
        return vaccination;
    }

    public void setVaccination(String vaccination) {
        this.vaccination = vaccination;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }
    
    
}
