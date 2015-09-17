package org.rippleosi.patient.medication.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredMedicationStoreTest {

    private NotConfiguredMedicationStore medicationStore;

    @Before
    public void setUp() throws Exception {
        medicationStore = new NotConfiguredMedicationStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", medicationStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateMedication() {
        medicationStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateMedication() {
        medicationStore.update(null, null);
    }
}
