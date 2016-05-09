package uk.gov.hscic.patient.immunisations.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultImmunisationSearchFactory extends AbstractRepositoryFactory<ImmunisationSearch> implements ImmunisationSearchFactory {

    @Override
    protected ImmunisationSearch defaultRepository() {
        return new NotConfiguredImmunisationSearch();
    }

    @Override
    protected Class<ImmunisationSearch> repositoryClass() {
        return ImmunisationSearch.class;
    }
}
