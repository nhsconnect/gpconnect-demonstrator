package org.rippleosi.patient.appointments.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyAppointmentStore implements AppointmentStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyAppointmentStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Appointment.Create")
    private AppointmentStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Appointment.Update")
    private AppointmentStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, AppointmentDetails appointment) {
        try {
            createTopic.create(patientId, appointment);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, AppointmentDetails appointment) {
        try {
            updateTopic.update(patientId, appointment);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
