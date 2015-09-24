package org.rippleosi.patient.appointments.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredAppointmentStoreTest {

    private NotConfiguredAppointmentStore appointmentStore;

    @Before
    public void setUp() throws Exception {
        appointmentStore = new NotConfiguredAppointmentStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", appointmentStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateAppointment() {
        appointmentStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateAppointment() {
        appointmentStore.update(null, null);
    }
}
