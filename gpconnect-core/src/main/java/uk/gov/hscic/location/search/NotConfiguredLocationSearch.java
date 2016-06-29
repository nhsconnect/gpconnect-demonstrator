package uk.gov.hscic.location.search;

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
	public LocationDetails findLocationDetailsBySiteOdsCode(String siteOdsCode) {
        throw ConfigurationException.unimplementedTransaction(LocationSearch.class);
	}
}