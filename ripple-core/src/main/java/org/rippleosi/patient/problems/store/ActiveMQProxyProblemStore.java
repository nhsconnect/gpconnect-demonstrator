package org.rippleosi.patient.problems.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyProblemStore implements ProblemStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyProblemStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Problems.Create")
    private ProblemStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Problems.Update")
    private ProblemStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, ProblemDetails problem) {
        try {
            createTopic.create(patientId, problem);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, ProblemDetails problem) {
        try {
            updateTopic.update(patientId, problem);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
