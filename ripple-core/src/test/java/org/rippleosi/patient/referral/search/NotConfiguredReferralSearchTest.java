package org.rippleosi.patient.referral.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredReferralSearchTest {

    private NotConfiguredReferralSearch referralSearch;

    @Before
    public void setUp() throws Exception {
        referralSearch = new NotConfiguredReferralSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", referralSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllReferrals() {
        referralSearch.findAllReferrals(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindReferralDetails() {
        referralSearch.findReferral(null, null);
    }
}
