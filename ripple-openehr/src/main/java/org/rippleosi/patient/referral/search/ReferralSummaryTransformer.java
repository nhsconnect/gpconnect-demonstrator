package org.rippleosi.patient.referral.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public class ReferralSummaryTransformer implements Transformer<Map<String, Object>, ReferralSummary> {

    @Override
    public ReferralSummary transform(Map<String, Object> input) {

        Date dateOfReferral = DateFormatter.toDate(MapUtils.getString(input, "referral_date"));

        ReferralSummary referral = new ReferralSummary();
        referral.setSource("openehr");
        referral.setSourceId(MapUtils.getString(input, "uid"));
        referral.setReferralFrom(MapUtils.getString(input, "referral_from"));
        referral.setReferralTo(MapUtils.getString(input, "referral_to"));
        referral.setDateOfReferral(dateOfReferral);

        return referral;
    }
}
