package org.rippleosi.patient.referral.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public class NotConfiguredReferralSearch implements ReferralSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ReferralSummary> findAllReferrals(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }

    @Override
    public ReferralDetails findReferral(String patientId, String referralId) {
        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }
}
