package uk.gov.hscic.location;


import ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.LocationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
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
import java.util.ArrayList;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.model.location.LocationDetails;
import uk.gov.hscic.model.organization.OrganizationDetails;
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

    @Search
    public List<Location> getByIdentifierCode(@RequiredParam(name = Location.SP_IDENTIFIER) TokenParam identifierCode, 
                                              @Sort SortSpec sort,
                                              @Count Integer count) {
        
        List<String> idefList = new ArrayList<>();
        idefList.add(SystemURL.ID_ODS_SITE_CODE);
        //Currently not supported in the demonstrator
        //idefList.add(SystemURL.ID_LOCAL_LOCATION_IDENTIFIER);
        
        if (!idefList.contains(identifierCode.getSystem()))
        {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Identifier System is invalid"),
                    SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueTypeEnum.INVALID_CONTENT, 
                    String.format("Identifier System must be one of: %s", String.join(", ", idefList)));
        }
        
        List<LocationDetails> locationDetails = SystemURL.ID_LOCAL_LOCATION_IDENTIFIER.equalsIgnoreCase(identifierCode.getSystem())
                ? locationSearch.findLocationDetailsByOrgOdsCode(identifierCode.getValue())
                : locationSearch.findLocationDetailsBySiteOdsCode(identifierCode.getValue());

        if (locationDetails.isEmpty()) {
           
            return null;
        }
        
        List<Location> results = locationDetails.stream().map(loc -> locationDetailsToLocation(loc)).collect(Collectors.toList());
        
        if(sort != null && sort.getParamName().equalsIgnoreCase(Location.SP_STATUS)){
            Collections.sort(results, (Location a, Location b) -> {
                
                String aStatus = a.getStatus();
                String bStatus = b.getStatus();
                
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
        return count != null ? results.subList(0, count) : results;
    }
    
    public List<Location> getByIdentifierOrgCode(String odsOrgCode) {
        
       
        List<LocationDetails> locationDetails = locationSearch.findLocationDetailsByOrgOdsCode(odsOrgCode);

        if (locationDetails.isEmpty()) {
           
            return null;
        }
        
        List<Location> results = locationDetails.stream().map(loc -> locationDetailsToLocation(loc)).collect(Collectors.toList());
        
        return results;
    }

    @Read(version = true)
    public Location getLocationById(@IdParam IdDt locationId) {
        LocationDetails locationDetails = locationSearch.findLocationById(locationId.getIdPart());
       
        if (locationDetails == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No location details found for location ID: "+locationId.getIdPart())
                    ,SystemCode.REFERENCE_NOT_FOUND,IssueTypeEnum.EXCEPTION);
            
        }

        return IdentifierValidator.versionComparison(locationId, locationDetailsToLocation(locationDetails));
    }

    private Location locationDetailsToLocation(LocationDetails locationDetails) {
        Location location = new Location();
        location.setId(new IdDt(locationDetails.getId()));
        location.getMeta().setLastUpdated(locationDetails.getLastUpdated());
        location.getMeta().setVersionId(String.valueOf(locationDetails.getLastUpdated().getTime()));
        location.getMeta().addProfile(SystemURL.SD_GPC_LOCATION);
        location.setName(new StringDt(locationDetails.getName()));
        location.setIdentifier(Collections.singletonList(new IdentifierDt(SystemURL.ID_ODS_SITE_CODE,locationDetails.getSiteOdsCode())));
        
        CodingDt locationCommTypeCode = new CodingDt();
        locationCommTypeCode.setCode("COMM");
        locationCommTypeCode.setSystem(SystemURL.VS_CC_SER_DEL_LOCROLETYPE);
        locationCommTypeCode.setDisplay("Community Location");
        
        CodingDt locationGachTypeCode = new CodingDt();
        locationGachTypeCode.setCode("GACH");
        locationGachTypeCode.setSystem(SystemURL.VS_CC_SER_DEL_LOCROLETYPE);
        locationGachTypeCode.setDisplay("Hospitals; General Acute Care Hospital");
        
        BoundCodeableConceptDt locationType = new BoundCodeableConceptDt();
        locationType.addCoding(locationCommTypeCode);
        locationType.addCoding(locationGachTypeCode);
        location.setType(locationType);
        
        Organization orgz = FindOrganization(locationDetails.getOrgOdsCode());
        
        if(orgz != null){
            ResourceReferenceDt mngOrg = new ResourceReferenceDt();
            mngOrg.setReference("Organization/" + orgz.getId());
            mngOrg.setDisplay(orgz.getName());
            location.setManagingOrganization(mngOrg);
        }
        
        EnumSet<LocationStatusEnum> statusList = EnumSet.allOf(LocationStatusEnum.class);
        LocationStatusEnum locationStatus = null;
        String status = locationDetails.getStatus();
        
        if(status != null){
            for(LocationStatusEnum statusItem : statusList){
            
                if(statusItem.getCode().equalsIgnoreCase(status)){
                    locationStatus = statusItem;
                    break;
                }
            }
        }
        
        location.setStatus(locationStatus);
        return location;
    }
    
    private Organization FindOrganization(String orgCode){
        
        List<Organization> orgz = organizationSearch.getOrganizationsByODSCode(new TokenParam(SystemURL.ID_ODS_ORGANIZATION_CODE, orgCode));
        
        if(!orgz.isEmpty()){
            return orgz.get(0);
        }
        
        return null;
    }
}
