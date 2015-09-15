package org.rippleosi.patient.procedures.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultProcedureSearchFactoryTest extends AbstractRepositoryFactoryTest<ProcedureSearchFactory,ProcedureSearch> {

    @Override
    protected ProcedureSearchFactory createSearchFactory() {
        return new DefaultProcedureSearchFactory();
    }

    @Override
    protected Class<ProcedureSearch> getSearchClass() {
        return ProcedureSearch.class;
    }
}
