package org.rippleosi.patient.appointments.store;

import org.rippleosi.common.repo.RepositoryFactory;

/**
 */
@FunctionalInterface
public interface AppointmentStoreFactory extends RepositoryFactory<AppointmentStore> {
}
