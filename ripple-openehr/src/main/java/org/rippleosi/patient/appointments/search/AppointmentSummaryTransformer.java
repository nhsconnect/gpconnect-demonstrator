package org.rippleosi.patient.appointments.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.appointments.model.AppointmentSummary;

/**
 */
public class AppointmentSummaryTransformer implements Transformer<Map<String, Object>, AppointmentSummary> {

    @Override
    public AppointmentSummary transform(Map<String, Object> input) {

        Date appointmentDate = DateFormatter.toDateOnly(MapUtils.getString(input, "appointment_date"));
        Date appointmentTime = DateFormatter.toTimeOnly(MapUtils.getString(input, "appointment_date"));

        AppointmentSummary appointment = new AppointmentSummary();
        appointment.setSource("openehr");
        appointment.setSourceId(MapUtils.getString(input, "uid"));
        appointment.setServiceTeam(MapUtils.getString(input, "service_team"));
        appointment.setDateOfAppointment(appointmentDate);
        appointment.setTimeOfAppointment(appointmentTime);

        return appointment;
    }
}
