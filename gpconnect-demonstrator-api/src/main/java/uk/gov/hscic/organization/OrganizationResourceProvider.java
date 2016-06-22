package uk.gov.hscic.organization;

import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.util.ArrayList;
import java.util.List;
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
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No organization details found for organization ID: "+organizationId.getIdPart());
            throw new InternalErrorException("No organization details found for organization ID: "+organizationId.getIdPart(), operationalOutcome);
        }
        return organizaitonDetailsToOrganizationResourceConverter(organizationDetails);
    }
    
    @Search
    public List<Organization> getOrganizations(@RequiredParam(name = "organizationODS") String organizationODSCode, @OptionalParam(name = "siteODSCode") String siteODSCode) {
        
        RepoSource sourceType = RepoSourceType.fromString(null);
        OrganizationSearch organizationSearch = applicationContext.getBean(OrganizationSearchFactory.class).select(sourceType);
        ArrayList<Organization> organizations = new ArrayList();
        
        List<OrganizationDetails> organizationDetailsList = null;
        
        if(organizationODSCode != null && siteODSCode != null){
            organizationDetailsList = organizationSearch.findOrganizationDetailsByOrgODSCodeAndSiteODSCode(organizationODSCode, siteODSCode);
        } else if (organizationODSCode != null){
            organizationDetailsList = organizationSearch.findOrganizationDetailsByOrgODSCode(organizationODSCode);
        }
        
        if(organizationDetailsList != null){
            for(OrganizationDetails organizationDetails : organizationDetailsList){
                organizations.add(organizaitonDetailsToOrganizationResourceConverter(organizationDetails));
            }
        }
        
        return organizations;
    }
    
    public Organization organizaitonDetailsToOrganizationResourceConverter(OrganizationDetails organizationDetails){
        Organization organization = new Organization();
        organization.setId(String.valueOf(organizationDetails.getId()));
        organization.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-organization-code", organizationDetails.getOrgCode()));
        organization.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-site-code", organizationDetails.getSiteCode()));
        organization.setName(organizationDetails.getOrgName());
        return organization;
    }
}
