package org.rippleosi.patient.transfers.search;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

import static org.junit.Assert.assertEquals;

public class NotConfiguredTransferOfCareSearchTest {

    private NotConfiguredTransferOfCareSearch transferSearch;

    @Before
    public void setUp() throws Exception {
        transferSearch = new NotConfiguredTransferOfCareSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", transferSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindTransfers() {
        transferSearch.findAllTransfers(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindTransfer() {
        transferSearch.findTransferOfCare(null, null);
    }
}
