package org.rippleosi.patient.medication.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.model.MedicationSummary;

/**
 */
public class NotConfiguredMedicationSearch implements MedicationSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<MedicationHeadline> findMedicationHeadlines(String patientId) {
        throw notImplemented();
    }

    @Override
    public List<MedicationSummary> findAllMedication(String patientId) {
        throw notImplemented();
    }

    @Override
    public MedicationDetails findMedication(String patientId, String medicationId) {
        throw notImplemented();
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + MedicationSearch.class.getSimpleName() + " instance");
    }
}
