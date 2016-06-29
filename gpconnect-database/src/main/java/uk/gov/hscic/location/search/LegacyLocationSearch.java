package uk.gov.hscic.location.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.location.model.LocationDetails;
import uk.gov.hscic.location.model.LocationEntity;
import uk.gov.hscic.location.repo.LocationRepository;

@Service
public class LegacyLocationSearch extends AbstractLegacyService implements LocationSearch {

	@Autowired
	private LocationRepository locationRepository;
	
	private LocationEntityToLocationDetailsTransformer transformer = new LocationEntityToLocationDetailsTransformer();
	
	@Override
	public LocationDetails findLocationDetailsBySiteOdsCode(String siteOdsCode) {
		LocationDetails locationDetails = null;
		
		if(siteOdsCode != null) {
			LocationEntity locationEntity = locationRepository.getBySiteOdsCode(siteOdsCode);
			if(locationEntity != null) {
				locationDetails = transformer.transform(locationEntity);
			}
		}

		return locationDetails;
	}
}
