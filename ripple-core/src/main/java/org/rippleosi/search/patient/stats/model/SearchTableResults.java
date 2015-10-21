package org.rippleosi.search.patient.stats.model;

import java.util.List;

public class SearchTableResults {

    private String totalPatients;
    private List<SearchTablePatientDetails> patientDetails;

    public String getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(String totalPatients) {
        this.totalPatients = totalPatients;
    }

    public List<SearchTablePatientDetails> getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(List<SearchTablePatientDetails> patientDetails) {
        this.patientDetails = patientDetails;
    }
}
