package org.rippleosi.patient.appointments.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.apache.commons.lang3.time.DateUtils;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRAppointmentStore extends AbstractOpenEhrService implements AppointmentStore {

    @Value("${openehr.appointmentsTemplate}")
    private String appointmentsTemplate;

    private static final String APPOINTMENTS_PREFIX = "referral/referral_details:0/schedule_appointment/";

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.Appointment.Create")
    public void create(String patientId, AppointmentDetails appointment) {

        Map<String, Object> content = createFlatJsonContent(appointment);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, appointmentsTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.Appointment.Update")
    public void update(String patientId, AppointmentDetails appointment) {

        Map<String, Object> content = createFlatJsonContent(appointment);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(appointment.getSourceId(), patientId, appointmentsTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String, Object> createFlatJsonContent(AppointmentDetails appointment) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        String appointmentStart = DateFormatter.combineDateTime(appointment.getDateOfAppointment(), appointment.getTimeOfAppointment());
        String appointmentEnd = DateFormatter.toString(DateUtils.addHours(DateFormatter.toDate(appointmentStart), 1));

        content.put(APPOINTMENTS_PREFIX + "referral_to", appointment.getServiceTeam());
        content.put(APPOINTMENTS_PREFIX + "appointment_schedule/lower", appointmentStart);
        content.put(APPOINTMENTS_PREFIX + "appointment_schedule/upper", appointmentEnd);
        content.put(APPOINTMENTS_PREFIX + "receiver/address:0/location", appointment.getLocation());
        content.put(APPOINTMENTS_PREFIX + "ism_transition/current_state|value", appointment.getStatus());
        content.put(APPOINTMENTS_PREFIX + "time", DateFormatter.toString(appointment.getDateCreated()));

        return content;
    }
}
