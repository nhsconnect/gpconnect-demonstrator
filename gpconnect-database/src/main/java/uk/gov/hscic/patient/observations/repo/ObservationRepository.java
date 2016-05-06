package uk.gov.hscic.patient.observations.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.observations.model.ObservationEntity;

public interface ObservationRepository extends JpaRepository<ObservationEntity, Long> {

}
