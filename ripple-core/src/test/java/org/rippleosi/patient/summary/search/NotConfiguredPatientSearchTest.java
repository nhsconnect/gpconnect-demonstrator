package org.rippleosi.patient.summary.search;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

import static org.junit.Assert.assertEquals;

public class NotConfiguredPatientSearchTest {

    private NotConfiguredPatientSearch patientSearch;

    @Before
    public void setUp() throws Exception {
        patientSearch = new NotConfiguredPatientSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", patientSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllPatients() {
        patientSearch.findAllPatients();
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindPatient() {
        patientSearch.findPatient(null);
    }
}
