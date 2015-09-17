package org.rippleosi.patient.contacts.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredContactStoreTest {

    private NotConfiguredContactStore contactStore;

    @Before
    public void setUp() throws Exception {
        contactStore = new NotConfiguredContactStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", contactStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateContact() {
        contactStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateContact() {
        contactStore.update(null, null);
    }
}
