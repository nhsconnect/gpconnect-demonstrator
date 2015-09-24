package org.rippleosi.patient.appointments.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredAppointmentSearchTest {

    private NotConfiguredAppointmentSearch appointmentSearch;

    @Before
    public void setUp() throws Exception {
        appointmentSearch = new NotConfiguredAppointmentSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", appointmentSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllAppointments() {
        appointmentSearch.findAllAppointments(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAppointmentDetails() {
        appointmentSearch.findAppointment(null, null);
    }
}
