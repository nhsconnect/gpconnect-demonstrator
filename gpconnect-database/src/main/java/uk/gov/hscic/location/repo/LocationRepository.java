package uk.gov.hscic.location.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import uk.gov.hscic.location.model.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    List<LocationEntity> findBySiteOdsCode(String siteOdsCode);
    List<LocationEntity> findByOrgOdsCode(String orgOdsCode);
}
