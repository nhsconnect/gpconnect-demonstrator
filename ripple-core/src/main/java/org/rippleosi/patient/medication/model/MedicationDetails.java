package org.rippleosi.patient.medication.model;

import java.util.Date;

/**
 */
public class MedicationDetails {

    private String sourceId;
    private String source;
    private String name;
    private String doseAmount;
    private String doseDirections;
    private String doseTiming;
    private String route;
    private Date startDateTime;
    private String medicationCode;
    private String medicationTerminology;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(String doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getDoseDirections() {
        return doseDirections;
    }

    public void setDoseDirections(String doseDirections) {
        this.doseDirections = doseDirections;
    }

    public String getDoseTiming() {
        return doseTiming;
    }

    public void setDoseTiming(String doseTiming) {
        this.doseTiming = doseTiming;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getMedicationCode() {
        return medicationCode;
    }

    public void setMedicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
    }

    public String getMedicationTerminology() {
        return medicationTerminology;
    }

    public void setMedicationTerminology(String medicationTerminology) {
        this.medicationTerminology = medicationTerminology;
    }
}
