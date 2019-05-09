package uk.gov.hscic.location;


import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Location.LocationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        return locationDetailsToLocation(locationDetails);
    }

    /**
     * convert locationDetails to fhir resource
     * @param locationDetails
     * @return Location resource
     */
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
        // #207 no site code
        //location.setIdentifier(Collections.singletonList(new Identifier().setSystem(SystemURL.ID_ODS_SITE_CODE).setValue(locationDetails.getSiteOdsCode())));
        
        // #246 remove type element
//        Coding locationCommTypeCode = new Coding();
//        locationCommTypeCode.setCode("COMM");
//        locationCommTypeCode.setSystem(SystemURL.VS_CC_SER_DEL_LOCROLETYPE);
//        locationCommTypeCode.setDisplay("Community Location");
//        
//        Coding locationGachTypeCode = new Coding();
//        locationGachTypeCode.setCode("GACH");
//        locationGachTypeCode.setSystem(SystemURL.VS_CC_SER_DEL_LOCROLETYPE);
//        locationGachTypeCode.setDisplay("Hospitals; General Acute Care Hospital");
//        
//        @SuppressWarnings("deprecation")
//        CodeableConcept locationType = new CodeableConcept();
//        locationType.addCoding(locationCommTypeCode);
//        locationType.addCoding(locationGachTypeCode);
//        location.setType(locationType);
        
        Organization orgz = FindOrganization(locationDetails.getOrgOdsCode());
        
        if(orgz != null){
            Reference mngOrg = new Reference();
            mngOrg.setReference(orgz.getId());
            // #246 remove display element
//          mngOrg.setDisplay(orgz.getName());
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
        
        location.setAddress(createAddress(locationDetails));
        
        location.setStatus(locationStatus);
        return location;
    }

    /**
     * Some of the assignments look rather odd but they are deliberate.
     * They result from a change to spec to remove the state attribute from the address
     * See the commit cd26528 by James Cox 6/3/18 see also OrganizationResourceProvider.getValidAddress 
     * @param locationDetails
     * @return Address Resource
     */
	private Address createAddress(LocationDetails locationDetails) {
		Address address = new Address();
        List<StringType> list = new LinkedList<>();
        list.add(new StringType(locationDetails.getAddressLine()));
        list.add(new StringType(locationDetails.getAddressCity()));
        address.setLine(list);
        address.setCity(locationDetails.getAddressDistrict());
        address.setDistrict(locationDetails.getAddressState());
        address.setPostalCode(locationDetails.getAddressPostalCode());
        address.setCountry(locationDetails.getAddressCountry());
		return address;
	}
    
    private Organization FindOrganization(String orgCode){
        
        List<Organization> orgz = organizationSearch.getOrganizationsByODSCode(new TokenParam(SystemURL.ID_ODS_ORGANIZATION_CODE, orgCode), null, null);
        
        if(null == orgz || orgz.isEmpty()){
            return null;
        }
        
        return orgz.get(0);
    }
}
