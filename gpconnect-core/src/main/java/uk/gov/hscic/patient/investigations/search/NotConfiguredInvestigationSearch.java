package uk.gov.hscic.patient.investigations.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.investigations.model.InvestigationListHtml;

public class NotConfiguredInvestigationSearch implements InvestigationSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public InvestigationListHtml findInvestigationListHtml(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(InvestigationSearch.class);
    }
}
