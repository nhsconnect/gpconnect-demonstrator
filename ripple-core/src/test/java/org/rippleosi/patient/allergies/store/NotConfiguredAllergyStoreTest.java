package org.rippleosi.patient.allergies.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredAllergyStoreTest {

    private NotConfiguredAllergyStore allergyStore;

    @Before
    public void setUp() throws Exception {
        allergyStore = new NotConfiguredAllergyStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", allergyStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateAllergy() {
        allergyStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateAllergy() {
        allergyStore.update(null, null);
    }
}
