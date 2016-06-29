package uk.gov.hscic.location.search;

import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultLocationSearchFactory extends AbstractRepositoryFactory<LocationSearch> implements LocationSearchFactory {

    @Override
    protected LocationSearch defaultRepository() {
        return new NotConfiguredLocationSearch();
    }

    @Override
    protected Class<LocationSearch> repositoryClass() {
        return LocationSearch.class;
    }
}
