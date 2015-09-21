package org.rippleosi.patient.procedures.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultProcedureSearchFactoryTest extends AbstractRepositoryFactoryTest<ProcedureSearchFactory,ProcedureSearch> {

    @Override
    protected ProcedureSearchFactory createRepositoryFactory() {
        return new DefaultProcedureSearchFactory();
    }

    @Override
    protected Class<ProcedureSearch> getRepositoryClass() {
        return ProcedureSearch.class;
    }
}
