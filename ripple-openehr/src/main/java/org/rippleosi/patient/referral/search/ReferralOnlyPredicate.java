package org.rippleosi.patient.referral.search;

import java.util.Map;

import org.apache.commons.collections4.Predicate;

/**
 */
public class ReferralOnlyPredicate implements Predicate<Map<String, Object>> {

    @Override
    public boolean evaluate(Map<String, Object> input) {
        return input.get("referral_from") != null && input.get("referral_to") != null;
    }
}
