package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceClinicalStatus;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceVerificationStatus;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Meta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.SystemURL;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergySearch;

@Component
public class StructuredAllergyIntoleranceBuilder {

	@Autowired
	private StructuredAllergySearch structuredAllergySearch;

	public AllergyIntolerance buildStructuredAllergyIntolerence(String NHS) {

		List<StructuredAllergyIntoleranceEntity> allergyData = structuredAllergySearch.getAllergyIntollerence(NHS);

		System.out.println(allergyData.get(0).getClinicalStatus());

		List<AllergyIntolerance> allergyIntolerenceList = new ArrayList<>();

		AllergyIntolerance all = new AllergyIntolerance();

		Meta met = new Meta();
		met.setVersionId("3");
		met.addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE);

		all.setMeta(met);

		all.setId("1");

		all.setClinicalStatus(AllergyIntoleranceClinicalStatus.ACTIVE);

		// all.addCategory(AllergyIntoleranceCategory.MEDICATION);

		all.setVerificationStatus(AllergyIntoleranceVerificationStatus.UNCONFIRMED);

		CodeableConcept value = new CodeableConcept();
		Coding coding = new Coding();
		coding.setCode("CODE");
		coding.setDisplay("DISPLAY");
		coding.setSystem("SYSTEM");
		value.addCoding(coding);

		all.setCode(value);

		// Date date = new Date(System.currentTimeMillis());

		// all.setAssertedDate(date);
		// structuredBundle.addEntry().setResource(all);

		// all.setClinicalStatus(value );

		// here = all.Meta().addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE));
		Date dateData = allergyData.get(0).getEndDate();

		all.setAssertedDate(dateData);

		AllergyIntoleranceReactionComponent reaction = new AllergyIntoleranceReactionComponent();

		List<CodeableConcept> theManifestation = new ArrayList<>();
		reaction.setManifestation(theManifestation);

		reaction.setDescription("MANIFESTATION DESCRIPTION");

		AllergyIntoleranceSeverity severity = AllergyIntoleranceSeverity.MILD;
		reaction.setSeverity(severity);

		CodeableConcept exposureRoute = new CodeableConcept();
		reaction.setExposureRoute(exposureRoute);
		all.addReaction(reaction);

		// allergyIntolerenceList.add(all);

		return all;

	}

}
