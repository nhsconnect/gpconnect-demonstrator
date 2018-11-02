package uk.gov.hscic.organization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.model.organization.OrganizationDetails;
import uk.gov.hscic.slots.PopulateSlotBundle;

@Component
public class OrganizationResourceProvider implements IResourceProvider {

	@Autowired
	public PopulateSlotBundle getScheduleOperation;

	@Autowired
	private OrganizationSearch organizationSearch;

	public static Set<String> getCustomReadOperations() {
		Set<String> customReadOperations = new HashSet<>();
		return customReadOperations;
	}

	@Override
	public Class<Organization> getResourceType() {
		return Organization.class;
	}

	@Read(version = true)
	public Organization getOrganizationById(@IdParam IdType organizationId) {

		OrganizationDetails organizationDetails = null;

		String idPart = organizationId.getIdPart();

		try {
			Long id = Long.parseLong(idPart);
			organizationDetails = organizationSearch.findOrganizationDetails(id);
			if (organizationDetails == null) {
				throw OperationOutcomeFactory.buildOperationOutcomeException(
						new ResourceNotFoundException("No organization details found for organization ID: " + idPart),
						SystemCode.ORGANISATION_NOT_FOUND, IssueType.INVALID);
			}
		} catch (NumberFormatException nfe) {

			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new ResourceNotFoundException("No organization details found for organization ID: " + idPart),
					SystemCode.ORGANISATION_NOT_FOUND, IssueType.INVALID);

		}

