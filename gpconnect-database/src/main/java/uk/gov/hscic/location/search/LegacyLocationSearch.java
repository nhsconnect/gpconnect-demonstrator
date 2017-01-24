package uk.gov.hscic.location.search;

import java.util.ArrayList;
import java.util.List;
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
	public List<LocationDetails> findLocationDetailsBySiteOdsCode(String siteOdsCode) {
		List<LocationEntity> locationEntities = locationRepository.findBySiteOdsCode(siteOdsCode);
        ArrayList<LocationDetails> locationDetails = new ArrayList<>();
        for(LocationEntity location : locationEntities){
            locationDetails.add(transformer.transform(location));
        }
		return locationDetails;
	}

    @Override
	public List<LocationDetails> findLocationDetailsByOrgOdsCode(String orgOdsCode) {
        List<LocationEntity> locationEntities = locationRepository.findByOrgOdsCode(orgOdsCode);
        ArrayList<LocationDetails> locationDetails = new ArrayList<>();
        for(LocationEntity location : locationEntities){
            locationDetails.add(transformer.transform(location));
        }
		return locationDetails;
	}

    @Override
    public LocationDetails findLocationById(final String locationId) {
        final LocationEntity item = locationRepository.findOne(Long.parseLong(locationId));
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }
}
