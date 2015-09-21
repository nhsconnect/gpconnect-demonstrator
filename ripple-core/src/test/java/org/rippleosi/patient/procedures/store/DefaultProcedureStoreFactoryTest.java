package org.rippleosi.patient.procedures.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultProcedureStoreFactoryTest extends AbstractRepositoryFactoryTest<ProcedureStoreFactory, ProcedureStore> {

    @Override
    protected ProcedureStoreFactory createRepositoryFactory() {
        return new DefaultProcedureStoreFactory();
    }

    @Override
    protected Class<ProcedureStore> getRepositoryClass() {
        return ProcedureStore.class;
    }
}
