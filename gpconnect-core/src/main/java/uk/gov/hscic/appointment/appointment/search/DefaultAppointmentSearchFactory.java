package uk.gov.hscic.appointment.appointment.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

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