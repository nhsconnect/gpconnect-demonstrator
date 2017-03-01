package uk.gov.hscic.practitioner;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Practitioner.PractitionerRole;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.practitioner.model.PractitionerDetails;
import uk.gov.hscic.practitioner.search.PractitionerSearch;

@Component
public class PractitionerResourceProvider  implements IResourceProvider {

    @Autowired
    private PractitionerSearch practitionerSearch;

    @Override
    public Class<Practitioner> getResourceType() {
        return Practitioner.class;
    }

    @Read()
    public Practitioner getPractitionerById(@IdParam IdDt practitionerId) {
        PractitionerDetails practitionerDetails = practitionerSearch.findPractitionerDetails(practitionerId.getIdPart());

        if (practitionerDetails == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No practitioner details found for practitioner ID: "+practitionerId.getIdPart());
            throw new InternalErrorException("No practitioner details found for practitioner ID: "+practitionerId.getIdPart(), operationalOutcome);
        }

        return practitionerDetailsToPractitionerResourceConverter(practitionerDetails);
    }

    @Search
    public List<Practitioner> getPractitionerByPractitionerUserId(@RequiredParam(name=Practitioner.SP_IDENTIFIER) TokenParam practitionerId) {
        ArrayList<Practitioner> practitioners = new ArrayList<>();
        List<PractitionerDetails> practitionerDetailsList = Collections.singletonList(practitionerSearch.findPractitionerByUserId(practitionerId.getValue()));

        if (practitionerDetailsList != null && !practitionerDetailsList.isEmpty()) {
            for (PractitionerDetails practitionerDetails : practitionerDetailsList) {
                Practitioner practitioner = practitionerDetailsToPractitionerResourceConverter(practitionerDetails);
                practitioner.setId(String.valueOf(practitionerDetails.getId()));
                practitioners.add(practitioner);
            }
        }

        return practitioners;
    }

    public Practitioner practitionerDetailsToPractitionerResourceConverter(PractitionerDetails practitionerDetails){
        Practitioner practitioner = new Practitioner();
        practitioner.setId(new IdDt(practitionerDetails.getId()));
        practitioner.getMeta().setLastUpdated(practitionerDetails.getLastUpdated());
        practitioner.getMeta().setVersionId(String.valueOf(practitionerDetails.getLastUpdated().getTime()));
        practitioner.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/sds-user-id", practitionerDetails.getUserId()));
        practitioner.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/sds-role-profile-id", practitionerDetails.getRoleId()));

        HumanNameDt name = new HumanNameDt();
        name.addFamily(practitionerDetails.getNameFamily());
        name.addGiven(practitionerDetails.getNameGiven());
        name.addPrefix(practitionerDetails.getNamePrefix());
        name.setUse(NameUseEnum.USUAL);
        practitioner.setName(name);

        switch (practitionerDetails.getGender().toLowerCase(Locale.UK)) {
            case "male":
                practitioner.setGender(AdministrativeGenderEnum.MALE);
                break;

            case "female":
                practitioner.setGender(AdministrativeGenderEnum.FEMALE);
                break;

            case "other":
                practitioner.setGender(AdministrativeGenderEnum.OTHER);
                break;

            default:
                practitioner.setGender(AdministrativeGenderEnum.UNKNOWN);
                break;
        }

        CodingDt roleCoding = new CodingDt();
        roleCoding.setSystem("http://fhir.nhs.net/ValueSet/sds-job-role-name-1");
        roleCoding.setCode(practitionerDetails.getRoleCode());
        roleCoding.setDisplay(practitionerDetails.getRoleDisplay());
        CodeableConceptDt roleCodableConcept = new CodeableConceptDt();
        roleCodableConcept.addCoding(roleCoding);
        PractitionerRole practitionerRole = practitioner.addPractitionerRole();
        practitionerRole.setRole(roleCodableConcept);

        practitionerRole.setManagingOrganization(new ResourceReferenceDt("Organization/"+practitionerDetails.getOrganizationId())); // Associated Organisation

        CodingDt comCoding = new CodingDt();
        comCoding.setSystem("http://fhir.nhs.net/ValueSet/human-language-1-0");
        comCoding.setCode(practitionerDetails.getComCode());
        comCoding.setDisplay(practitionerDetails.getComDisplay());
        practitioner.addCommunication().addCoding(comCoding);

        return practitioner;
    }
}
