package org.rippleosi.patient.procedures.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultProcedureSearchFactory extends AbstractRepositoryFactory<ProcedureSearch> implements ProcedureSearchFactory {

    @Override
    protected ProcedureSearch defaultRepository() {
        return new NotConfiguredProcedureSearch();
    }

    @Override
    protected Class<ProcedureSearch> repositoryClass() {
        return ProcedureSearch.class;
    }
}
