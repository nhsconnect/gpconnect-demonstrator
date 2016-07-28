package uk.gov.hscic.appointment.appointment.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.appointment.model.AppointmentEntity;
import uk.gov.hscic.appointment.appointment.repo.AppointmentRepository;
import uk.gov.hscic.common.service.AbstractLegacyService;

@Service
public class LegacyAppointmentSearch extends AbstractLegacyService implements AppointmentSearch {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private final AppointmentEntityToAppointmentDetailTransformer transformer = new AppointmentEntityToAppointmentDetailTransformer();
    
    @Override
    public AppointmentDetail findAppointmentByID(Long id) {
        final AppointmentEntity item = appointmentRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }

    @Override
    public List<AppointmentDetail> findAppointmentForPatientId(Long patinetId) {
        List<AppointmentEntity> items = appointmentRepository.findByPatientId(patinetId);
        ArrayList<AppointmentDetail> appointmentDetails = new ArrayList();
        for(AppointmentEntity entity : items){
            appointmentDetails.add(transformer.transform(entity));
        }
        return appointmentDetails;
    }
    
    @Override
    public List<AppointmentDetail> findAppointmentForPatientId(Long patinetId, Date startDate) {
        List<AppointmentEntity> items = appointmentRepository.findByPatientIdAndEndDateTimeAfter(patinetId, startDate);
        ArrayList<AppointmentDetail> appointmentDetails = new ArrayList();
        for(AppointmentEntity entity : items){
            appointmentDetails.add(transformer.transform(entity));
        }
        return appointmentDetails;
    }
    
    @Override
    public List<AppointmentDetail> findAppointmentForPatientId(Long patinetId, Date startDate, Date endDate) {
        List<AppointmentEntity> items = appointmentRepository.findByPatientIdAndEndDateTimeAfterAndStartDateTimeBefore(patinetId, startDate, endDate);
        ArrayList<AppointmentDetail> appointmentDetails = new ArrayList();
        for(AppointmentEntity entity : items){
            appointmentDetails.add(transformer.transform(entity));
        }
        return appointmentDetails;
    }

    @Override
    public List<AppointmentDetail> searchAppointments(Long patientId, Date startLowerDate, Date startUpperDate){
        String queryParameter = "";
        List<AppointmentEntity> items = appointmentRepository.findByPatientId(patientId);
        ArrayList<AppointmentDetail> appointmentDetails = new ArrayList();
        for(AppointmentEntity entity : items){
            boolean addAppointment = true;
            if(startLowerDate != null && entity.getStartDateTime().before(startLowerDate)){ addAppointment = false; }
            if(startUpperDate != null && entity.getStartDateTime().after(startUpperDate)){ addAppointment = false; }
            if(addAppointment){
                appointmentDetails.add(transformer.transform(entity));
            }
        }
        return appointmentDetails;
    }
    
}
