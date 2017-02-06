package uk.gov.hscic.patient.referral.search;

import java.util.Date;
import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;
/**
 */
public class NotConfiguredReferralSearch implements ReferralSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ReferralListHTML> findAllReferralHTMLTables(final String patientId,Date fromDate, Date toDate) {
        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }
}
