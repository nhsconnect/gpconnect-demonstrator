package net.nhs.esb.appointments.route.converter;

import net.nhs.esb.appointments.model.Appointment;
import net.nhs.esb.appointments.model.AppointmentsUpdate;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static net.nhs.esb.util.DateFormatter.toDate;
import static net.nhs.esb.util.DateFormatter.toTimeOnly;
import static org.apache.commons.collections4.MapUtils.getString;


@Converter
@Component
public class PatientAppointmentsCompositionConverter {

    private static final String APPOINTMENTS_PREFIX = "referral/referral_details:0/schedule_appointment/";

    @Converter
    public Appointment convertResponseToAppointmentsComposition(CompositionResponseData responseData) {
        // retrieve composition
        Map<String, Object> rawComposition = responseData.getComposition();

        // retrieve data
        String compositionId = getString(rawComposition, "referral/_uid");

        String dateTimeOfAppointment = getString(rawComposition, APPOINTMENTS_PREFIX + "appointment_schedule/lower");

        Date dateOfAppointment = toDate(dateTimeOfAppointment);
        Date timeSlot = toTimeOnly(dateTimeOfAppointment);

        String rawDateCreated = getString(rawComposition, APPOINTMENTS_PREFIX + "time");
        Date dateCreated = toDate(rawDateCreated);

        String careServiceTeam = getString(rawComposition, APPOINTMENTS_PREFIX + "referral_to");
        String location = getString(rawComposition, APPOINTMENTS_PREFIX + "receiver/address:0/location");
        String status = getString(rawComposition, APPOINTMENTS_PREFIX + "ism_transition/current_state|value");

        String author = getString(rawComposition, "ctx/composer_name");
        String source = "openehr";

        // populate composition
        Appointment appointment = new Appointment();
        appointment.setCompositionId(compositionId);
        appointment.setCareServiceTeam(careServiceTeam);
        appointment.setDateOfAppointment(dateOfAppointment);
        appointment.setTimeSlot(timeSlot);
        appointment.setLocation(location);
        appointment.setStatus(status);
        appointment.setDateCreated(dateCreated);
        appointment.setAuthor(author);
        appointment.setSource(source);

        return appointment;
    }

    @Converter
    public AppointmentsUpdate convertAppointmentsCompositionToUpdate(Appointment appointment) {
        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");
        content.put("ctx/composer_name", appointment.getAuthor());

        Date appointmentStart = appointment.getDateOfAppointment();
        Date appointmentEnd = DateUtils.addHours(appointmentStart, 1);

        content.put(APPOINTMENTS_PREFIX + "referral_to", appointment.getCareServiceTeam());
        content.put(APPOINTMENTS_PREFIX + "appointment_schedule/lower", DateFormatter.toString(appointment.getTimeSlot()));
        content.put(APPOINTMENTS_PREFIX + "appointment_schedule/upper", DateFormatter.toString(appointmentEnd));
        content.put(APPOINTMENTS_PREFIX + "receiver/address:0/location", appointment.getLocation());
        content.put(APPOINTMENTS_PREFIX + "ism_transition/current_state|value", appointment.getStatus());
        content.put(APPOINTMENTS_PREFIX + "time", DateFormatter.toString(appointment.getDateCreated()));

        return new AppointmentsUpdate(content);
    }
}
