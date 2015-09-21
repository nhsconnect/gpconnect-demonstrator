package org.rippleosi.patient.procedures.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rippleosi.common.exception.ConfigurationException;

/**
 */
public class NotConfiguredProcedureSearchTest {

    private NotConfiguredProcedureSearch procedureSearch;

    @Before
    public void setUp() throws Exception {
        procedureSearch = new NotConfiguredProcedureSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals("not configured", procedureSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllProcedures() {
        procedureSearch.findAllProcedures(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindProcedureDetails() {
        procedureSearch.findProcedure(null, null);
    }
}
