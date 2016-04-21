/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.patient.appointments.store;

import org.apache.camel.Produce;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.appointments.model.AppointmentDetails;
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
    public RepoSource getSource() {
        return RepoSourceType.ACTIVEMQ;
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
