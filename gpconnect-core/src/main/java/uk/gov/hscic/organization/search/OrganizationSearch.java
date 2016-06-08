package uk.gov.hscic.organization.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.organization.model.OrganizationDetails;

public interface OrganizationSearch extends Repository {

    OrganizationDetails findOrganizationDetails(String organizationId);
}
