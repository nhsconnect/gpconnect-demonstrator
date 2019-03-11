package uk.gov.hscic.organization;

import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.search.OrganizationSearch;

@Component
public class OrganizationResourceProvider {

    @Autowired
    private OrganizationSearch organizationSearch;

    public Organization getOrganizationById(IdDt organizationId) {
        OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(organizationId.getIdPart());

        if (organizationDetails == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No organization details found for organization ID: " + organizationId.getIdPart());
            throw new InternalErrorException("No organization details found for organization ID: " + organizationId.getIdPart(), operationalOutcome);
        }

        return organizationDetailsToOrganizationResourceConverter(organizationDetails);
    }

    public Organization organizationDetailsToOrganizationResourceConverter(OrganizationDetails organizationDetails) {
        // #152 remove site code
        Organization organization = new Organization()
                .setName(organizationDetails.getOrgName())
                .addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-organization-code", organizationDetails.getOrgCode()))/*
                .addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-site-code", organizationDetails.getSiteCode()))*/;

        organization.setId(String.valueOf(organizationDetails.getId()));

        organization.getMeta()
                .addProfile(OperationConstants.META_GP_CONNECT_ORGANIZATION)
                .setLastUpdated(organizationDetails.getLastUpdated())
                .setVersionId(String.valueOf(organizationDetails.getLastUpdated().getTime()));

        // #152 add an address and telcon
        organization = addAdditionalProperties(organization);

        return organization;
    }

    private Organization addAdditionalProperties(Organization organization) {

        organization.addTelecom(getValidTelecom());
    	organization.addAddress(getValidAddress());

        return organization;
    }

    private ContactPointDt getValidTelecom() {

        ContactPointDt orgTelCom = new ContactPointDt();
 		orgTelCom.setSystem(ContactPointSystemEnum.PHONE);
		orgTelCom.setUse(ContactPointUseEnum.WORK);
        orgTelCom.setValue("12345678");

        return orgTelCom;
    }

	private AddressDt getValidAddress() {
		AddressDt orgAddress = new AddressDt();

		orgAddress.setUse(AddressUseEnum.WORK);
        orgAddress.addLine("NHS NPFIT");
        orgAddress.addLine("Test Data Manager");
        orgAddress.addLine("Princes Exchange");
        orgAddress.setCity("Leeds");
        orgAddress.setPostalCode("LS1 4HY");
        return orgAddress;
	}

}
