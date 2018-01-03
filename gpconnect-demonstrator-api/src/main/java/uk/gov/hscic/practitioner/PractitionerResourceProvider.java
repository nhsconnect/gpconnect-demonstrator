package uk.gov.hscic.practitioner;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.model.practitioner.PractitionerDetails;

@Component
public class PractitionerResourceProvider implements IResourceProvider {

    @Autowired
    private PractitionerSearch practitionerSearch;

    @Override
    public Class<Practitioner> getResourceType() {
        return Practitioner.class;
    }

    @Read(version = true)
    public Practitioner getPractitionerById(@IdParam IdType practitionerId) {
        PractitionerDetails practitionerDetails = practitionerSearch
                .findPractitionerDetails(practitionerId.getIdPart());

        if (practitionerDetails == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException(
                            "No practitioner details found for practitioner ID: " + practitionerId.getIdPart()),
                    SystemCode.PRACTITIONER_NOT_FOUND, IssueType.INVALID);
        }

        return IdentifierValidator.versionComparison(practitionerId,
                practitionerDetailsToPractitionerResourceConverter(practitionerDetails));
    }

    @Search
    public List<Practitioner> getPractitionerByPractitionerUserId(
            @RequiredParam(name = Practitioner.SP_IDENTIFIER) TokenParam practitionerId, 
            @Sort SortSpec sort,
            @Count Integer count) {
        
    
        
        List<Practitioner> practitioners =  practitionerSearch.findPractitionerByUserId(practitionerId.getValue()).stream()
                .map(this::practitionerDetailsToPractitionerResourceConverter).collect(Collectors.toList());
        
        if(sort != null && sort.getParamName().equalsIgnoreCase(Location.SP_STATUS)){
            Collections.sort(practitioners, (Practitioner a, Practitioner b) -> {
                
                String aStatus = a.getGender().toString();
                String bStatus = b.getGender().toString();
                
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
        return count != null ? practitioners.subList(0, count) : practitioners;
    }

    private Practitioner practitionerDetailsToPractitionerResourceConverter(PractitionerDetails practitionerDetails) {
        Identifier identifier = new Identifier()
                .setSystem(SystemURL.ID_SDS_USER_ID)
                .setValue(practitionerDetails.getUserId());
        
        Practitioner practitioner = new Practitioner()
                .addIdentifier(identifier);
        
        practitionerDetails
                .getRoleIds()
                .stream()
                .distinct()
                .map(roleId -> new Identifier().setSystem(SystemURL.ID_SDS_ROLE_PROFILE_ID).setValue(roleId))
                .forEach(practitioner::addIdentifier);

        String resourceId = String.valueOf(practitionerDetails.getId());
        String versionId = String.valueOf(practitionerDetails.getLastUpdated().getTime());
        String resourceType = practitioner.getResourceType().toString();
        
        IdType id = new IdType(resourceType, resourceId, versionId);

        practitioner.setId(id);
        practitioner.getMeta().setVersionId(versionId);
        practitioner.getMeta().setLastUpdated(practitionerDetails.getLastUpdated());       
        practitioner.getMeta().addProfile(SystemURL.SD_GPC_PRACTITIONER);

        HumanName name = new HumanName()
                .setFamily(practitionerDetails.getNameFamily())
                .addGiven(practitionerDetails.getNameGiven())
                .addPrefix(practitionerDetails.getNamePrefix())
                .setUse(NameUse.USUAL);

        practitioner.addName(name);

        switch (practitionerDetails.getGender().toLowerCase(Locale.UK)) {
            case "male":
                practitioner.setGender(AdministrativeGender.MALE);
                break;
            case "female":
                practitioner.setGender(AdministrativeGender.FEMALE);
                break;
            case "other":
                practitioner.setGender(AdministrativeGender.OTHER);
                break;
            default:
                practitioner.setGender(AdministrativeGender.UNKNOWN);
                break;
        }

        Coding roleCoding = new Coding(SystemURL.VS_SDS_JOB_ROLE_NAME, practitionerDetails.getRoleCode(), null)
                .setDisplay(practitionerDetails.getRoleDisplay());
       
      /*  practitioner.addPractitionerRole().setRole(new CodeableConcept().addCoding(roleCoding))
                .setManagingOrganization(
                        new Reference("Organization/" + practitionerDetails.getOrganizationId())); // Associated
                                                                                                             // Organisation*/

        for (int i = 0; i < practitionerDetails.getComCode().size(); i++) {

            Coding comCoding = new Coding(SystemURL.VS_HUMAN_LANGUAGE, practitionerDetails.getComCode().get(i), null)
                    .setDisplay(practitionerDetails.getComDisplay().get(i));

            practitioner.addCommunication().addCoding(comCoding);
        }

        return practitioner;
    }
}
