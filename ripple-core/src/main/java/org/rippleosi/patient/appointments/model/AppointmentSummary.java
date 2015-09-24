package org.rippleosi.patient.appointments.model;

import java.util.Date;

/**
 */
public class AppointmentSummary {

    private String source;
    private String sourceId;
    private String serviceTeam;
    private Date dateOfAppointment;
    private Date timeOfAppointment;

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
}
