package uk.gov.hscic.patient.investigations.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;

public interface InvestigationRepository extends JpaRepository<InvestigationEntity, Long> {
    List<InvestigationEntity> findByNhsNumber(String nhsNumber);
}
