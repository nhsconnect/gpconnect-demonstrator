package org.rippleosi.patient.appointments.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.appointments.model.AppointmentDetails;

/**
 */
@InOnly
public interface AppointmentStore extends Repository {

    void create(@Header("patientId") String patientId, @Body AppointmentDetails appointment);

    void update(@Header("patientId") String patientId, @Body AppointmentDetails appointment);
}
