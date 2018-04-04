package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListMode;
import org.hl7.fhir.dstu3.model.ListResource.ListStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.SystemConstants;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergySearch;

@Component
public class StructuredAllergyIntoleranceBuilder {

    @Autowired
    private StructuredAllergySearch structuredAllergySearch;

    public Bundle buildStructuredAllergyIntolerence(String NHS, Bundle bundle, Boolean includedResolved) {

        List<StructuredAllergyIntoleranceEntity> allergyData = structuredAllergySearch.getAllergyIntollerence(NHS);

        ListResource active = addMetaToList();
        ListResource resolved = addMetaToList();

        final CodeableConcept codingActive = createCoding("http://snomed.info/sct", "TBD", "Active Allergies");
        final CodeableConcept codingResolved = createCoding("http://snomed.info/sct", "TBD", "Resolved Allergies");

        active.setCode(codingActive);
        active.setId("33");
        active.setStatus(ListStatus.CURRENT);
        active.setMode(ListMode.SNAPSHOT);

        resolved.setCode(codingResolved);
        resolved.setId("33");
        resolved.setStatus(ListStatus.CURRENT);
        resolved.setMode(ListMode.SNAPSHOT);


        active.setTitle("Active Allergies");
        resolved.setTitle("Resolved Allergies");

        AllergyIntolerance allergyIntolerance;

        if (includedResolved.equals(true) &&
                allergyData.size() == 1 &&
                allergyData.get(0).getClinicalStatus().equals(SystemConstants.NO_KNOWN)) {

            CodeableConcept noKnownAllergies = createCoding(SystemURL.HL7_SPECIAL_VALUES, "nil-known", "Nil Known");
            noKnownAllergies.setText("No Known Allergies");

            active.setEmptyReason(noKnownAllergies);


            Reference patient = new Reference(
                    SystemConstants.PATIENT_REFERENCE_URL + allergyData.get(0).getPatientRef());

            active.setSubject(patient);

            bundle.addEntry().setResource(active);

            return bundle;
        }


        for (int i = 0; i < allergyData.size(); i++) {
            allergyIntolerance = new AllergyIntolerance();
            Meta met = new Meta();
            met.setVersionId("3");
            met.addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE);

            allergyIntolerance.setMeta(met);

            allergyIntolerance.setId(allergyData.get(i).getId().toString());

            if (allergyData.get(i).getClinicalStatus().equals(SystemConstants.ACTIVE)) {
                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.ACTIVE);
            } else {
                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.RESOLVED);
            }
            if (allergyData.get(i).getClinicalStatus().equals(SystemConstants.MEDICATION)) {
                allergyIntolerance.addCategory(AllergyIntoleranceCategory.MEDICATION);
            } else {
                allergyIntolerance.addCategory(AllergyIntoleranceCategory.ENVIRONMENT);
            }

            allergyIntolerance.setVerificationStatus(AllergyIntoleranceVerificationStatus.UNCONFIRMED);

            CodeableConcept value = new CodeableConcept();
            Coding coding = new Coding();
            coding.setCode("241933001");
            coding.setDisplay("Peanut-induced anaphylaxis (disorder)");
            coding.setSystem(SystemConstants.SNOMED_URL);
            value.addCoding(coding);

            allergyIntolerance.setCode(value);

            allergyIntolerance.setAssertedDate(allergyData.get(i).getAssertedDate());

            Reference patient = new Reference(
                    SystemConstants.PATIENT_REFERENCE_URL + allergyData.get(i).getPatientRef());
            allergyIntolerance.setPatient(patient);

            // ADD END DATE AND REASON

            AllergyIntoleranceReactionComponent reaction = new AllergyIntoleranceReactionComponent();

            // MANIFESTATION
            List<CodeableConcept> theManifestation = new ArrayList<>();
            CodeableConcept manifestation = new CodeableConcept();
            Coding manifestationCoding = new Coding();
            manifestationCoding.setDisplay(allergyData.get(i).getNote());
            manifestationCoding.setCode("241933001");
            manifestationCoding.setSystem(SystemConstants.SNOMED_URL);
            manifestation.addCoding(manifestationCoding);
            theManifestation.add(manifestation);
            reaction.setManifestation(theManifestation);

            reaction.setDescription(SystemConstants.MANIFESTATION_DESCRIPTION);

            AllergyIntoleranceSeverity severity = AllergyIntoleranceSeverity.SEVERE;
            reaction.setSeverity(severity);

            CodeableConcept exposureRoute = new CodeableConcept();
            reaction.setExposureRoute(exposureRoute);
            allergyIntolerance.addReaction(reaction);

            allergyIntolerance.addNote().setText(allergyData.get(i).getNote());


            if (allergyIntolerance.getClinicalStatus().getDisplay().contains("Active")) {
                active = listResourceBuilder(active, allergyIntolerance);
                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.ACTIVE);
                bundle.addEntry().setResource(allergyIntolerance);

            } else if (allergyIntolerance.getClinicalStatus().getDisplay().equals("Resolved")
                    && includedResolved.equals(true)) {
                resolved = listResourceBuilder(resolved, allergyIntolerance);
                allergyIntolerance.setLastOccurrence(allergyData.get(i).getEndDate());

                allergyIntolerance.setExtension(createAllergyEndExtension(allergyData, i));

                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.RESOLVED);
                bundle.addEntry().setResource(allergyIntolerance);
            }

        }

        if (!active.hasEntry()) {
            addIdentifier(NHS, active);
        }

        bundle.addEntry().setResource(active);

        if (includedResolved && !resolved.hasEntry()) {
            addIdentifier(NHS, resolved);
        }

        if (includedResolved) {
            bundle.addEntry().setResource(resolved);
        }

        return bundle;

    }

    private void addIdentifier(String NHS, ListResource active) {
        active.addNote(new Annotation(new StringType("" +
                "There are no allergies in the patient record but it has not been confirmed with the patient that " +
                "they have no allergies (that is, a ‘no known allergies’ code has not been recorded)."
        )));

        final Reference value = new Reference();
        final Identifier identifier = new Identifier();
        identifier.setSystem(SystemURL.ID_NHS_NUMBER);
        identifier.setValue(NHS);

        value.setIdentifier(identifier);
        active.setSubject(value);
    }

    private CodeableConcept createCoding(String system, String code, String display) {
        final CodeableConcept codeableConcept = new CodeableConcept();
        codeableConcept.setCoding(Arrays.asList(new Coding(
                system,
                code,
                display
        )));

        return codeableConcept;
    }

    private ListResource addMetaToList() {
        ListResource active = new ListResource();

        final Meta meta = new Meta();
        meta.addProfile(SystemURL.SD_GPC_LIST);
        meta.setVersionId("3");

        active.setMeta(meta);
        return active;
    }

    private List<Extension> createAllergyEndExtension(List<StructuredAllergyIntoleranceEntity> allergyData, int i) {
        final Extension allergyEnd = new Extension("https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-AllergyIntoleranceEnd-1");

        final Extension endDate = new Extension("endDate", new DateTimeType(allergyData.get(i).getEndDate()));

        final Extension endReason = new Extension("endReason", new StringType(allergyData.get(i).getEndReason()));

        allergyEnd.addExtension(endDate);
        allergyEnd.addExtension(endReason);

        return Arrays.asList(allergyEnd);
    }

    private ListResource listResourceBuilder(ListResource buildingListResource, AllergyIntolerance allergyIntolerance) {
        buildingListResource.setId(allergyIntolerance.getId());

        CodeableConcept noContent = new CodeableConcept();
        noContent.setText(SystemConstants.NO_CONTENT);
        buildingListResource.setEmptyReason(noContent);

        List<Resource> value = new ArrayList<>();
        value.add(allergyIntolerance);
        buildingListResource.setContained(value);
        ListEntryComponent comp = new ListEntryComponent();
        Reference allergyReference = new Reference("AllergyIntolerance/" + allergyIntolerance.getId());
        comp.setItem(allergyReference);
        buildingListResource.addEntry(comp);

        ListStatus activeAllergiesStatus = ListStatus.CURRENT;
        buildingListResource.setStatus(activeAllergiesStatus);

        ListMode activeAllergiesMode = ListMode.SNAPSHOT;
        buildingListResource.setMode(activeAllergiesMode);

        CodeableConcept activeAllergiesCode = new CodeableConcept();
        List<Coding> activeAllergiesCoding = new ArrayList<>();
        Coding coding = new Coding();
        coding.setCode("List of code to be defined");
        activeAllergiesCoding.add(coding);

        activeAllergiesCode.setCoding(activeAllergiesCoding);
        buildingListResource.setCode(activeAllergiesCode);

        buildingListResource.setStatus(activeAllergiesStatus);

        Reference reference = new Reference(SystemConstants.PATIENT_REFERENCE_URL + allergyIntolerance.getPatient());
        reference.setDisplay(SystemConstants.PATIENT_REFERENCE_URL + allergyIntolerance.getPatient());
        buildingListResource.setSubject(reference);

        return buildingListResource;

    }

}
