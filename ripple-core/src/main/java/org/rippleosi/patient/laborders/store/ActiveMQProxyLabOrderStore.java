package org.rippleosi.patient.laborders.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyLabOrderStore implements LabOrderStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyLabOrderStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.LabOrder.Create")
    private LabOrderStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.LabOrder.Update")
    private LabOrderStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, LabOrderDetails labOrder) {
        try {
            createTopic.create(patientId, labOrder);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, LabOrderDetails labOrder) {
        try {
            updateTopic.update(patientId, labOrder);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
