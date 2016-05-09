package uk.gov.hscic.patient.keyindicators.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.keyindicators.model.KeyIndicatorEntity;

public interface KeyIndicatorRepository extends JpaRepository<KeyIndicatorEntity, Long> {

}
