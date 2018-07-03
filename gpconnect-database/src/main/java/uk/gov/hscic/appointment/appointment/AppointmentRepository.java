package uk.gov.hscic.appointment.appointment;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByPatientId(Long patientId);
    AppointmentEntity findOneByIdAndLastUpdated(Long id, Date lastUpdated);
    
    /**
     * DO NOT USE.
     * Only for use on daily refresh of appointment/slot data.
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "ALTER TABLE appointment_appointments AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrementAppointmentTable();
    
}
