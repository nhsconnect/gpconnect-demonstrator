package uk.gov.hscic.organization.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.model.OrganizationEntity;

public class OrganizationEntityToObjectTransformer implements Transformer<OrganizationEntity, OrganizationDetails> {

    @Override
    public OrganizationDetails transform(final OrganizationEntity organizationEntity) {
        
        OrganizationDetails organization = new OrganizationDetails();
        
        organization.setId(organizationEntity.getId());
        organization.setOrgCode(organizationEntity.getOrgCode());
        organization.setSiteCode(organizationEntity.getSiteCode());
        organization.setOrgName(organizationEntity.getOrgName());
        
        return organization;
        
    }
}
