package uk.gov.hscic.patient.referral.search;

import java.util.Date;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.referral.model.ReferralListHtml;

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
    public ReferralListHtml findReferralListHtml(final String patientId, Date fromDate, Date toDate) {
        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }
}
