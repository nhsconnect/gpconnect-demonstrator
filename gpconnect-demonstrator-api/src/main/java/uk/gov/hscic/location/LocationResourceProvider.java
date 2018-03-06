package uk.gov.hscic.location;


import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Location.LocationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueTypeEnumFactory;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import java.util.LinkedList;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.StringType;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.model.location.LocationDetails;
import uk.gov.hscic.organization.OrganizationResourceProvider;

@Component
public class LocationResourceProvider implements IResourceProvider {

    @Autowired
    private LocationSearch locationSearch;
    
    @Autowired
    private OrganizationResourceProvider organizationSearch;
    
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Location.class;
    }
    
    public List<Location> getAllLocationDetails(){
        List<LocationDetails> allLocationDetails = locationSearch.findAllLocations();
            
        if (allLocationDetails.isEmpty()) {
            return null;
        }

        List<Location> results = allLocationDetails.stream().map(loc -> locationDetailsToLocation(loc)).collect(Collectors.toList());

        return results;
    }
    
    @Read(version = true)
    public Location getLocationById(@IdParam IdType locationId) {
        LocationDetails locationDetails = locationSearch.findLocationById(locationId.getIdPart());
       
        if (locationDetails == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No location details found for location ID: "+locationId.getIdPart())
                    ,SystemCode.REFERENCE_NOT_FOUND, IssueType.INCOMPLETE);
            
        }

        Location location = locationDetailsToLocation(locationDetails);
        List<StringType> list = new LinkedList<>();
        list.add(new StringType("Trevelyan Square"));
        list.add(new StringType("Boar Ln"));
        list.add(new StringType("Leeds"));
        location.setAddress(new Address().setPostalCode("LS1 6AE").setLine(list));
        return location;
    }

    private Location locationDetailsToLocation(LocationDetails locationDetails) {
        Location location = new Location();
        
        String resourceId = String.valueOf(locationDetails.getId());
        String versionId = String.valueOf(locationDetails.getLastUpdated().getTime());
        String resourceType = location.getResourceType().toString();
        
        IdType id = new IdType(resourceType, resourceId, versionId);

        location.setId(id);
        location.getMeta().setVersionId(versionId);
        location.getMeta().setLastUpdated(locationDetails.getLastUpdated());          
        location.getMeta().addProfile(SystemURL.SD_GPC_LOCATION);
        
        location.setName(locationDetails.getName());
        location.setIdentifier(Collections.singletonList(new Identifier().setSystem(SystemURL.ID_ODS_SITE_CODE).setValue(locationDetails.getSiteOdsCode())));
        Coding locationCommTypeCode = new Coding();
        locationCommTypeCode.setCode("COMM");
        locationCommTypeCode.setSystem(SystemURL.VS_CC_SER_DEL_LOCROLETYPE);
        locationCommTypeCode.setDisplay("Community Location");
        
        Coding locationGachTypeCode = new Coding();
        locationGachTypeCode.setCode("GACH");
        locationGachTypeCode.setSystem(SystemURL.VS_CC_SER_DEL_LOCROLETYPE);
        locationGachTypeCode.setDisplay("Hospitals; General Acute Care Hospital");
        
        @SuppressWarnings("deprecation")
        CodeableConcept locationType = new CodeableConcept();
        locationType.addCoding(locationCommTypeCode);
        locationType.addCoding(locationGachTypeCode);
        location.setType(locationType);
        
        Organization orgz = FindOrganization(locationDetails.getOrgOdsCode());
        
        if(orgz != null){
            Reference mngOrg = new Reference();
            mngOrg.setReference("Organization/" + orgz.getId());
            mngOrg.setDisplay(orgz.getName());
            location.setManagingOrganization(mngOrg);
        }
        
        EnumSet<LocationStatus> statusList = EnumSet.allOf(LocationStatus.class);
        LocationStatus locationStatus = null;
        String status = locationDetails.getStatus();
        
        if(status != null){
            for(LocationStatus statusItem : statusList){
            
                if(statusItem.toCode().equalsIgnoreCase(status)){
                    locationStatus = statusItem;
                    break;
                }
            }
        }
        
        location.setStatus(locationStatus);
        return location;
    }
    
    private Organization FindOrganization(String orgCode){
        
        List<Organization> orgz = organizationSearch.getOrganizationsByODSCode(new TokenParam(SystemURL.ID_ODS_ORGANIZATION_CODE, orgCode), null, null);
        
        if(null == orgz || orgz.isEmpty()){
            return null;
        }
        
        return orgz.get(0);
    }
}
