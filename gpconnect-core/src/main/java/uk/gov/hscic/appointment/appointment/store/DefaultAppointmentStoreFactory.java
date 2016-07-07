package uk.gov.hscic.appointment.appointment.store;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultAppointmentStoreFactory  extends AbstractRepositoryFactory<AppointmentStore> implements AppointmentStoreFactory {

    @Override
    protected AppointmentStore defaultRepository() {
        return new NotConfiguredAppointmentStore();
    }

    @Override
    protected Class<AppointmentStore> repositoryClass() {
        return AppointmentStore.class;
    }
}