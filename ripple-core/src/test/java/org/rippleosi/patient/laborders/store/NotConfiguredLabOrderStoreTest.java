package org.rippleosi.patient.laborders.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredLabOrderStoreTest {

    private NotConfiguredLabOrderStore labOrderStore;

    @Before
    public void setUp() throws Exception {
        labOrderStore = new NotConfiguredLabOrderStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", labOrderStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateLabOrder() {
        labOrderStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateLabOrder() {
        labOrderStore.update(null, null);
    }
}
