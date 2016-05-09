package uk.gov.hscic.patient.immunisations.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;

public interface ImmunisationRepository extends JpaRepository<ImmunisationEntity, Long> {

}
