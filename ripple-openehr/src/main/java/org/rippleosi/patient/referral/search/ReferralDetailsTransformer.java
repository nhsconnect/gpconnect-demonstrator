package org.rippleosi.patient.referral.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.referral.model.ReferralDetails;

/**
 */
public class ReferralDetailsTransformer implements Transformer<Map<String,Object>, ReferralDetails> {

    @Override
    public ReferralDetails transform(Map<String, Object> input) {

        Date dateOfReferral = DateFormatter.toDate(MapUtils.getString(input, "referral_date"));
        Date dateCreated = DateFormatter.toDate(MapUtils.getString(input, "date_submitted"));

        ReferralDetails referral = new ReferralDetails();
        referral.setSource("openehr");
        referral.setSourceId(MapUtils.getString(input, "uid"));
        referral.setReferralFrom(MapUtils.getString(input, "referral_from"));
        referral.setReferralTo(MapUtils.getString(input, "referral_to"));
        referral.setDateOfReferral(dateOfReferral);
        referral.setReason(MapUtils.getString(input, "referral_reason"));
        referral.setClinicalSummary(MapUtils.getString(input, "clinical_summary"));
        referral.setAuthor(MapUtils.getString(input, "author"));
        referral.setDateCreated(dateCreated);

        return referral;
    }
}
