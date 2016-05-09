package uk.gov.hscic.patient.keyindicator.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultKeyIndicatorSearchFactory extends AbstractRepositoryFactory<KeyIndicatorSearch> implements KeyIndicatorSearchFactory {

    @Override
    protected KeyIndicatorSearch defaultRepository() {
        return new NotConfiguredKeyIndicatorSearch();
    }

    @Override
    protected Class<KeyIndicatorSearch> repositoryClass() {
        return KeyIndicatorSearch.class;
    }
}
