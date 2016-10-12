package uk.gov.hscic.location;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.location.model.LocationDetails;
import uk.gov.hscic.location.search.LocationSearch;
import uk.gov.hscic.location.search.LocationSearchFactory;

@Component
public class LocationResourceProvider implements IResourceProvider {

    @Autowired
    LocationSearchFactory locationSearchFactory;
    
	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Location.class;
	}

    @Search
	public List<Location> getByIdentifierCode(@RequiredParam(name=Location.SP_IDENTIFIER) TokenParam identifierCode) {
        
		RepoSource sourceType = RepoSourceType.fromString(null);
        LocationSearch locationSearch = locationSearchFactory.select(sourceType);
        
        List<LocationDetails> locationDetails = null;
        
        if("http://fhir.nhs.net/Id/ods-organization-code".equalsIgnoreCase(identifierCode.getSystem())){
            locationDetails = locationSearch.findLocationDetailsByOrgOdsCode(identifierCode.getValue());
        } else {
            locationDetails = locationSearch.findLocationDetailsBySiteOdsCode(identifierCode.getValue());
        }
        
        if(locationDetails.size() <= 0){
            String msg = String.format("No location details found for code: %s", identifierCode.getValue());
        	
        	OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(msg);
            throw new InternalErrorException(msg, operationalOutcome);
        }
        
        ArrayList<Location> locations = new ArrayList();
        for(LocationDetails locationDetail : locationDetails){
            locations.add(locationDetailsToLocation(locationDetail));
        }
        return locations;
	}

    @Read()
    public Location getLocationById(@IdParam IdDt locationId) {
        
        RepoSource sourceType = RepoSourceType.fromString(null);
        LocationSearch locationSearch = locationSearchFactory.select(sourceType);
        LocationDetails locationDetails = locationSearch.findLocationById(locationId.getIdPart());
        
        if(locationDetails == null){
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No location details found for location ID: "+locationId.getIdPart());
            throw new InternalErrorException("No location details found for location ID: "+locationId.getIdPart(), operationalOutcome);
        }
        return locationDetailsToLocation(locationDetails);
    }
    
	private Location locationDetailsToLocation(LocationDetails locationDetails) {
		Location location = new Location();
		
		List<IdentifierDt> identifier = new ArrayList<IdentifierDt>();
		identifier.add(new IdentifierDt(locationDetails.getSiteOdsCode(), locationDetails.getSiteOdsCodeName()));
		
		location.setId(new IdDt(locationDetails.getId()));
        location.getMeta().setLastUpdated(locationDetails.getLastUpdated());
        location.getMeta().setVersionId(String.valueOf(locationDetails.getLastUpdated().getTime()));
		location.setName(new StringDt(locationDetails.getName()));
		location.setIdentifier(identifier);
		
		return location;
	}
}
