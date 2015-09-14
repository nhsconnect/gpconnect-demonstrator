package org.rippleosi.patient.summary.search;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;

/**
 */
public class NotConfiguredPatientSearch implements PatientSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<PatientSummary> findAllPatients() {
        // TODO Remove this code once implementation is done and replace with notImplemented()
        PatientSummary patientSummary = new PatientSummary();
        patientSummary.setId("9999999000");
        patientSummary.setName("Ivor Cox");
        patientSummary.setAddress("6948 Et St., Halesowen, Worcestershire, VX27 5DV");
        patientSummary.setDateOfBirth(new Date());
        patientSummary.setGender("Male");
        patientSummary.setNhsNumber("9999999000");

        return Collections.singletonList(patientSummary);
    }

    @Override
    public PatientDetails findPatient(String patientId) {

        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setId("9999999000");
        patientDetails.setName("COX, Ivor (Mr)");
        patientDetails.setGender("Male");
        patientDetails.setNhsNumber("9999999000");
        patientDetails.setAddress("6948 Et St., Halesowen, Worcestershire, VX27 5DV");
        patientDetails.setDateOfBirth(new Date());
        patientDetails.setGpDetails(null);
        patientDetails.setPasNumber(null);
        patientDetails.setTelephone("(011981) 32362");


        return patientDetails;
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + PatientSearch.class.getSimpleName() + " instance");
    }
}
