package org.rippleosi.patient.mdtreports.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredMDTReportStoreTest {

    private NotConfiguredMDTReportStore mdtReportStore;

    @Before
    public void setUp() throws Exception {
        mdtReportStore = new NotConfiguredMDTReportStore();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", mdtReportStore.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingCreateMDTReport() {
        mdtReportStore.create(null, null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToUpdateMDTReport() {
        mdtReportStore.update(null, null);
    }
}
