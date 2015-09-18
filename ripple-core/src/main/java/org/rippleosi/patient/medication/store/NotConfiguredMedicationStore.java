package org.rippleosi.patient.medication.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.medication.model.MedicationDetails;

/**
 */
public class NotConfiguredMedicationStore implements MedicationStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body MedicationDetails medication) {
        throw ConfigurationException.unimplementedTransaction(MedicationStore.class);
    }

    @Override
    public void update(@Header("patientId") String patientId, @Body MedicationDetails medication) {
        throw ConfigurationException.unimplementedTransaction(MedicationStore.class);
    }
}
