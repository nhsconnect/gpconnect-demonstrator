package uk.gov.hscic.practitioner;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.model.practitioner.PractitionerDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PractitionerRoleResourceProvider implements IResourceProvider {

    @Autowired
    private PractitionerSearch practitionerSearch;

    @Override
    public Class<Practitioner> getResourceType() {
        return Practitioner.class;
    }

    @Read(version = true)
    public List<PractitionerRole> getPractitionerRoleByPracticionerId(@IdParam IdType practitionerId) {
        PractitionerDetails practitionerDetails = practitionerSearch
                .findPractitionerDetails(practitionerId.getIdPart());

        if (practitionerDetails == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException(
                            "No practitioner details found for practitioner ID: " + practitionerId.getIdPart()),
                    SystemCode.PRACTITIONER_NOT_FOUND, IssueType.NOTFOUND);
        }

        return practitionerDetailsToPractitionerRoleList(practitionerDetails);
    }


    private List<PractitionerRole> practitionerDetailsToPractitionerRoleList(PractitionerDetails practitionerDetails) {
        final List<String> roleIds = practitionerDetails.getRoleIds();

        final CodeableConcept codeableConcept = new CodeableConcept();

        codeableConcept.setCoding(Arrays.asList(new Coding(
                        SystemURL.CS_CC_JOB_ROLE,
                        practitionerDetails.getRoleCode(),
                        practitionerDetails.getRoleDisplay()
                )
        ));
        
        List<PractitionerRole> practitionerRoleList = new ArrayList<>();
        for(String roleId : roleIds) {
        	PractitionerRole role = new PractitionerRole();
        	Meta meta = new Meta();
        	meta.setProfile(Collections.singletonList(new UriType(SystemURL.SD_GPC_PRACTITIONER_ROLE)));
        	role.setMeta(meta);
        	role.setId(roleId);
        	role.setCode(Arrays.asList(codeableConcept));
        	
        	role.setPractitioner(new Reference("Practitioner/" + practitionerDetails.getId()));
        	role.setOrganization(new Reference ("Organization/" + practitionerDetails.getOrganizationId()));
        	practitionerRoleList.add(role);
        }
        return practitionerRoleList;
    }


}
