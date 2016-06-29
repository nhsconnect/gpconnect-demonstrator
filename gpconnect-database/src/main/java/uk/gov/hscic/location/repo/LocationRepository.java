package uk.gov.hscic.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.gov.hscic.location.model.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
	LocationEntity getBySiteOdsCode(String siteOdsCode);
}
