package org.rippleosi.patient.referral.store;

import org.apache.camel.Produce;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class ActiveMQProxyReferralStore implements ReferralStore {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQProxyReferralStore.class);

    @Value("${repository.config.jms:500}")
    private int priority;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Referrals.Create")
    private ReferralStore createTopic;

    @Produce(uri = "activemq:topic:VirtualTopic.Ripple.Referrals.Update")
    private ReferralStore updateTopic;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return "activemq";
    }

    @Override
    public void create(String patientId, ReferralDetails referral) {
        try {
            createTopic.create(patientId, referral);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(String patientId, ReferralDetails referral) {
        try {
            updateTopic.update(patientId, referral);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