		return IdentifierValidator.versionComparison(organizationId,
				convertOrganizatonDetailsListToOrganizationList(Collections.singletonList(organizationDetails))
						.get(0));
	}

	@Search
	public List<Organization> getOrganizationsByODSCode(
			@RequiredParam(name = Organization.SP_IDENTIFIER) TokenParam tokenParam, @Sort SortSpec sort,
			@Count Integer count) {

		if (StringUtils.isBlank(tokenParam.getSystem()) || StringUtils.isBlank(tokenParam.getValue())) {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new InvalidRequestException("Missing identifier token"), SystemCode.INVALID_PARAMETER,
					IssueType.INVALID);
		}

		if (tokenParam.getSystem().equals(SystemURL.ID_ODS_ORGANIZATION_CODE)
				|| tokenParam.getSystem().equals(SystemURL.ID_ODS_OLD_ORGANIZATION_CODE)) {
			List<Organization> organizationDetails = convertOrganizatonDetailsListToOrganizationList(
					organizationSearch.findOrganizationDetailsByOrgODSCode(tokenParam.getValue()));
            if (organizationDetails.isEmpty()) {
				return organizationDetails;
			}
			if (sort != null && sort.getParamName().equalsIgnoreCase(Location.SP_STATUS)) {
				Collections.sort(organizationDetails, (Organization a, Organization b) -> {

					String aStatus = a.getName();
					String bStatus = b.getName();

					if (aStatus == null && bStatus == null) {
						return 0;
					}

					if (aStatus == null && bStatus != null) {
						return -1;
					}

					if (aStatus != null && bStatus == null) {
						return 1;
					}

					return aStatus.compareToIgnoreCase(bStatus);
				});
			}

			// Update startIndex if we do paging
			return count != null ? organizationDetails.subList(0, count) : organizationDetails;

		} else {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new InvalidRequestException("Invalid system code"), SystemCode.INVALID_PARAMETER,
					IssueType.INVALID);
		}

	}

	private List<Organization> convertOrganizatonDetailsListToOrganizationList(
			List<OrganizationDetails> organizationDetails) {
		Map<Long, Organization> map = new HashMap<>();

		for (OrganizationDetails organizationDetail : organizationDetails) {
            
            // Changed key to be logical id rather than ods code since can have > 1 org per ods code
            // for 1.2.2
			Long mapKey = organizationDetail.getId();
			if (map.containsKey(mapKey)) {
				continue;
			}

			Identifier identifier = new Identifier().setSystem(SystemURL.ID_ODS_ORGANIZATION_CODE)
					.setValue(organizationDetail.getOrgCode());

			Organization organization = new Organization().setName(organizationDetail.getOrgName())
					.addIdentifier(identifier);

			String resourceId = String.valueOf(organizationDetail.getId());
			String versionId = String.valueOf(organizationDetail.getLastUpdated().getTime());
			String resourceType = organization.getResourceType().toString();

			IdType id = new IdType(resourceType, resourceId, versionId);

			organization.setId(id);
			organization.getMeta().setVersionId(versionId);
			organization.getMeta().setLastUpdated(organizationDetail.getLastUpdated());
			organization.getMeta().addProfile(SystemURL.SD_GPC_ORGANIZATION);

			organization = addAdditionalProperties(organization);

			map.put(mapKey, organization);

		}

		return new ArrayList<>(map.values());
	}

	public Organization convertOrganizaitonDetailsToOrganization(OrganizationDetails organizationDetails) {

		String mapKey = String.format("%s", organizationDetails.getOrgCode());

		Identifier identifier = new Identifier().setSystem(SystemURL.ID_ODS_ORGANIZATION_CODE)
				.setValue(organizationDetails.getOrgCode());

		Organization organization = new Organization().setName(organizationDetails.getOrgName())
				.addIdentifier(identifier);

		String resourceId = String.valueOf(organizationDetails.getId());
		String versionId = String.valueOf(organizationDetails.getLastUpdated().getTime());
		String resourceType = organization.getResourceType().toString();

		IdType id = new IdType(resourceType, resourceId, versionId);

		organization.setId(id);
		organization.getMeta().setVersionId(versionId);
		organization.getMeta().setLastUpdated(organizationDetails.getLastUpdated());
		organization.getMeta().addProfile(SystemURL.SD_GPC_ORGANIZATION);

		organization = addAdditionalProperties(organization);

		return organization;
	}

	// Adding in additional properties manually for now so we can test in the
	// Test Suite
	private Organization addAdditionalProperties(Organization organization) {

		Coding orgTypeCode = new Coding();
		orgTypeCode.setCode("dept");
		orgTypeCode.setDisplay("Hospital Department");
		orgTypeCode.setSystem(SystemURL.VS_CC_ORGANISATION_TYPE);

		organization.addTelecom(getValidTelecom());
		organization.addAddress(getValidAddress());
		// organization.addContact(getValidContact());

		CodeableConcept orgType = new CodeableConcept();
		orgType.addCoding(orgTypeCode);
		organization.addType(orgType);

		organization.addExtension().setUrl(SystemURL.SD_EXTENSION_CC_MAIN_LOCATION);

		return organization;
	}

	private ContactPoint getValidTelecom() {

		ContactPoint orgTelCom = new ContactPoint();
		orgTelCom.addExtension().setUrl("testUrl");
		orgTelCom.setSystem(ContactPointSystem.PHONE);
		orgTelCom.setUse(ContactPointUse.WORK);

		return orgTelCom;
	}

	private Address getValidAddress() {

		Address orgAddress = new Address();
		orgAddress.setType(AddressType.PHYSICAL);
		orgAddress.setUse(AddressUse.WORK);

		return orgAddress;
	}

	private ContactDetail getValidContact() {

		HumanName orgCtName = new HumanName();
		orgCtName.setUse(NameUse.USUAL);
		orgCtName.setFamily("FamilyName");
		Coding coding = new Coding().setSystem(SystemURL.VS_CC_ORG_CT_ENTITYTYPE).setDisplay("ADMIN");
		CodeableConcept orgCtPurpose = new CodeableConcept().addCoding(coding);

		ContactDetail orgContact = new ContactDetail();

		orgContact.setNameElement(orgCtName.getFamilyElement());
		orgContact.addTelecom(getValidTelecom());
		// orgContact.setAddress(getValidAddress());
		// orgContact.setPurpose(orgCtPurpose);

		return orgContact;
	}
}
