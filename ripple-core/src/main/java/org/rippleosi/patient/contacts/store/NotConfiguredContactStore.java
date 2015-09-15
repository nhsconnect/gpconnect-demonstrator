package org.rippleosi.patient.contacts.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.contacts.model.ContactDetails;

/**
 */
public class NotConfiguredContactStore implements ContactStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body ContactDetails contact) {
        throw notImplemented();
    }

    @Override
    public void update(@Header("patientId") String patientId, @Body ContactDetails contact) {
        throw notImplemented();
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + ContactStore.class.getSimpleName() + " instance");
    }
}
