package org.rippleosi.patient.referral.search;

import java.util.Collections;
import java.util.Date;
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

        ReferralSummary referral = new ReferralSummary();
        referral.setSourceId("1");
        referral.setSource("openehr");
        referral.setDateOfReferral(new Date());
        referral.setReferralFrom("Dr Jane Wonders");
        referral.setReferralTo("Dr Joseph Lay");

        return Collections.singletonList(referral);

//        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }

    @Override
    public ReferralDetails findReferral(String patientId, String referralId) {

        ReferralDetails referral = new ReferralDetails();
        referral.setSourceId("1");
        referral.setSource("openehr");
        referral.setDateOfReferral(new Date());
        referral.setReferralFrom("Dr Jane Wonders");
        referral.setReferralTo("Dr Joseph Lay");
        referral.setAuthor("answer");
        referral.setClinicalSummary("Blood pressure has not decreased.");
        referral.setReason("Blood pressure");
        referral.setDateCreated(new Date());

        return referral;

//        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }
}
