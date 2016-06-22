package uk.gov.hscic.organization.search;

import java.util.List;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.organization.model.OrganizationDetails;

public class NotConfiguredOrganizationSearch implements OrganizationSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public OrganizationDetails findOrganizationDetails(final String organizationId) {
        throw ConfigurationException.unimplementedTransaction(OrganizationSearch.class);
    }

    @Override
    public List<OrganizationDetails> findOrganizationDetailsByOrgODSCode(String organizationODSCode) {
        throw ConfigurationException.unimplementedTransaction(OrganizationSearch.class);
    }

    @Override
    public List<OrganizationDetails> findOrganizationDetailsByOrgODSCodeAndSiteODSCode(String organizationODSCode, String siteODSCode) {
        throw ConfigurationException.unimplementedTransaction(OrganizationSearch.class);
    }
}