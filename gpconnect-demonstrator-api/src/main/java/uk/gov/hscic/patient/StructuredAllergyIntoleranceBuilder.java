package uk.gov.hscic.patient;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.*;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListMode;
import org.hl7.fhir.dstu3.model.ListResource.ListStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemConstants;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.helpers.CodeableConceptBuilder;
import uk.gov.hscic.common.helpers.WarningCodeExtHelper;
import uk.gov.hscic.patient.details.PatientRepository;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergySearch;
import uk.gov.hscic.practitioner.PractitionerSearch;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import static uk.gov.hscic.SystemConstants.ACTIVE_ALLERGIES_DISPLAY;
import static uk.gov.hscic.SystemConstants.ACTIVE_ALLERGIES_TITLE;
import static uk.gov.hscic.SystemConstants.NO_INFORMATION_AVAILABLE;
import static uk.gov.hscic.SystemConstants.RESOLVED_ALLERGIES_DISPLAY;

@Component
public class StructuredAllergyIntoleranceBuilder {


    @Autowired
    private StructuredAllergySearch structuredAllergySearch;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PractitionerSearch practitionerSearch;
    
    @Autowired
    private CodeableConceptBuilder codeableConceptBuilder;

    @Value("${datasource.patient.nhsNumber:#{null}}")
    private String patient2NhsNo;

