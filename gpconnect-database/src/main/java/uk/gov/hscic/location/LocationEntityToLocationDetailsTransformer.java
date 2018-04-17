package uk.gov.hscic.location;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.model.location.LocationDetails;

class LocationEntityToLocationDetailsTransformer implements Transformer<LocationEntity, LocationDetails> {

	@Override
	public LocationDetails transform(LocationEntity locationEntity) {
            
            LocationDetails locationDetails = null;

            if (locationEntity != null) {
                    locationDetails = new LocationDetails();

                    locationDetails.setId(locationEntity.getId());
                    locationDetails.setName(locationEntity.getName());
                    locationDetails.setOrgOdsCode(locationEntity.getOrgOdsCode());
                    locationDetails.setOrgOdsCodeName(locationEntity.getOrgOdsCodeName());
                    locationDetails.setSiteOdsCode(locationEntity.getSiteOdsCode());
                    locationDetails.setSiteOdsCodeName(locationEntity.getSiteOdsCodeName());
                    locationDetails.setLastUpdated(locationEntity.getLastUpdated());
                    locationDetails.setStatus(locationEntity.getStatus());
                    if (locationEntity.getAddress() != null) {
                    	if (locationEntity.getAddress().getLine() != null) {
                    		locationDetails.setAddressLine(locationEntity.getAddress().getLine());
                    	}
                    	if (locationEntity.getAddress().getCity() != null) {
                    		locationDetails.setAddressCity(locationEntity.getAddress().getCity());
                    	}
                    	if (locationEntity.getAddress().getDistrict() != null) {
                    		locationDetails.setAddressDistrict(locationEntity.getAddress().getDistrict());
                    	}
                    	if (locationEntity.getAddress().getState() != null) {
                    		locationDetails.setAddressState(locationEntity.getAddress().getState());
                    	}
                    	if (locationEntity.getAddress().getPostalCode() != null) {
                    		locationDetails.setAddressPostalCode(locationEntity.getAddress().getPostalCode());
                    	}
                    	if (locationEntity.getAddress().getCountry() != null) {
                    		locationDetails.setAddressCountry(locationEntity.getAddress().getCountry());
                    	}                    		
                    }
                    
            }

            return locationDetails;
	}
}
