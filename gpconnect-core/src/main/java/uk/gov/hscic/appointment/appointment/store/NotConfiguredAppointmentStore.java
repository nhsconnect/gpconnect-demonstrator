package uk.gov.hscic.appointment.appointment.store;

import java.util.List;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
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
    public AppointmentDetail saveAppointment(AppointmentDetail appoitmentDetail, List<SlotDetail> slots) {
        throw ConfigurationException.unimplementedTransaction(AppointmentStore.class);
    }


}
