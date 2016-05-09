package uk.gov.hscic.patient.adminitems.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;

public interface AdminItemRepository extends JpaRepository<AdminItemEntity, Long> {

}
