package org.rippleosi.patient.appointments.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAppointmentStoreFactoryTest extends AbstractRepositoryFactoryTest<AppointmentStoreFactory, AppointmentStore> {

    @Override
    protected AppointmentStoreFactory createRepositoryFactory() {
        return new DefaultAppointmentStoreFactory();
    }

    @Override
    protected Class<AppointmentStore> getRepositoryClass() {
        return AppointmentStore.class;
    }
}
