package org.rippleosi.patient.mdtreports.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredMDTReportSearchTest {

    private NotConfiguredMDTReportSearch mdtReportSearch;

    @Before
    public void setUp() throws Exception {
        mdtReportSearch = new NotConfiguredMDTReportSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", mdtReportSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllMDTReports() {
        mdtReportSearch.findAllMDTReports(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindMDTReportDetails() {
        mdtReportSearch.findMDTReport(null, null);
    }
}
