package uk.gov.hscic.location.search;

import java.util.List;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.location.model.LocationDetails;

public class NotConfiguredLocationSearch implements LocationSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
	public List<LocationDetails> findLocationDetailsBySiteOdsCode(String siteOdsCode) {
        throw ConfigurationException.unimplementedTransaction(LocationSearch.class);
	}

    @Override
	public List<LocationDetails> findLocationDetailsByOrgOdsCode(String orgOdsCode) {
        throw ConfigurationException.unimplementedTransaction(LocationSearch.class);
	}
    
    @Override
    public LocationDetails findLocationById(String locationId) {
        throw ConfigurationException.unimplementedTransaction(LocationSearch.class);
    }
}