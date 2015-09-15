package org.rippleosi.patient.referral.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public interface ReferralSearch extends Repository {

    List<ReferralSummary> findAllReferrals(String patientId);

    ReferralDetails findReferral(String patientId, String referralId);
}
