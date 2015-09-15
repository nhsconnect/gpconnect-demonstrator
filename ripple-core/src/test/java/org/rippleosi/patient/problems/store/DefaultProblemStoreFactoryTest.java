package org.rippleosi.patient.problems.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultProblemStoreFactoryTest extends AbstractRepositoryFactoryTest<ProblemStoreFactory, ProblemStore> {

    @Override
    protected ProblemStoreFactory createRepositoryFactory() {
        return new DefaultProblemStoreFactory();
    }

    @Override
    protected Class<ProblemStore> getRepositoryClass() {
        return ProblemStore.class;
    }
}
