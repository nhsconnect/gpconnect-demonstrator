package uk.gov.hscic.organization.search;

import java.util.List;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.organization.model.OrganizationDetails;

public interface OrganizationSearch extends Repository {

    OrganizationDetails findOrganizationDetails(String organizationId);
    
    List<OrganizationDetails> findOrganizationDetailsByOrgODSCode(String organizationODSCode);
    
    List<OrganizationDetails> findOrganizationDetailsByOrgODSCodeAndSiteODSCode(String organizationODSCode, String siteODSCode);
}
