package org.rippleosi.patient.appointments.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.rippleosi.patient.appointments.model.AppointmentSummary;

/**
 */
public class NotConfiguredAppointmentSearch implements AppointmentSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<AppointmentSummary> findAllAppointments(String patientId) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }

    @Override
    public AppointmentDetails findAppointment(String patientId, String appointmentId) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }
}
