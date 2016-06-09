package uk.gov.hscic.practitioner.repo;

import uk.gov.hscic.practitioner.model.PractitionerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PractitionerRepository extends JpaRepository<PractitionerEntity, Long> {

}
