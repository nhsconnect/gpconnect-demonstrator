package org.rippleosi.patient.transfers.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.Produce;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQProxyTransferStore implements TransferOfCareStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyTransferStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Transfers.Create")
    private TransferOfCareStore createTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body TransferOfCareDetails transferOfCare) {
        try {
            createTopic.create(patientId, transferOfCare);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
