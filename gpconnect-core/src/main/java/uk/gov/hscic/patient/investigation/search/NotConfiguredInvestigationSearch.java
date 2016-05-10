package uk.gov.hscic.patient.investigation.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.investigation.model.InvestigationListHTML;

import java.util.List;

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
    public List<InvestigationListHTML> findAllInvestigationHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(InvestigationSearch.class);
    }
}
