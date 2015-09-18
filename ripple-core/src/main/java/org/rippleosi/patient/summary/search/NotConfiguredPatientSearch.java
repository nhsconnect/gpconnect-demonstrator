package org.rippleosi.patient.summary.search;

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
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public PatientDetails findPatient(String patientId) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }
}
