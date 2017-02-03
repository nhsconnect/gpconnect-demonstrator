package uk.gov.hscic.patient.clinicalitems.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;

public interface ClinicalItemRepository extends JpaRepository<ClinicalItemEntity, Long> {
    List<ClinicalItemEntity> findBynhsNumberOrderBySectionDateDesc(Long patientNHSNumber);
    List<ClinicalItemEntity> findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long patientNHSNumber, Date startDate);
    List<ClinicalItemEntity> findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long patientNHSNumber, Date endDate);
    List<ClinicalItemEntity> findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(Long patientNHSNumber, Date startDate, Date endDate);
}
