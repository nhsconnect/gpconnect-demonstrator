package uk.gov.hscic.appointment.healthcareservice;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthcareServiceRepository extends JpaRepository<HealthcareServiceEntity, Long> {
    List<HealthcareServiceEntity> findByIdentifier(String identifier);
    List<HealthcareServiceEntity> findAll();
    HealthcareServiceEntity getById(Long healthcareServiceId);
}
