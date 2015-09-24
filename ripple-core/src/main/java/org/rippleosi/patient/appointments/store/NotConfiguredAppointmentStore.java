package org.rippleosi.patient.appointments.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.appointments.model.AppointmentDetails;

/**
 */
public class NotConfiguredAppointmentStore implements AppointmentStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body AppointmentDetails appointment) {
        throw ConfigurationException.unimplementedTransaction(AppointmentStore.class);
    }

    @Override
    public void update(@Header("patientId") String patientId, @Body AppointmentDetails appointment) {
        throw ConfigurationException.unimplementedTransaction(AppointmentStore.class);
    }
}
