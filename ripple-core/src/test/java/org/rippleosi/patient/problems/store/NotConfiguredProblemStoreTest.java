package org.rippleosi.patient.problems.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredProblemStoreTest {

    private NotConfiguredProblemStore problemStore;

    @Before
    public void setUp() throws Exception {
        problemStore = new NotConfiguredProblemStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", problemStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateProblem() {
        problemStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateProblem() {
        problemStore.update(null, null);
    }
}
