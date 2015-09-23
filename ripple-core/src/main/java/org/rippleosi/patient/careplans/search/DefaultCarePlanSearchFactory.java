package org.rippleosi.patient.careplans.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultCarePlanSearchFactory extends AbstractRepositoryFactory<CarePlanSearch> implements CarePlanSearchFactory {

    @Override
    protected CarePlanSearch defaultRepository() {
        return new NotConfiguredCarePlanSearch();
    }

    @Override
    protected Class<CarePlanSearch> repositoryClass() {
        return CarePlanSearch.class;
    }
}
