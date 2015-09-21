package org.rippleosi.patient.medication.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredMedicationSearchTest {

    private NotConfiguredMedicationSearch medicationSearch;

    @Before
    public void setUp() throws Exception {
        medicationSearch = new NotConfiguredMedicationSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", medicationSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindMedicationHeadlines() {
        medicationSearch.findMedicationHeadlines(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllMedication() {
        medicationSearch.findAllMedication(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindMedicationDetails() {
        medicationSearch.findMedication(null, null);
    }
}
