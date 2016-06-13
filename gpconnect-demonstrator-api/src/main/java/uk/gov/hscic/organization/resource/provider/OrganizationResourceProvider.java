package uk.gov.hscic.organization.resource.provider;

import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.search.OrganizationSearch;
import uk.gov.hscic.organization.search.OrganizationSearchFactory;

public class OrganizationResourceProvider  implements IResourceProvider {
     
    ApplicationContext applicationContext;
     
    public OrganizationResourceProvider(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    
    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }
   
    @Read()
    public Organization getOrganizationById(@IdParam IdDt organizationId) {
        
        RepoSource sourceType = RepoSourceType.fromString(null);
        OrganizationSearch organizationSearch = applicationContext.getBean(OrganizationSearchFactory.class).select(sourceType);
        OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(organizationId.getIdPart());
        
        if(organizationDetails == null){
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No practitioner details found for practitioner ID: "+organizationId.getIdPart());
            throw new InternalErrorException("No practitioner details found for practitioner ID: "+organizationId.getIdPart(), operationalOutcome);
        }
        
        Organization organization = new Organization();
        
        organization.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-organization-code", organizationDetails.getOrgCode()));
        organization.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-site-code", organizationDetails.getSiteCode()));
        organization.setName(organizationDetails.getOrgName());
        
        return organization;
    }
}
