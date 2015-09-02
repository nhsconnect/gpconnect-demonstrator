package org.rippleosi.patient.procedures.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyProcedureStore implements ProcedureStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyProcedureStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Procedures.Create")
    private ProcedureStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Procedures.Update")
    private ProcedureStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, ProcedureDetails procedure) {
        try {
            createTopic.create(patientId, procedure);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, ProcedureDetails procedure) {
        try {
            updateTopic.update(patientId, procedure);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
