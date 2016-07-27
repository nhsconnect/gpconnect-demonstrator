package uk.gov.hscic.location.search;

import java.util.List;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.location.model.LocationDetails;

public interface LocationSearch extends Repository {
	
	List<LocationDetails> findLocationDetailsBySiteOdsCode(String siteOdsCode);
    List<LocationDetails> findLocationDetailsByOrgOdsCode(String orgOdsCode);
    
    LocationDetails findLocationById(String locationId);
}
