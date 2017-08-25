package uk.gov.hscic.location;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import java.util.Collections;
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

@Component
public class LocationResourceProvider implements IResourceProvider {

    @Autowired
    private LocationSearch locationSearch;
    
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Location.class;
    }

    @Search
    public List<Location> getByIdentifierCode(@RequiredParam(name = Location.SP_IDENTIFIER) TokenParam identifierCode) {
        
        if (!identifierCode.getSystem().equals(SystemURL.ID_ODS_SITE_CODE) && !identifierCode.getSystem().equals(SystemURL.ID_SDS_ROLE_PROFILE_ID))
        {
         
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Identifier System is invalid"),
                    SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueTypeEnum.INVALID_CONTENT);
          
        }
        
        List<LocationDetails> locationDetails = SystemURL.ID_ODS_ORGANIZATION_CODE.equalsIgnoreCase(identifierCode.getSystem())
                ? locationSearch.findLocationDetailsByOrgOdsCode(identifierCode.getValue())
                : locationSearch.findLocationDetailsBySiteOdsCode(identifierCode.getValue());

        if (locationDetails.isEmpty()) {
           
            return null;
        }

        return locationDetails.stream()
                .map(LocationResourceProvider::locationDetailsToLocation)
                .collect(Collectors.toList());
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

    private static Location locationDetailsToLocation(LocationDetails locationDetails) {
        Location location = new Location();
        location.setId(new IdDt(locationDetails.getId()));
        location.getMeta().setLastUpdated(locationDetails.getLastUpdated());
        location.getMeta().setVersionId(String.valueOf(locationDetails.getLastUpdated().getTime()));
        location.getMeta().addProfile(SystemURL.SD_GPC_LOCATION);
        location.setName(new StringDt(locationDetails.getName()));
        location.setIdentifier(Collections.singletonList(new IdentifierDt(SystemURL.ID_ODS_SITE_CODE,locationDetails.getSiteOdsCode())));
        return location;
    }
}