    public Bundle buildStructuredAllergyIntolerence(String NHS, Set<String> practitionerIds, Bundle bundle, Boolean includedResolved) {
        List<StructuredAllergyIntoleranceEntity> allergyData = structuredAllergySearch.getAllergyIntollerence(NHS);

        ListResource active = initiateListResource(NHS, ACTIVE_ALLERGIES_DISPLAY, allergyData);
        ListResource resolved = initiateListResource(NHS, RESOLVED_ALLERGIES_DISPLAY, allergyData);

        AllergyIntolerance allergyIntolerance;

        //If there is a 'no known' allergies code then add the necessary coding and return the bundle
        if (allergyData.size() == 1 &&
                allergyData.get(0).getClinicalStatus().equals(SystemConstants.NO_KNOWN)) {

            CodeableConcept noKnownAllergies = createCoding(SystemURL.VS_LIST_EMPTY_REASON_CODE, "nil-known", "Nil Known");
            noKnownAllergies.setText("No Known Allergies");
            active.setEmptyReason(noKnownAllergies);

            Reference patient = new Reference(SystemConstants.PATIENT_REFERENCE_URL + allergyData.get(0).getPatientRef());

            active.setSubject(patient);
            bundle.addEntry().setResource(active);

            if (includedResolved) {
                resolved.setSubject(patient);
                resolved.setEmptyReason(noKnownAllergies);
                bundle.addEntry().setResource(resolved);
            }

            return bundle;
        }

        for (StructuredAllergyIntoleranceEntity allergyIntoleranceEntity : allergyData) {
            allergyIntolerance = new AllergyIntolerance();
            allergyIntolerance.setOnset(new DateTimeType(allergyIntoleranceEntity.getOnSetDateTime()));
            allergyIntolerance.setMeta(createMeta(SystemURL.SD_CC_ALLERGY_INTOLERANCE));
            
            allergyIntolerance.setId(allergyIntoleranceEntity.getId().toString());
            List<Identifier> identifiers = new ArrayList<>();
            Identifier identifier1 = new Identifier()
                    .setSystem("https://fhir.nhs.uk/Id/cross-care-setting-identifier")
                    .setValue(allergyIntoleranceEntity.getGuid());
            identifiers.add(identifier1);
            allergyIntolerance.setIdentifier(identifiers);

            if (allergyIntoleranceEntity.getClinicalStatus().equals(SystemConstants.ACTIVE)) {
                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.ACTIVE);
            } else {
                allergyIntolerance.setClinicalStatus(AllergyIntoleranceClinicalStatus.RESOLVED);
            }

            if (allergyIntoleranceEntity.getCategory().equals(SystemConstants.MEDICATION)) {
                allergyIntolerance.addCategory(AllergyIntoleranceCategory.MEDICATION);
            } else {
                allergyIntolerance.addCategory(AllergyIntoleranceCategory.ENVIRONMENT);
            }

            allergyIntolerance.setVerificationStatus(AllergyIntoleranceVerificationStatus.UNCONFIRMED);

            //CODE
            codeableConceptBuilder.addConceptCode(SystemConstants.SNOMED_URL, allergyIntoleranceEntity.getConceptCode(), allergyIntoleranceEntity.getConceptDisplay())
            	   .addDescription(allergyIntoleranceEntity.getDescCode(), allergyIntoleranceEntity.getDescDisplay())
            	   .addTranslation(allergyIntoleranceEntity.getCodeTranslationRef());
            allergyIntolerance.setCode(codeableConceptBuilder.build());
            codeableConceptBuilder.clear();

            allergyIntolerance.setAssertedDate(allergyIntoleranceEntity.getAssertedDate());

            Reference patient = new Reference(
                    SystemConstants.PATIENT_REFERENCE_URL + allergyIntoleranceEntity.getPatientRef());
            allergyIntolerance.setPatient(patient);

            Annotation noteAnnotation = new Annotation(new StringType(allergyIntoleranceEntity.getNote()));
            allergyIntolerance.setNote(Collections.singletonList(noteAnnotation));

            AllergyIntoleranceReactionComponent reaction = new AllergyIntoleranceReactionComponent();

            // MANIFESTATION
            List<CodeableConcept> theManifestation = new ArrayList<>();
            codeableConceptBuilder.addConceptCode(SystemConstants.SNOMED_URL, allergyIntoleranceEntity.getManifestationCoding(), allergyIntoleranceEntity.getManifestationDisplay())
            	.addDescription(allergyIntoleranceEntity.getManifestationDescCoding(), allergyIntoleranceEntity.getManifestationDescDisplay())
            	.addTranslation(allergyIntoleranceEntity.getManTranslationRef());
            theManifestation.add(codeableConceptBuilder.build());
            codeableConceptBuilder.clear();
            reaction.setManifestation(theManifestation);

            reaction.setDescription(allergyIntoleranceEntity.getNote());

            //SEVERITY
            try {
				reaction.setSeverity(AllergyIntoleranceSeverity.fromCode(allergyIntoleranceEntity.getSeverity()));
			} catch (FHIRException e) {
				throw OperationOutcomeFactory.buildOperationOutcomeException(
						new UnprocessableEntityException("Unknown severity: " + allergyIntoleranceEntity.getSeverity()),
						SystemCode.INVALID_RESOURCE, IssueType.INVALID);
			}          

            //EXPOSURE
            CodeableConcept exposureRoute = new CodeableConcept();
            reaction.setExposureRoute(exposureRoute);
            allergyIntolerance.addReaction(reaction);

            //RECORDER
            final Reference refValue = new Reference();
            final Identifier identifier = new Identifier();
            final String recorder = allergyIntoleranceEntity.getRecorder();

            //This is just an example to demonstrate using Reference element instead of Identifier element
            if (recorder.equals(patient2NhsNo.trim())) {
                Reference rec = new Reference(
                        SystemConstants.PATIENT_REFERENCE_URL + allergyIntoleranceEntity.getPatientRef());
                allergyIntolerance.setRecorder(rec);
            } else if (patientRepository.findByNhsNumber(recorder) != null) {
                identifier.setSystem(SystemURL.ID_NHS_NUMBER);
                identifier.setValue(recorder);

                refValue.setIdentifier(identifier);
                allergyIntolerance.setRecorder(refValue);
            } else if (practitionerSearch.findPractitionerByUserId(recorder) != null) {
                refValue.setReference("Practitioner/" + recorder);
                allergyIntolerance.setRecorder(refValue);

                practitionerIds.add(recorder);
            }

            //CLINICAL STATUS
            List<Extension> extensions = new ArrayList<>();
            if (allergyIntolerance.getClinicalStatus().getDisplay().contains("Active")) {
                listResourceBuilder(active, allergyIntolerance, false);

                bundle.addEntry().setResource(allergyIntolerance);
            } else if (allergyIntolerance.getClinicalStatus().getDisplay().equals("Resolved")
                    && includedResolved.equals(true)) {
                listResourceBuilder(resolved, allergyIntolerance, true);
                allergyIntolerance.setLastOccurrence(allergyIntoleranceEntity.getEndDate());


                final Extension allergyEndExtension = createAllergyEndExtension(allergyIntoleranceEntity);
                extensions.add(allergyEndExtension);

            }

            if (!extensions.isEmpty()) {
                allergyIntolerance.setExtension(extensions);
            }
            
            //ASSERTER
            Reference asserter = allergyIntolerance.getAsserter();
            if (asserter != null && asserter.getReference() != null && asserter.getReference().startsWith("Practitioner")) {
            	String[] split = asserter.getReference().split("/");
            	practitionerIds.add(split[1]);
            }

        }

        if (!active.hasEntry()) {
            addEmptyListNote(active);
            addEmptyReasonCode(active);
        }

        bundle.addEntry().setResource(active);

        if (includedResolved && !resolved.hasEntry()) {
            addEmptyListNote(resolved);
            addEmptyReasonCode(resolved);
        }

        if (includedResolved) {
            bundle.addEntry().setResource(resolved);
        }

