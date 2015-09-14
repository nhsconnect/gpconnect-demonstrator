package org.rippleosi.patient.allergies.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredAllergySearchTest {

    private NotConfiguredAllergySearch allergySearch;

    @Before
    public void setUp() throws Exception {
        allergySearch = new NotConfiguredAllergySearch();
    }

    @Test
    public void shouldReportAsUnknownImplementation() {
        assertEquals("not configured", allergySearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllergyHeadlines() {
        allergySearch.findAllergyHeadlines(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllAllergies() {
        allergySearch.findAllAllergies(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindPatientDetails() {
        allergySearch.findAllergy(null, null);
    }
}
