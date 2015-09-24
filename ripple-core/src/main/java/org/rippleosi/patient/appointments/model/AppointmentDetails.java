package org.rippleosi.patient.appointments.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class AppointmentDetails implements Serializable {

    private String source;
    private String sourceId;
    private String author;
    private Date dateCreated;
    private String serviceTeam;
    private Date dateOfAppointment;
    private Date timeOfAppointment;
    private String location;
    private String status;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getServiceTeam() {
        return serviceTeam;
    }

    public void setServiceTeam(String serviceTeam) {
        this.serviceTeam = serviceTeam;
    }

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Date dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public Date getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public void setTimeOfAppointment(Date timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
