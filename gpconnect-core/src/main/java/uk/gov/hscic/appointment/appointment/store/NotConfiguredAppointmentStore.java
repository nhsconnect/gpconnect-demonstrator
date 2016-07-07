package uk.gov.hscic.appointment.appointment.store;

import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

public class NotConfiguredAppointmentStore implements AppointmentStore {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public AppointmentDetail saveAppointment(AppointmentDetail appoitmentDetail) {
        throw ConfigurationException.unimplementedTransaction(AppointmentStore.class);
    }


}
