package uk.gov.hscic.organization;

import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.search.OrganizationSearch;

@Component
public class OrganizationResourceProvider {

    @Autowired
    private OrganizationSearch organizationSearch;

    public Organization getOrganizationById(@IdParam IdDt organizationId) {
        OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(organizationId.getIdPart());

        if (organizationDetails == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No organization details found for organization ID: " + organizationId.getIdPart());
            throw new InternalErrorException("No organization details found for organization ID: " + organizationId.getIdPart(), operationalOutcome);
        }

        return organizaitonDetailsToOrganizationResourceConverter(organizationDetails);
    }

    public Organization organizaitonDetailsToOrganizationResourceConverter(OrganizationDetails organizationDetails) {
        Organization organization = new Organization()
                .setName(organizationDetails.getOrgName())
                .addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-organization-code", organizationDetails.getOrgCode()))
                .addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-site-code", organizationDetails.getSiteCode()));

        organization.setId(String.valueOf(organizationDetails.getId()));
        
        organization.getMeta()
                .addProfile(OperationConstants.META_GP_CONNECT_ORGANIZATION)
                .setLastUpdated(organizationDetails.getLastUpdated())
                .setVersionId(String.valueOf(organizationDetails.getLastUpdated().getTime()));

        return organization;
    }
}
