package org.rippleosi.patient.labresults.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredLabResultSearchTest {

    private NotConfiguredLabResultSearch labResultSearch;

    @Before
    public void setUp() throws Exception {
        labResultSearch = new NotConfiguredLabResultSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", labResultSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllLabResults() {
        labResultSearch.findAllLabResults(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindLabResultDetails() {
        labResultSearch.findLabResult(null, null);
    }
}
