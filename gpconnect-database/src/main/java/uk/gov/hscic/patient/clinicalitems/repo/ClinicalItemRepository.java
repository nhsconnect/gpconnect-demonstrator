package uk.gov.hscic.patient.clinicalitems.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;

public interface ClinicalItemRepository extends JpaRepository<ClinicalItemEntity, Long> {

}
