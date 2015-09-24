package org.rippleosi.patient.appointments.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultAppointmentSearchFactory extends AbstractRepositoryFactory<AppointmentSearch> implements AppointmentSearchFactory {

    @Override
    protected AppointmentSearch defaultRepository() {
        return new NotConfiguredAppointmentSearch();
    }

    @Override
    protected Class<AppointmentSearch> repositoryClass() {
        return AppointmentSearch.class;
    }
}
