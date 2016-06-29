package uk.gov.hscic.appointment.appointment.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.appointment.appointment.model.AppointmentEntity;

public interface AppointmentRepository  extends JpaRepository<AppointmentEntity, Long> {
    
    List<AppointmentEntity> findByPatientId(Long patientId);
    
    List<AppointmentEntity> findByPatientIdAndEndDateTimeAfter(Long patientId, Date startDate);
    
    List<AppointmentEntity> findByPatientIdAndEndDateTimeAfterAndStartDateTimeBefore(Long patientId, Date startDate, Date endDate);
    
}
