package org.rippleosi.patient.problems.store;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultProblemStoreFactory extends AbstractRepositoryFactory<ProblemStore> implements ProblemStoreFactory {

    @Override
    protected ProblemStore defaultRepository() {
        return new NotConfiguredProblemStore();
    }

    @Override
    protected Class<ProblemStore> repositoryClass() {
        return ProblemStore.class;
    }
}
