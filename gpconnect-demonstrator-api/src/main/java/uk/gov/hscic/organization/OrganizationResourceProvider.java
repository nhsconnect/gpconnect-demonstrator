package uk.gov.hscic.organization;

import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Organization.Contact;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.model.location.LocationDetails;
import uk.gov.hscic.model.organization.OrganizationDetails;
import uk.gov.hscic.slots.PopulateSlotBundle;

@Component
public class OrganizationResourceProvider implements IResourceProvider {

    @Autowired
    public PopulateSlotBundle getScheduleOperation;

    @Autowired
    private OrganizationSearch organizationSearch;

    public static Set<String> getCustomReadOperations() {
        Set<String> customReadOperations = new HashSet<String>();
        return customReadOperations;
    }

    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Read(version = true)
    public Organization getOrganizationById(@IdParam IdDt organizationId) {

    	OrganizationDetails organizationDetails = null;

    	String idPart = organizationId.getIdPart();

    	try {
    		Long id = Long.parseLong(idPart);
    		organizationDetails = organizationSearch.findOrganizationDetails(id);
    		if (organizationDetails == null) {
    			throw OperationOutcomeFactory.buildOperationOutcomeException(
    					new ResourceNotFoundException("No organization details found for organization ID: " + idPart),
    					SystemCode.ORGANISATION_NOT_FOUND, IssueTypeEnum.INVALID_CONTENT);
    		}
    	}
    	catch(NumberFormatException nfe) {

			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new ResourceNotFoundException("No organization details found for organization ID: " + idPart),
					SystemCode.ORGANISATION_NOT_FOUND, IssueTypeEnum.INVALID_CONTENT);

    	}

        return IdentifierValidator.versionComparison(organizationId, convertOrganizaitonDetailsListToOrganizationList(Collections.singletonList(organizationDetails)).get(0));
    }

    @Search
    public List<Organization> getOrganizationsByODSCode(@RequiredParam(name = Organization.SP_IDENTIFIER) TokenParam tokenParam,
            @Sort SortSpec sort,
            @Count Integer count) {
        
        
        if (StringUtils.isBlank(tokenParam.getSystem()) || StringUtils.isBlank(tokenParam.getValue())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Missing identifier token"),
                    SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }

        switch (tokenParam.getSystem()) {
            case SystemURL.ID_ODS_ORGANIZATION_CODE:
                List<Organization> organizationDetails = convertOrganizaitonDetailsListToOrganizationList(organizationSearch.findOrganizationDetailsByOrgODSCode(tokenParam.getValue()));
                if (organizationDetails.isEmpty()) {
                    
                    return null;
                }
                if(sort != null && sort.getParamName().equalsIgnoreCase(Location.SP_STATUS)){
                    Collections.sort(organizationDetails, (Organization a, Organization b) -> {
                        
                        String aStatus = a.getName();
                        String bStatus = b.getName();
                        
                        if(aStatus == null && bStatus == null){
                            return 0;
                        }
                        
                        if(aStatus == null && bStatus != null){
                            return -1;
                        }
                        
                        if(aStatus != null && bStatus == null){
                            return 1;
                        }
                        
                        return aStatus.compareToIgnoreCase(bStatus);
                    });
                }

                //Update startIndex if we do paging
                return count != null ? organizationDetails.subList(0, count) : organizationDetails;
                 
            default:
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid system code"),
                        SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }
    }


  

    public List<Organization> convertOrganizaitonDetailsListToOrganizationList(List<OrganizationDetails> organizationDetails) {
        Map<String, Organization> map = new HashMap<>();

        for (OrganizationDetails organizationDetail : organizationDetails) {
            
            String mapKey = String.format("%s", organizationDetail.getOrgCode());
            if (map.containsKey(mapKey)) {
                continue;
            } 
            
            Organization organization = new Organization()
                    .setName(organizationDetail.getOrgName())
                    .addIdentifier(new IdentifierDt(SystemURL.ID_ODS_ORGANIZATION_CODE, organizationDetail.getOrgCode()));
                   
            organization.setId(String.valueOf(organizationDetail.getId()));

            organization.getMeta()
                    .addProfile(SystemURL.SD_GPC_ORGANIZATION)
                    .setLastUpdated(organizationDetail.getLastUpdated())
                    .setVersionId(String.valueOf(organizationDetail.getLastUpdated().getTime()));
            
            organization = addAdditionalProperties(organization);

            map.put(mapKey, organization);
            
        }

        return new ArrayList<>(map.values());
    }
    
    //Adding in additional properties manually for now so we can test in the Test Suite
    public Organization addAdditionalProperties(Organization organization){
        
        List<IdentifierDt> identifiers = organization.getIdentifier();
       
        CodingDt orgTypeCode = new CodingDt();
        orgTypeCode.setCode("dept");
        orgTypeCode.setDisplay("Hospital Department");
        orgTypeCode.setSystem(SystemURL.VS_CC_ORGANISATION_TYPE);
        
     
                    
        organization.addTelecom(getValidTelecom());
        organization.addAddress(getValidAddress());
        organization.addContact(getValidContact());

        CodeableConceptDt orgType = new CodeableConceptDt();
        orgType.addCoding(orgTypeCode);
        organization.setType(orgType);
        
        organization.addUndeclaredExtension(false, SystemURL.SD_EXTENSION_CC_MAIN_LOCATION);

        return organization;
    }
    
    
    private ContactPointDt getValidTelecom(){
        
        ContactPointDt orgTelCom = new ContactPointDt();
        orgTelCom.addUndeclaredExtension(false, "testurl");
        orgTelCom.setSystem(ContactPointSystemEnum.PHONE);
        orgTelCom.setUse(ContactPointUseEnum.WORK);
        
        return orgTelCom;
    }
    
    private AddressDt getValidAddress(){
        
        AddressDt orgAddress = new AddressDt();
        orgAddress.setType(AddressTypeEnum.PHYSICAL);
        orgAddress.setUse(AddressUseEnum.WORK);
        
        return orgAddress;
    }
    
    private Contact getValidContact(){
        
        HumanNameDt orgCtName = new HumanNameDt();
        orgCtName.setUse(NameUseEnum.USUAL);
        orgCtName.addFamily("FamilyName");
        
        CodeableConceptDt orgCtPurpose = new CodeableConceptDt(SystemURL.VS_CC_ORG_CT_ENTITYTYPE, "ADMIN");
        
        Contact orgContact = new Contact();
        orgContact.setName(orgCtName);
        orgContact.addTelecom(getValidTelecom());
        orgContact.setAddress(getValidAddress());
        orgContact.setPurpose(orgCtPurpose);
        
        return orgContact;
    }
}
