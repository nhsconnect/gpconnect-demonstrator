package uk.gov.hscic.patient.problems.search;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

public class NotConfiguredProblemSearchTest {
    private NotConfiguredProblemSearch problemSearch;

    @Before
    public void setUp() throws Exception {
        problemSearch = new NotConfiguredProblemSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals(RepoSourceType.NONE, problemSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindProblemsHTMLTables() {
        problemSearch.findAllProblemHTMLTables(null);
    }
}
