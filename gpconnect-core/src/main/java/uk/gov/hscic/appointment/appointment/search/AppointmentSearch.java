package uk.gov.hscic.appointment.appointment.search;

import java.util.Date;
import java.util.List;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.common.repo.Repository;

public interface AppointmentSearch extends Repository {
    
    AppointmentDetail findAppointmentByID(Long id);
    List<AppointmentDetail> findAppointmentForPatientId(Long patientId);
    List<AppointmentDetail> findAppointmentForPatientId(Long patientId, Date startDate);
    List<AppointmentDetail> findAppointmentForPatientId(Long patientId, Date startDate, Date endDate);
    
}