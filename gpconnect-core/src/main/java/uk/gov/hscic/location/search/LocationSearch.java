package uk.gov.hscic.location.search;

import java.net.URI;
import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.location.model.LocationDetails;

public interface LocationSearch extends Repository {
	
	LocationDetails findLocationDetailsByOdsSiteCode(URI odsSiteCode);
	List<LocationDetails> findLocationDetailsByManagingOrganisationOdsSiteCode(URI managingOrgOdsSiteCode);
}
