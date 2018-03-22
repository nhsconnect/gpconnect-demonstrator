package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.ListResource.ListMode;
import org.hl7.fhir.dstu3.model.ListResource.ListStatus;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Reference;
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

		ListResource activeAllergies = new ListResource();
		activeAllergies.setTitle(SystemConstants.ACTIVE_ALLERGIES);
		ListResource resolvedAllergies = new ListResource();
		resolvedAllergies.setTitle(SystemConstants.RESOLVED_ALLERGIES);
		AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

		for (int i = 0; i < allergyData.size(); i++) {

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

			allergyIntolerance.getMeta().addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE);

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
			manifestationCoding.setDisplay("Peanut-induced anaphylaxis (disorder)");
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

			if (allergyData.get(i).getClinicalStatus().equals(SystemConstants.RESOLVED)) {

				listResourceBuilder(resolvedAllergies, allergyIntolerance);

			} else {

				listResourceBuilder(activeAllergies, allergyIntolerance);
			}

		}

		if (resolvedAllergies.hasContained() && includedResolved.equals(true)) {
			bundle.addEntry().setResource(resolvedAllergies);
		}
		if (activeAllergies.hasContained()) {
			bundle.addEntry().setResource(activeAllergies);

		}

		return bundle;

	}

	private ListResource listResourceBuilder(ListResource buildingListResource, AllergyIntolerance allergyIntolerance) {
		buildingListResource.setId(allergyIntolerance.getId());

		CodeableConcept noContent = new CodeableConcept();
		noContent.setText(SystemConstants.NO_CONTENT);
		buildingListResource.setEmptyReason(noContent);

		buildingListResource.addContained(allergyIntolerance);

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
