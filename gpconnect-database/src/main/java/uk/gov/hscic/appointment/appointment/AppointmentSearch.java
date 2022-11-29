package uk.gov.hscic.appointment.appointment;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.model.appointment.AppointmentDetail;

@Service
public class AppointmentSearch {
    private final AppointmentEntityToAppointmentDetailTransformer transformer = new AppointmentEntityToAppointmentDetailTransformer();

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentDetail findAppointmentByID(Long id) {
        AppointmentEntity item = null;
        try {
            item = appointmentRepository.findById(id).get();
        } catch ( NoSuchElementException ex) {
        }

        return item == null
                ? null
                : transformer.transform(item);
    }
    
    public AppointmentDetail findAppointmentByIDAndLastUpdated(Long id, Date lastUpdated) {
        final AppointmentEntity item = appointmentRepository.findOneByIdAndLastUpdated(id, lastUpdated);;

        return item == null
                ? null
                : transformer.transform(item);
    }  

    public List<AppointmentDetail> searchAppointments(Long patientId, Date startLowerDate, Date startUpperDate) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .filter(appointmentEntity -> startLowerDate == null || !appointmentEntity.getStartDateTime().before(startLowerDate))
                .filter(appointmentEntity -> startUpperDate == null || !appointmentEntity.getStartDateTime().after(startUpperDate))
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
