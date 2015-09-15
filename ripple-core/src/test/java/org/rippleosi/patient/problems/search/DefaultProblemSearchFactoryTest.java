package org.rippleosi.patient.problems.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultProblemSearchFactoryTest extends AbstractRepositoryFactoryTest<ProblemSearchFactory, ProblemSearch> {

    @Override
    protected ProblemSearchFactory createRepositoryFactory() {
        return new DefaultProblemSearchFactory();
    }

    @Override
    protected Class<ProblemSearch> getRepositoryClass() {
        return ProblemSearch.class;
    }
}
