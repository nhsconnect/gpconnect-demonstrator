package org.rippleosi.patient.appointments.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.appointments.model.AppointmentDetails;

/**
 */
public class AppointmentDetailsTransformer implements Transformer<Map<String, Object>, AppointmentDetails> {

    @Override
    public AppointmentDetails transform(Map<String, Object> input) {

        Date dateCreated = DateFormatter.toDate(MapUtils.getString(input, "date_created"));
        Date appointmentDate = DateFormatter.toDateOnly(MapUtils.getString(input, "appointment_date"));
        Date appointmentTime = DateFormatter.toTimeOnly(MapUtils.getString(input, "appointment_date"));

        AppointmentDetails appointment = new AppointmentDetails();
        appointment.setSource("openehr");
        appointment.setSourceId(MapUtils.getString(input, "uid"));
        appointment.setAuthor(MapUtils.getString(input, "author"));
        appointment.setDateCreated(dateCreated);
        appointment.setDateOfAppointment(appointmentDate);
        appointment.setTimeOfAppointment(appointmentTime);
        appointment.setLocation(MapUtils.getString(input, "location"));
        appointment.setServiceTeam(MapUtils.getString(input, "service_team"));
        appointment.setStatus(MapUtils.getString(input, "status"));

        return appointment;
    }
}
