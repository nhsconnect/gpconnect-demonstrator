package uk.gov.hscic.common.validators;

import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

public class IdentifierValidator {

    public static <T extends BaseResource> T versionComparison(IdDt id, T resource) {
        if(id.hasVersionIdPart()){
            String requestedVersion = id.getVersionIdPart();
            if(!requestedVersion.equals(resource.getMeta().getVersionId())){
                String msg = String.format("No matching resource found with version ID: %s", requestedVersion);
                throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(msg), SystemCode.REFERENCE_NOT_FOUND, IssueTypeEnum.NOT_FOUND);
            }
        }
        return resource;
    }    
}
