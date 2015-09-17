package org.rippleosi.patient.procedures.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredProcedureStoreTest {

    private NotConfiguredProcedureStore procedureStore;

    @Before
    public void setUp() throws Exception {
        procedureStore = new NotConfiguredProcedureStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", procedureStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateProcedure() {
        procedureStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateProcedure() {
        procedureStore.update(null, null);
    }
}