        return bundle;

    }

    private ListResource initiateListResource(String NHS, String display, List<StructuredAllergyIntoleranceEntity> allergyIntoleranceEntity) {
        ListResource listResource = new ListResource();

        if (display.equals(ACTIVE_ALLERGIES_DISPLAY)) {
            listResource.setCode(createCoding(SystemConstants.SNOMED_URL, "886921000000105", display));
            listResource.setTitle(ACTIVE_ALLERGIES_TITLE);
        } else if (display.equals(RESOLVED_ALLERGIES_DISPLAY)) {
            listResource.setCode(createCoding(SystemConstants.SNOMED_URL, "1103671000000101", display));
            listResource.setTitle(SystemConstants.RESOLVED_ALLERGIES_TIILE);
        }

        listResource.setMeta(createMeta(SystemURL.SD_GPC_LIST));
        // #179 dont populate List.id
        //listResource.setId("33");
        listResource.setStatus(ListStatus.CURRENT);
        listResource.setMode(ListMode.SNAPSHOT);
        addSubjectWithIdentifier(NHS, listResource);

        addWarningCodeExtensions(allergyIntoleranceEntity, listResource);
        Extension clinicalSettingExtension = setClinicalSetting(allergyIntoleranceEntity);

        if(clinicalSettingExtension != null) {
            listResource.addExtension(clinicalSettingExtension);
        }

        return listResource;
    }

    private void addWarningCodeExtensions(List<StructuredAllergyIntoleranceEntity> allergyIntolerances, ListResource list) {
    	
    	Set<String> warningCodes = new HashSet<>();
    	allergyIntolerances.forEach(allergy -> {
			if (allergy.getWarningCode() != null) {
				warningCodes.add(allergy.getWarningCode());
			}
		});
    	
    	WarningCodeExtHelper.addWarningCodeExtensions(warningCodes, list);
    }
    
    private Extension setClinicalSetting(List<StructuredAllergyIntoleranceEntity> allergyIntoleranceEntity) {
        String warningCode = null;
        for(StructuredAllergyIntoleranceEntity structuredAllergyIntoleranceEntity: allergyIntoleranceEntity) {
            warningCode = structuredAllergyIntoleranceEntity.getWarningCode();
        }
        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding(SystemConstants.SNOMED_URL,"1060971000000108", "General practice service");
        codeableConcept.setCoding(Collections.singletonList(coding));

        return warningCode != null ? new Extension(SystemURL.CLINICAL_SETTING, codeableConcept) : null;

    }

    private void addEmptyListNote(ListResource list) {
        list.addNote(new Annotation(new StringType(SystemConstants.INFORMATION_NOT_AVAILABLE)));
    }

    private void addEmptyReasonCode(ListResource list) {
        CodeableConcept noContent = new CodeableConcept();
        noContent.setCoding(Arrays.asList(new Coding(SystemURL.VS_LIST_EMPTY_REASON_CODE, SystemConstants.NO_CONTENT_RECORDED, SystemConstants.NO_CONTENT_RECORDED_DISPLAY)));
        noContent.setText(SystemConstants.INFORMATION_NOT_AVAILABLE);
        list.setEmptyReason(noContent);
    }

    private void addSubjectWithIdentifier(String NHS, ListResource active) {
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

    private Meta createMeta(String profile) {
        final Meta meta = new Meta();
        meta.addProfile(profile);
        //meta.setLastUpdated(new Date());
        //meta.setVersionId("3");

        return meta;
    }

    private Extension createAllergyEndExtension(StructuredAllergyIntoleranceEntity allergyIntoleranceEntity) {
        final Extension allergyEnd = new Extension(SystemURL.SD_CC_EXT_ALLERGY_INTOLERANCE_END);

        final Extension endDate = new Extension("endDate", new DateTimeType(allergyIntoleranceEntity.getEndDate()));

        String strEndReason = allergyIntoleranceEntity.getEndReason();
        final Extension endReason = new Extension("reasonEnded", new StringType(strEndReason==null || strEndReason.trim().isEmpty() ? NO_INFORMATION_AVAILABLE : strEndReason ));
        allergyEnd.addExtension(endDate);
        allergyEnd.addExtension(endReason);

        return allergyEnd;
    }

    private void listResourceBuilder(ListResource buildingListResource, AllergyIntolerance allergyIntolerance, boolean isResolved) {
        ListEntryComponent comp = new ListEntryComponent();
        // #179 dont populate List.id
        //buildingListResource.setId(allergyIntolerance.getId());

        if (isResolved) {

            buildingListResource.addContained(allergyIntolerance);

            Reference allergyReference = new Reference("#" + allergyIntolerance.getId());
            comp.setItem(allergyReference);
        } else {

            Reference allergyReference = new Reference("AllergyIntolerance/" + allergyIntolerance.getId());
            comp.setItem(allergyReference);
        }

        buildingListResource.addEntry(comp);
    }

}
