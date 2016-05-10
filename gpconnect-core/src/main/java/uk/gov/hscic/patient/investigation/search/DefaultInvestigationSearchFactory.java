package uk.gov.hscic.patient.investigation.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultInvestigationSearchFactory extends AbstractRepositoryFactory<InvestigationSearch> implements InvestigationSearchFactory {

    @Override
    protected InvestigationSearch defaultRepository() {
        return new NotConfiguredInvestigationSearch();
    }

    @Override
    protected Class<InvestigationSearch> repositoryClass() {
        return InvestigationSearch.class;
    }
}
