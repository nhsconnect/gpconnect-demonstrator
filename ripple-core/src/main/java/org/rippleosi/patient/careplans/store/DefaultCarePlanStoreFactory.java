package org.rippleosi.patient.careplans.store;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultCarePlanStoreFactory extends AbstractRepositoryFactory<CarePlanStore> implements CarePlanStoreFactory {

    @Override
    protected CarePlanStore defaultRepository() {
        return new NotConfiguredCarePlanStore();
    }

    @Override
    protected Class<CarePlanStore> repositoryClass() {
        return CarePlanStore.class;
    }
}
