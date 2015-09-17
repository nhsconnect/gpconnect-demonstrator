package org.rippleosi.patient.referral.store;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.referral.model.ReferralDetails;

/**
 */
public class NotConfiguredReferralStore implements ReferralStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(String patientId, ReferralDetails referral) {
        throw notImplemented();
    }

    @Override
    public void update(String patientId, ReferralDetails referral) {
        throw notImplemented();
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + ReferralStore.class.getSimpleName() + " instance");
    }
}