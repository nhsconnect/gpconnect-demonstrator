package uk.gov.hscic.appointment.appointment.store;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.appointment.model.AppointmentEntity;
import uk.gov.hscic.appointment.appointment.repo.AppointmentRepository;
import uk.gov.hscic.appointment.appointment.search.AppointmentEntityToAppointmentDetailTransformer;
import uk.gov.hscic.appointment.slot.model.SlotDetail;

@Service
public class AppointmentStore {
    private final AppointmentEntityToAppointmentDetailTransformer entityToDetailTransformer = new AppointmentEntityToAppointmentDetailTransformer();
    private final AppointmentDetailToAppointmentEntityTransformer detailToEntityTransformer = new AppointmentDetailToAppointmentEntityTransformer();

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentDetail saveAppointment(AppointmentDetail appointment, List<SlotDetail> slots){
        AppointmentEntity appointmentEntity = detailToEntityTransformer.transform(appointment, slots);
        appointmentEntity = appointmentRepository.saveAndFlush(appointmentEntity);
        return entityToDetailTransformer.transform(appointmentEntity);
    }

    public void clearAppointments(){
        appointmentRepository.deleteAll();
    }
}
