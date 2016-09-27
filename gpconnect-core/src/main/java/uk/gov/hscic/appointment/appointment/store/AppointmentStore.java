package uk.gov.hscic.appointment.appointment.store;

import java.util.List;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.common.repo.Repository;

public interface AppointmentStore  extends Repository {
    
    AppointmentDetail saveAppointment(AppointmentDetail appoitmentDetail, List<SlotDetail> slots);
    
}