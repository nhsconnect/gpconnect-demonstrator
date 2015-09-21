package org.rippleosi.patient.procedures.store;

import org.rippleosi.common.repo.RepositoryFactory;

/**
 */
@FunctionalInterface
public interface ProcedureStoreFactory extends RepositoryFactory<ProcedureStore> {
}
