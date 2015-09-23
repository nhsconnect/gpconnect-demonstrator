package org.rippleosi.patient.transfers.store;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

import static org.junit.Assert.assertEquals;

public class NotConfiguredTransferOfCareStoreTest {

    private NotConfiguredTransferOfCareStore transferStore;

    @Before
    public void setUp() throws Exception {
        transferStore = new NotConfiguredTransferOfCareStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", transferStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToCreateTransfer() {
        transferStore.create(null, null);
    }
}
