package uk.gov.hscic.appointment.appointment.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.appointment.model.AppointmentEntity;
import uk.gov.hscic.appointment.appointment.repo.AppointmentRepository;
import uk.gov.hscic.appointment.appointment.search.AppointmentEntityToAppointmentDetailTransformer;
import uk.gov.hscic.common.service.AbstractLegacyService;

@Service
public class LegacyAppointmentStore extends AbstractLegacyService implements AppointmentStore {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    private final AppointmentEntityToAppointmentDetailTransformer entityToDetailTransformer = new AppointmentEntityToAppointmentDetailTransformer();
    private final AppointmentDetailToAppointmentEntityTransformer detailToEntityTransformer = new AppointmentDetailToAppointmentEntityTransformer();
        
    @Override
    @Modifying
    @Transactional
    public AppointmentDetail saveAppointment(AppointmentDetail appointment){
        AppointmentEntity appointmentEntity = detailToEntityTransformer.transform(appointment);
        appointmentEntity = appointmentRepository.saveAndFlush(appointmentEntity);
        return entityToDetailTransformer.transform(appointmentEntity);
    }
    
}
