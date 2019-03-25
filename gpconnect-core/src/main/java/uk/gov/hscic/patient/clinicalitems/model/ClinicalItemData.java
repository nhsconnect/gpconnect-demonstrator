package uk.gov.hscic.patient.clinicalitems.model;

import java.util.Date;

public class ClinicalItemData {

    private String sourceId;
    private String source;
    private Date itemDate;
    private String Entry;
    private String Details;


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

    public Date getDate() {
        return itemDate;
    }

    public void setDate(Date itemDate) {
        this.itemDate = itemDate;
    }

    public String getEntry() {
        return Entry;
    }

    public void setEntry(String entry) {
        Entry = entry;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

 
}
