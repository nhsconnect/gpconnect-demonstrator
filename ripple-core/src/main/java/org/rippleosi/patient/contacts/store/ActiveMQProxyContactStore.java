package org.rippleosi.patient.contacts.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyContactStore implements ContactStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyContactStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Contacts.Create")
    private ContactStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Contacts.Update")
    private ContactStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, ContactDetails contact) {
        try {
            createTopic.create(patientId, contact);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, ContactDetails contact) {
        try {
            createTopic.create(patientId, contact);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
