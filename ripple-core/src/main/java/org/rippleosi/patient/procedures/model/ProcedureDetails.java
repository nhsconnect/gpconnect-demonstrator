package org.rippleosi.patient.procedures.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class ProcedureDetails implements Serializable {

    private String id;
    private String author;
    private Date dateSubmitted;
    private String name;
    private String notes;
    private String performer;
    private Date date;
    private String currentStatus;
    private String currentStatusCode;
    private String currentStatusTerminology;
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public String getCurrentStatusTerminology() {
        return currentStatusTerminology;
    }

    public void setCurrentStatusTerminology(String currentStatusTerminology) {
        this.currentStatusTerminology = currentStatusTerminology;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
