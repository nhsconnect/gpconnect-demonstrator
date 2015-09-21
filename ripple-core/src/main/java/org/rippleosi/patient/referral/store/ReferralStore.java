package org.rippleosi.patient.referral.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.referral.model.ReferralDetails;

/**
 */
@InOnly
public interface ReferralStore extends Repository {

    void create(@Header("patientId") String patientId, @Body ReferralDetails referral);

    void update(@Header("patientId") String patientId, @Body ReferralDetails referral);
}
