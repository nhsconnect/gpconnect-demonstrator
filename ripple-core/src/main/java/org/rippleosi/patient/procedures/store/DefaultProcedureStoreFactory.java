package org.rippleosi.patient.procedures.store;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultProcedureStoreFactory extends AbstractRepositoryFactory<ProcedureStore> implements ProcedureStoreFactory {

    @Override
    protected ProcedureStore defaultRepository() {
        return new NotConfiguredProcedureStore();
    }

    @Override
    protected Class<ProcedureStore> repositoryClass() {
        return ProcedureStore.class;
    }
}
