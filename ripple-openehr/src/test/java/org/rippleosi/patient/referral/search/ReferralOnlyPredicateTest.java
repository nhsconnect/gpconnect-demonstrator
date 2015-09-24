package org.rippleosi.patient.referral.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 */
public class ReferralOnlyPredicateTest {

    private ReferralOnlyPredicate predicate;

    @Before
    public void setUp() throws Exception {
        predicate = new ReferralOnlyPredicate();
    }

    @Test
    public void shouldReturnFalseWhenReferralFromIsMissing() {

        Map<String, Object> input = Collections.singletonMap("referral_to", "to");

        boolean result = predicate.evaluate(input);

        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenReferralToIsMissing() {

        Map<String, Object> input = Collections.singletonMap("referral_from", "from");

        boolean result = predicate.evaluate(input);

        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenBothEntriesAreMissing() {

        Map<String, Object> input = Collections.emptyMap();

        boolean result = predicate.evaluate(input);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenBothEntriesArePresent() {

        Map<String, Object> input = new HashMap<>();
        input.put("referral_from", "from");
        input.put("referral_to", "to");

        boolean result = predicate.evaluate(input);

        assertTrue(result);
    }
}
