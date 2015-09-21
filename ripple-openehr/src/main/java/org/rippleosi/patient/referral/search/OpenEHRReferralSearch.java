package org.rippleosi.patient.referral.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.model.ReferralSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRReferralSearch extends AbstractOpenEhrService implements ReferralSearch {

    @Override
    public List<ReferralSummary> findAllReferrals(String patientId) {
        ReferralSummaryQueryStrategy query = new ReferralSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public ReferralDetails findReferral(String patientId, String referralId) {
        ReferralDetailsQueryStrategy query = new ReferralDetailsQueryStrategy(patientId, referralId);

        return findData(query);
    }
}
