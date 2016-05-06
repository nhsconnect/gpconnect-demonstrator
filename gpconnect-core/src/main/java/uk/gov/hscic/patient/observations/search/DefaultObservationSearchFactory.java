package uk.gov.hscic.patient.observations.search;

import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultObservationSearchFactory extends AbstractRepositoryFactory<ObservationSearch> implements ObservationSearchFactory {

    @Override
    protected ObservationSearch defaultRepository() {
        return new NotConfiguredObservationSearch();
    }

    @Override
    protected Class<ObservationSearch> repositoryClass() {
        return ObservationSearch.class;
    }
}
