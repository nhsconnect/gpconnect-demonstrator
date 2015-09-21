package org.rippleosi.patient.medication.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyMedicationStore implements MedicationStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyMedicationStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Medication.Create")
    private MedicationStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Medication.Update")
    private MedicationStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, MedicationDetails medication) {
        try {
            createTopic.create(patientId, medication);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, MedicationDetails medication) {
        try {
            createTopic.create(patientId, medication);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
