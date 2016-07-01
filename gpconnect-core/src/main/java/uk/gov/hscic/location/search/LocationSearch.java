package uk.gov.hscic.location.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.location.model.LocationDetails;

public interface LocationSearch extends Repository {
	
	LocationDetails findLocationDetailsBySiteOdsCode(String siteOdsCode);
    
    LocationDetails findLocationById(String locationId);
}
