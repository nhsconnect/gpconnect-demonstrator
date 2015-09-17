package org.rippleosi.patient.laborders.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredLabOrderSearchTest {

    private NotConfiguredLabOrderSearch labOrderSearch;

    @Before
    public void setUp() throws Exception {
        labOrderSearch = new NotConfiguredLabOrderSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", labOrderSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllLabOrders() {
        labOrderSearch.findAllLabOrders(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindLabOrderDetails() {
        labOrderSearch.findLabOrder(null, null);
    }
}
