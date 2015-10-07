package org.rippleosi.search.reports.table.model;

import java.util.List;

public class ReportTableResults {

    private String totalPatients;
    private List<ReportTablePatientDetails> patientDetails;

    public String getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(String totalPatients) {
        this.totalPatients = totalPatients;
    }

    public List<ReportTablePatientDetails> getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(List<ReportTablePatientDetails> patientDetails) {
        this.patientDetails = patientDetails;
    }
}
