package uk.gov.hscic.patient.referral.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hscic.common.types.RepoSourceType;

/**
 */
public class NotConfiguredReferralSearchTest {

    private NotConfiguredReferralSearch referralSearch;

    @Before
    public void setUp() throws Exception {
        referralSearch = new NotConfiguredReferralSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals(RepoSourceType.NONE, referralSearch.getSource());
    }

}
