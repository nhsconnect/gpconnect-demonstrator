package uk.gov.hscic.appointment.appointment.search;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.appointment.model.AppointmentDetail;
import uk.gov.hscic.appointment.appointment.model.AppointmentEntity;
import uk.gov.hscic.appointment.appointment.repo.AppointmentRepository;

@Service
public class AppointmentSearch {
    private final AppointmentEntityToAppointmentDetailTransformer transformer = new AppointmentEntityToAppointmentDetailTransformer();

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentDetail findAppointmentByID(Long id) {
        final AppointmentEntity item = appointmentRepository.findOne(id);

        return item == null
                ? null
                : transformer.transform(item);
    }

    public List<AppointmentDetail> findAppointmentForPatientId(Long patinetId) {
        return appointmentRepository.findByPatientId(patinetId)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }

    public List<AppointmentDetail> findAppointmentForPatientId(Long patinetId, Date startDate) {
        return appointmentRepository.findByPatientIdAndEndDateTimeAfter(patinetId, startDate)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }

    public List<AppointmentDetail> findAppointmentForPatientId(Long patinetId, Date startDate, Date endDate) {
        return appointmentRepository.findByPatientIdAndEndDateTimeAfterAndStartDateTimeBefore(patinetId, startDate, endDate)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
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
