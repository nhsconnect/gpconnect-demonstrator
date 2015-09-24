package org.rippleosi.patient.appointments.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.rippleosi.patient.appointments.model.AppointmentSummary;

/**
 */
public interface AppointmentSearch extends Repository {

    List<AppointmentSummary> findAllAppointments(String patientId);

    AppointmentDetails findAppointment(String patientId, String appointmentId);
}
