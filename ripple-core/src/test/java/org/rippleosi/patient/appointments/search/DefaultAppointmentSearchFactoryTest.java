package org.rippleosi.patient.appointments.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAppointmentSearchFactoryTest extends AbstractRepositoryFactoryTest<AppointmentSearchFactory, AppointmentSearch> {

    @Override
    protected AppointmentSearchFactory createRepositoryFactory() {
        return new DefaultAppointmentSearchFactory();
    }

    @Override
    protected Class<AppointmentSearch> getRepositoryClass() {
        return AppointmentSearch.class;
    }
}
