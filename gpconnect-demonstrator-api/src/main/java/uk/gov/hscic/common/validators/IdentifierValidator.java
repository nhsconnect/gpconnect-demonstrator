package uk.gov.hscic.common.validators;


import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

public class IdentifierValidator {

    public static <T extends BaseResource> T versionComparison(IdType id, T resource) {
        if(id.hasVersionIdPart()){
            String requestedVersion = id.getVersionIdPart();
            if(!requestedVersion.equals(resource.getMeta().getVersionId())){
                String msg = String.format("No matching resource found with version ID: %s", requestedVersion);
                throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(msg), SystemCode.REFERENCE_NOT_FOUND, IssueType.NOTFOUND);
            }
        }
        return resource;
    }    
}
