package uk.gov.hscic.location.search;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.location.model.LocationDetails;
import uk.gov.hscic.location.model.LocationEntity;
import uk.gov.hscic.location.repo.LocationRepository;

@Service
public class LocationSearch {
	private final LocationEntityToLocationDetailsTransformer transformer = new LocationEntityToLocationDetailsTransformer();

	@Autowired
	private LocationRepository locationRepository;

	public List<LocationDetails> findLocationDetailsBySiteOdsCode(String siteOdsCode) {
		return locationRepository.findBySiteOdsCode(siteOdsCode)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
	}

	public List<LocationDetails> findLocationDetailsByOrgOdsCode(String orgOdsCode) {
        return locationRepository.findByOrgOdsCode(orgOdsCode)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
	}

    public LocationDetails findLocationById(final String locationId) {
        final LocationEntity item = locationRepository.findById(Long.parseLong(locationId)).get();

        return item == null
                ? null
                : transformer.transform(item);
    }
}
