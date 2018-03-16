package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.Subject;

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
import org.hl7.fhir.dstu3.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.SystemURL;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergySearch;

@Component
public class StructuredAllergyIntoleranceBuilder {

	@Autowired
	private StructuredAllergySearch structuredAllergySearch;

	public Bundle buildStructuredAllergyIntolerence(String NHS, Bundle bundle, Type includeResolved) {

		List<StructuredAllergyIntoleranceEntity> allergyData = structuredAllergySearch.getAllergyIntollerence(NHS);
		System.out.println(allergyData.size());

		ListResource activeAllergies = new ListResource();
		activeAllergies.setTitle("Active Allergies");
		ListResource resolvedAllergies = new ListResource();
		resolvedAllergies.setTitle("Resolved Allergies");
		AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

		for (int i = 0; i < allergyData.size(); i++) {

			Meta met = new Meta();
			met.setVersionId("3");
			met.addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE);

			allergyIntolerance.setMeta(met);

			allergyIntolerance.setId("1");

			allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.ACTIVE);

			allergyIntolerance.addCategory(AllergyIntoleranceCategory.MEDICATION);

			allergyIntolerance.setVerificationStatus(AllergyIntoleranceVerificationStatus.UNCONFIRMED);

			CodeableConcept value = new CodeableConcept();
			Coding coding = new Coding();
			coding.setCode("CODE");
			coding.setDisplay("DISPLAY");
			coding.setSystem("SYSTEM");
			value.addCoding(coding);

			allergyIntolerance.setCode(value);

			// Date date = new Date(System.currentTimeMillis());

			// all.setAssertedDate(date);
			// structuredBundle.addEntry().setResource(allergyIntolerance);

			// allergyIntolerance.setClinicalStatus(value );

			// here
			// =allergyIntolerance.Meta().addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE));

			Date dateData = allergyData.get(0).getEndDate();

			allergyIntolerance.setAssertedDate(dateData);

			AllergyIntoleranceReactionComponent reaction = new AllergyIntoleranceReactionComponent();

			// MANIFESTATION
			List<CodeableConcept> theManifestation = new ArrayList<>();
			CodeableConcept manifestation = new CodeableConcept();
			Coding manifestationCoding = new Coding();
			manifestationCoding.setDisplay("Peanut-induced anaphylaxis (disorder)");
			manifestationCoding.setCode("241933001");
			manifestationCoding.setSystem("http://snomed.info/sct");
			manifestation.addCoding(manifestationCoding);
			theManifestation.add(manifestation);
			reaction.setManifestation(theManifestation);

			reaction.setDescription("MANIFESTATION DESCRIPTION");

			AllergyIntoleranceSeverity severity = AllergyIntoleranceSeverity.SEVERE;
			reaction.setSeverity(severity);

			CodeableConcept exposureRoute = new CodeableConcept();
			reaction.setExposureRoute(exposureRoute);
			allergyIntolerance.addReaction(reaction);

			// allergyIntolerenceList.add(all);

			System.out.println(allergyData.get(i).getClinicalStatus());

			if (allergyData.get(i).getClinicalStatus().equals("resolved")) {

				listResourceBuilder(resolvedAllergies, allergyIntolerance);

			} else {

				listResourceBuilder(activeAllergies, allergyIntolerance);
			}

		}

		if (resolvedAllergies.hasContained() && includeResolved.primitiveValue().equals("true")) {
			bundle.addEntry().setResource(resolvedAllergies);
		}
		if (activeAllergies.hasContained()) {
			bundle.addEntry().setResource(activeAllergies);

		}

		return bundle;

	}

	private ListResource listResourceBuilder(ListResource buildingListResource, AllergyIntolerance allergyIntolerance) {
		buildingListResource.setId("1");

		CodeableConcept noContent = new CodeableConcept();
		noContent.setText("noContent");
		buildingListResource.setEmptyReason(noContent);

		buildingListResource.addContained(allergyIntolerance);

		ListStatus activeAllergiesStatus = ListStatus.CURRENT;
		buildingListResource.setStatus(activeAllergiesStatus);

		ListMode activeAllergiesMode = ListMode.SNAPSHOT;
		buildingListResource.setMode(activeAllergiesMode);

		CodeableConcept activeAllergiesCode = new CodeableConcept();
		List<Coding> activeAllergiesCoding = new ArrayList<>();
		Coding code1 = new Coding();
		code1.setCode("444");
		activeAllergiesCoding.add(code1);

		activeAllergiesCode.setCoding(activeAllergiesCoding);
		buildingListResource.setCode(activeAllergiesCode);

		buildingListResource.setStatus(activeAllergiesStatus);

		Reference ref2 = new Reference("Patient/1");
		ref2.setDisplay("Patient/1");
		buildingListResource.setSubject(ref2);

		return buildingListResource;

	}

}
