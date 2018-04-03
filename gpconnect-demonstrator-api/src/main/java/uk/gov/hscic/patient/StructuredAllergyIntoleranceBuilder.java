package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.Date;
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

import uk.gov.hscic.SystemCode;
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
        ListResource listResource = new ListResource();
        AllergyIntolerance allergyIntolerance;

        if (includedResolved.equals(true) &&
                allergyData.size() == 1 &&
                allergyData.get(0).getClinicalStatus().equals(SystemConstants.NO_KNOWN)) {
            //only when this condition is true then "No Known Allergies" also will be true
            CodeableConcept noKnownAllergies = new CodeableConcept();
            noKnownAllergies.setText("No Known Allergies");

            listResource.setEmptyReason(noKnownAllergies);
            bundle.addEntry().setResource(listResource);

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
                listResource = listResourceBuilder(listResource, allergyIntolerance);
                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.ACTIVE);
                bundle.addEntry().setResource(allergyIntolerance);

            } else if (allergyIntolerance.getClinicalStatus().getDisplay().equals("Resolved")
                    && includedResolved.equals(true)) {
                listResource = listResourceBuilder(listResource, allergyIntolerance);
                allergyIntolerance.setLastOccurrence(allergyData.get(i).getEndDate());

                final List<Extension> extension = new ArrayList<>();
                final Extension allergyEnd = new Extension();

                allergyEnd.setUrl("allergyEnd");

                final Extension endDate = new Extension();
                endDate.setUrl("endDate");
                final Date endDateSource = allergyData.get(i).getEndDate();
                endDate.setValue(new DateTimeType(endDateSource));

                final Extension endReason = new Extension();
                endReason.setUrl("endReason");
                final String endReasonSource = allergyData.get(i).getEndReason();
                endReason.setValue(new StringType(endReasonSource));

                allergyEnd.addExtension(endDate);
                allergyEnd.addExtension(endReason);


                extension.add(allergyEnd);

                allergyIntolerance.setExtension(extension);


                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.RESOLVED);
                bundle.addEntry().setResource(allergyIntolerance);
            }

        }

        if (listResource.isEmpty()) {
            listResource.addNote(new Annotation(new StringType("" +
                    "There are no allergies in the patient record but it has not been confirmed with the patient that " +
                    "they have no allergies (that is, a ‘no known allergies’ code has not been recorded)."
            )));
        }

        bundle.addEntry().setResource(listResource);
        return bundle;

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
