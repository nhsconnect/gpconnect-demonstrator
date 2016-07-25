package uk.gov.hscic.location;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.context.ApplicationContext;

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
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.location.model.LocationDetails;
import uk.gov.hscic.location.search.LocationSearch;
import uk.gov.hscic.location.search.LocationSearchFactory;

public class LocationResourceProvider implements IResourceProvider {

    ApplicationContext applicationContext;
    
	public LocationResourceProvider(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Location.class;
	}

	@SuppressWarnings("deprecation")
	@Search
	public Location getBySiteOdsCode(@RequiredParam(name = "siteODSCode") String siteODSCode) {
		RepoSource sourceType = RepoSourceType.fromString(null);
        LocationSearch locationSearch = applicationContext.getBean(LocationSearchFactory.class).select(sourceType);
        LocationDetails locationDetails = locationSearch.findLocationDetailsBySiteOdsCode(siteODSCode);
        
        if(locationDetails == null){
            String msg = String.format("No location details found for site ODS code: %s", siteODSCode);
        	
        	OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(msg);
            throw new InternalErrorException(msg, operationalOutcome);
        }
        
        return locationDetailsToLocation(locationDetails);
	}

    @Read()
    public Location getLocationById(@IdParam IdDt locationId) {
        
        RepoSource sourceType = RepoSourceType.fromString(null);
        LocationSearch locationSearch = applicationContext.getBean(LocationSearchFactory.class).select(sourceType);
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
