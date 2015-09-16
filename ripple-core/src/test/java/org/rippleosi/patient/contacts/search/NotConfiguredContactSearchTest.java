package org.rippleosi.patient.contacts.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredContactSearchTest {

    private NotConfiguredContactSearch contactSearch;

    @Before
    public void setUp() throws Exception {
        contactSearch = new NotConfiguredContactSearch();
    }

    @Test
    public void shouldReportAsUnknownImplementation() {
        assertEquals("not configured", contactSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindContactHeadlines() {
        contactSearch.findContactHeadlines(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllAllergies() {
        contactSearch.findAllContacts(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindPatientDetails() {
        contactSearch.findContact(null, null);
    }
}
