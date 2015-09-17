package org.rippleosi.patient.referral.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredReferralStoreTest {

    private NotConfiguredReferralStore referralStore;

    @Before
    public void setUp() throws Exception {
        referralStore = new NotConfiguredReferralStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", referralStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateReferral() {
        referralStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateReferral() {
        referralStore.update(null, null);
    }
}
