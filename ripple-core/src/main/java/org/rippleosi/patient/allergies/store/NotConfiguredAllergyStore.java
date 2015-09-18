package org.rippleosi.patient.allergies.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.allergies.model.AllergyDetails;

/**
 */
public class NotConfiguredAllergyStore implements AllergyStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body AllergyDetails allergy) {
        throw ConfigurationException.unimplementedTransaction(AllergyStore.class);
    }

    @Override
    public void update(@Header("patientId") String patientId, @Body AllergyDetails allergy) {
        throw ConfigurationException.unimplementedTransaction(AllergyStore.class);
    }
}
