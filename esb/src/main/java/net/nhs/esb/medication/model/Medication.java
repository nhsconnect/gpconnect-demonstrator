package net.nhs.esb.medication.model;

/**
 */
public class Medication {

    private String name;
    private String route;
    private String doseAmount;
    private String doseTiming;
    private String startDateTime;
    private String doseDirections;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(String doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getDoseTiming() {
        return doseTiming;
    }

    public void setDoseTiming(String doseTiming) {
        this.doseTiming = doseTiming;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getDoseDirections() {
        return doseDirections;
    }

    public void setDoseDirections(String doseDirections) {
        this.doseDirections = doseDirections;
    }
}
