package org.rippleosi.patient.mdtreports.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyMDTReportStore implements MDTReportStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyMDTReportStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.MDTReport.Create")
    private MDTReportStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.MDTReport.Update")
    private MDTReportStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, MDTReportDetails mdtReport) {
        try {
            createTopic.create(patientId, mdtReport);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, MDTReportDetails mdtReport) {
        try {
            updateTopic.update(patientId, mdtReport);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
