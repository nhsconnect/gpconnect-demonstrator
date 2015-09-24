package org.rippleosi.patient.appointments.store;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultAppointmentStoreFactory extends AbstractRepositoryFactory<AppointmentStore> implements AppointmentStoreFactory {

    @Override
    protected AppointmentStore defaultRepository() {
        return new NotConfiguredAppointmentStore();
    }

    @Override
    protected Class<AppointmentStore> repositoryClass() {
        return AppointmentStore.class;
    }
}
