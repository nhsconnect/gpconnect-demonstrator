package uk.gov.hscic.patient.practitioner.repo;

import uk.gov.hscic.patient.practitioner.model.PractitionerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PractitionerRepository extends JpaRepository<PractitionerEntity, Long> {

}
