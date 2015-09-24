package org.rippleosi.patient.appointments.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.rippleosi.patient.appointments.model.AppointmentSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRAppointmentSearch extends AbstractOpenEhrService implements AppointmentSearch {

    @Override
    public List<AppointmentSummary> findAllAppointments(String patientId) {
        AppointmentSummaryQueryStrategy query = new AppointmentSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public AppointmentDetails findAppointment(String patientId, String appointmentId) {
        AppointmentDetailsQueryStrategy query = new AppointmentDetailsQueryStrategy(patientId, appointmentId);

        return findData(query);
    }
}
