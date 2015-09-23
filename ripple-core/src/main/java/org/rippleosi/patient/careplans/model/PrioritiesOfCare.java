package org.rippleosi.patient.careplans.model;

import java.io.Serializable;

/**
 */
public class PrioritiesOfCare implements Serializable {

    private String placeOfCare;
    private String placeOfDeath;
    private String comment;

    public String getPlaceOfCare() {
        return placeOfCare;
    }

    public void setPlaceOfCare(String placeOfCare) {
        this.placeOfCare = placeOfCare;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
