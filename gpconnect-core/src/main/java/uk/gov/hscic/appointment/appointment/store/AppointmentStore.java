package uk.gov.hscic.appointment.appointment.store;

import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.common.repo.Repository;

public interface AppointmentStore  extends Repository {
    
    AppointmentDetail saveAppointment(AppointmentDetail appoitmentDetail);
    
}