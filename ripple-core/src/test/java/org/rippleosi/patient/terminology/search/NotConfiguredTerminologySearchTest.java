package org.rippleosi.patient.terminology.search;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

import static org.junit.Assert.assertEquals;

public class NotConfiguredTerminologySearchTest {

    private NotConfiguredTerminologySearch terminologySearch;

    @Before
    public void setUp() throws Exception {
        terminologySearch = new NotConfiguredTerminologySearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", terminologySearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindTerms() {
        terminologySearch.findTerms(null);
    }
}
