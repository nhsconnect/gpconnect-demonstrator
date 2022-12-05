/*
 Copyright 2019  Simon Farrow <simon.farrow1@hscic.gov.uk>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.gov.hscic.patient;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.exceptions.FHIRException;
import uk.gov.hscic.SystemConstants;
import static uk.gov.hscic.SystemConstants.*;
import uk.gov.hscic.SystemURL;
import static uk.gov.hscic.SystemURL.ID_CROSS_CARE_SETTIING;
import static uk.gov.hscic.SystemURL.SD_CC_EXT_PROBLEM_SIGNIFICANCE;
import static uk.gov.hscic.SystemURL.SD_GPC_LIST;
import static uk.gov.hscic.medications.PopulateMedicationBundle.setClinicalSetting;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyListNote;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyReasonCode;
import static uk.gov.hscic.patient.PatientResourceProvider.createCodeableConcept;

/**
 * Appends canned responses to a StructuredBundle response. Note that the
 * Clinical Item List Resources do not have an id so where these are referenced
 * in collections we use the list *title* to identify them. NB for Allergies the
 * title may not be the same as the Snomed display.
 *
 * @author simonfarrow
 */
public class StructuredBuilder {

    private final static String BASE_DATE_STR = "2020-12-01";
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    static {
        try {
            BASE_DATE = FORMATTER.parse(BASE_DATE_STR);
        } catch (ParseException pe) {
        }
    }

    private static Date BASE_DATE; // supplies a consistent date to the resources created (replaces multiple calls to new Date())

    /**
     * This map is critical it guides what resources will be added into the
     * canned response. It should only contain references of the form
     * ResourceName/ResourceId plus some list title
     */
    private final HashMap<String, Integer> refCounts = new HashMap<String, Integer>() {
        // Anonymous class overriding put behaviour.
        @Override
        public Integer put(String reference, Integer i) {
            if (DEBUG_LEVEL > 0) {
                if (!Arrays.asList(SNOMED_LIST_TITLES).contains(reference) && reference != null && !reference.matches("^[^/]+/[^/]+$")) {
                    System.err.println("WARNING: StructuredBuilder - Adding non reference format key to refCounts " + reference);
                }
                if (get(reference) != null && get(reference) != i - 1) {
                    System.err.println("WARNING: StructuredBuilder - Non incrememntal change to refCounts " + reference + " current value " + get(reference) + " value to set " + i);
                }
            }
            return reference != null ? super.put(reference, i) : null;
        }
    };

    // This also tracks what has been added but allows access via id to the resource
    private final HashMap<String, Resource> addedToResponse = new HashMap<String, Resource>() {
        // Anonymous class overriding put behaviour.
        @Override
        public Resource put(String reference, Resource r) {
            if (DEBUG_LEVEL > 0) {
                if (!Arrays.asList(SNOMED_LIST_TITLES).contains(reference) && reference != null && !reference.matches("^[^/]+/[^/]+$")) {
                    System.err.println("WARNING: StructuredBuilder - Adding non reference format key to addedToResponse " + reference);
                } else {
                    //System.err.println("StructuredBuilder - Adding reference format key to addedToResponse " + key);
                }
                if (get(reference) != null) {
                    System.err.println("WARNING: StructuredBuilder - Setting an existing addedToResponse entry " + reference);
                }
            }
            return reference != null ? super.put(reference, r) : null;
        }
    };

    private static final String SNOMED_CONSULTATION_ENCOUNTER_TYPE = "325851000000107";

    private static final String[] SNOMED_LIST_TITLES = {
        SNOMED_MEDICATION_LIST_DISPLAY,
        SNOMED_RESOLVED_ALLERGIES_DISPLAY,
        SNOMED_ACTIVE_ALLERGIES_DISPLAY,
        SNOMED_PROBLEMS_LIST_DISPLAY, // 1.3
        SNOMED_CONSULTATION_LIST_DISPLAY,
        SNOMED_IMMUNIZATIONS_LIST_DISPLAY,
        SNOMED_UNCATEGORISED_DATA_LIST_DISPLAY,
        SNOMED_REFERRALS_LIST_DISPLAY, // 1.4
        SNOMED_INVESTIGATIONS_LIST_DISPLAY}; // 1.4

    // add any new clinical areas supported as canned responses here
    private final static String[] PROCESSED_CANNED_CLINICAL_AREAS = {INCLUDE_PROBLEMS_PARM,
        INCLUDE_CONSULTATIONS_PARM
// extension point add new clinical areas *that need processing* here
    };

    private static final int DEBUG_LEVEL = 2;

    /**
     * append the entries in the xml bundle file to the response bundle can
     * handle xml or json source
     *
     * This method is only invoked for patients 2 or 3
     *
     * @param filename fully qualified file path to canned response file
     * @param structuredBundle
     * @param patient
     * @param parms
     * @throws DataFormatException
     * @throws ConfigurationException
     * @throws org.hl7.fhir.exceptions.FHIRException
     * @throws java.io.IOException
     */
    public void appendCannedResponse(String filename, Bundle structuredBundle, Patient patient, ArrayList<Parameters.ParametersParameterComponent> parms) throws DataFormatException, ConfigurationException, FHIRException, IOException {
        // read from a pre prepared file
        FhirContext ctx = FhirContext.forDstu3();
        String suffix = filename.replaceFirst("^.*\\.([^\\.]+)$", "$1");
        String s = new String(Files.readAllBytes(new File(filename).toPath()));

        s = editResponse(patient, s);

        IParser parser = getParser(suffix, ctx);

        Bundle parsedBundle = (Bundle) parser.parseResource(s);
        HashMap<String, Integer> duplicates = new HashMap<>();
        // This is an array but the parameter name is the same even for multiple entries
        // which only happens for problems
        String parameterName = parms.get(0).getName();

        // This list identifies those canned clinical areas that need some processing 
        // rather simply transcibing into the reponse as is
        // ie if problems or consultations, for patient 2 or 3
        if (Arrays.asList(PROCESSED_CANNED_CLINICAL_AREAS).contains(parameterName)) {
            // ie if patient 2 or (patient 3 && consultations)
            if (patient.getIdElement().getIdPartAsLong() == PATIENT_2 || parameterName.equals(INCLUDE_CONSULTATIONS_PARM)) {
                // this handles canned responses that need reprocessing ie problems and consultations at 1.3
                handleCannedClinicalArea(parsedBundle, duplicates, structuredBundle, parms, parameterName, patient);
            } else {
                // for patient 3 when problems are requested return an empty list
                addEmptyProblemsList(patient, structuredBundle);
            }
        } else {

            Resource pickedResource = null;
            // for other canned responses just transcribe as is eg uncat and imm for 1.3
            for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
                structuredBundle.addEntry(entry);
                switch (parameterName) {
                    case INCLUDE_IMMUNIZATIONS_PARM:
                        if (pickedResource == null && entry.getResource().getResourceType().toString().equals("Immunization")) {
                            pickedResource = entry.getResource();
                        }
                        break;
                    case INCLUDE_UNCATEGORISED_DATA_PARM:
                        if (pickedResource == null && entry.getResource().getResourceType().toString().equals("Observation")) {
                            pickedResource = entry.getResource();
                        }
                        break;
                    case INCLUDE_INVESTIGATIONS_PARM:
                        if (pickedResource == null && entry.getResource().getResourceType().toString().equals("DiagnosticReport")) {
                            pickedResource = entry.getResource();
                        }
                        break;
                    case INCLUDE_REFERRALS_PARM:
                        if (pickedResource == null && entry.getResource().getResourceType().toString().equals("ReferralRequest")) {
                            pickedResource = entry.getResource();
                        }
                        break;
                }
            }

            // patient 2 specific add a created problem
            if (patient.getIdElement().getIdPartAsLong() == PATIENT_2) {

                //addEntryToBundleOnlyOnce(structuredBundle, resource.getResourceType() + "/" + resource.getId(), new BundleEntryComponent().setResource(resource));
                // add a problem to the bundle
                Condition condition = createCondition(parameterName.replaceFirst("^include(.*)$", "$1Problem"), patient, pickedResource.getId());
                addEntryToBundleOnlyOnce(structuredBundle, condition.getResourceType() + "/" + condition.getId(), new BundleEntryComponent().setResource(condition));

                // #359 new secondary list which may have been created earlier in the uncanned meds and allergies
                // if there is more than one of these lists then copy further entries into the first list
                // and note subsequent lists for deletion after we exit the iteration
                // this ensures that we only work on a single list and there are no copies
                ListResource problemsLinkedNotRelatedToPrimaryQueryList = null;
                ArrayList<BundleEntryComponent> toBeDeleted = new ArrayList<>();
                for (BundleEntryComponent entry : structuredBundle.getEntry()) {
                    if (entry.getResource().getResourceType() == ResourceType.List) {
                        ListResource list = (ListResource) entry.getResource();
                        if (list.getTitle() != null && list.getTitle().equals(PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE)) {
                            if (problemsLinkedNotRelatedToPrimaryQueryList == null) {
                                problemsLinkedNotRelatedToPrimaryQueryList = list;
                            } else {
                                // we have found another instance of this list 
                                // so copy the entries in the second list to the first list and mark subsequent ones for removal
                                for (ListEntryComponent entry1 : list.getEntry()) {
                                    problemsLinkedNotRelatedToPrimaryQueryList.addEntry(entry1);
                                }
                                // rename so we don't end up performing multiple copies because this code
                                // can be invoked several times
                                list.setTitle("deleteme");
                                toBeDeleted.add(entry);
                            } // second or later copy
                        } // its a problems not linked list
                    } // its a list
                }  // for each bundle

                // delete any second or subsequent lists 
                for (BundleEntryComponent entry : toBeDeleted) {
                    structuredBundle.getEntry().remove(entry);
                }

                if (problemsLinkedNotRelatedToPrimaryQueryList == null && addedToResponse.get(PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE) != null) {
                    problemsLinkedNotRelatedToPrimaryQueryList = (ListResource) addedToResponse.get(PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE);
                }
                if (problemsLinkedNotRelatedToPrimaryQueryList == null) {
                    // create a new problem linked list
                    problemsLinkedNotRelatedToPrimaryQueryList = createList(PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_CODE, PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE, SECONDARY_LIST_URL, patient);
                    // add it to the bundle
                    addEntryToBundleOnlyOnce(structuredBundle, PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE, new BundleEntryComponent().setResource(problemsLinkedNotRelatedToPrimaryQueryList));
                }
                // add the problem (Condition) to the problem list
                String ref = "Condition/" + parameterName.replaceFirst("^include(.*)$", "$1Problem");
                problemsLinkedNotRelatedToPrimaryQueryList.addEntry(new ListEntryComponent().setItem(new Reference(ref)));

                // remove any entries that are references other prime queries
                if (problemsLinkedNotRelatedToPrimaryQueryList != null) {

                    // foind the problems list and add the list items to the entry arrayy
                    ArrayList<ListEntryComponent> problemReferences = new ArrayList<>();
                    for (BundleEntryComponent entry : structuredBundle.getEntry()) {
                        if (entry.getResource().getResourceType() == ResourceType.List) {
                            ListResource list = (ListResource) entry.getResource();
                            if (list.getTitle() != null && list.getTitle().equals("Problems")) {
                                for (ListEntryComponent problemEntry : list.getEntry()) {
                                    // list entries in the not relating list
                                    for (ListEntryComponent problemNotRelatedEntry : problemsLinkedNotRelatedToPrimaryQueryList.getEntry()) {
                                        if (problemNotRelatedEntry.getItem().getReference().equals(problemEntry.getItem().getReference())) {
                                            problemReferences.add(problemNotRelatedEntry);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }

                    // finally renove the entries..
                    for (ListEntryComponent entry : problemReferences) {
                        problemsLinkedNotRelatedToPrimaryQueryList.getEntry().remove(entry);
                    }
                }
            }  // patient 2
        } // uncanned
    } // appendCannedResponse

    private void addEmptyProblemsList(Patient patient, Bundle structuredBundle) {
        // this for patient three when problems are requested
        // add an empty problems list
        ListResource problemList = createList(SNOMED_PROBLEMS_LIST_CODE, SNOMED_PROBLEMS_LIST_DISPLAY, patient);
        // add it to the bundle
        if (addEntryToBundleOnlyOnce(structuredBundle, SNOMED_PROBLEMS_LIST_DISPLAY, new BundleEntryComponent().setResource(problemList))) {
            // make it an empty list
            addEmptyListNote(problemList);
            addEmptyReasonCode(problemList);
        }
    }

    /**
     * overload for single reference
     *
     * @param id
     * @param patient
     * @param reference
     * @return The Condition Resource object
     */
    public static Condition createCondition(String id, Patient patient, String reference) {
        return createCondition(id, patient, new String[]{reference});
    }

    /**
     * create a dummy problem/Condition
     *
     * @param id problem id
     * @param patient
     * @param references array of resource id to which this problem refers (may
     * be null)
     * @return The Condition Resource object
     */
    public static Condition createCondition(String id, Patient patient, String[] references) {
        Condition condition = new Condition();
        condition.setId(id);
        condition.setMeta(new Meta().addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-ProblemHeader-Condition-1"));

        // add a problem significance extension
        condition.addExtension(new Extension(SD_CC_EXT_PROBLEM_SIGNIFICANCE).
                setValue(new CodeType("major")));

        // add in some relatedProblemHeader content
        switch (id) {
            case "ImmunizationForAddedProblem":
                addRelatedProblemHeader(condition, "Condition/AllergyIntoleranceForAddedProblem", "child");
                break;
            case "AllergyIntoleranceForAddedProblem":
                addRelatedProblemHeader(condition, "Condition/ImmunizationForAddedProblem", "parent");
                break;
            default:
        }

        if (references != null) {
            for (String reference : references) {
                // not mandatory but required for certains test conditions
                // add the reference to a resource
                condition.addExtension(new Extension("https://fhir.hl7.org.uk/STU3/StructureDefinition/Extension-CareConnect-RelatedClinicalContent-1").
                        setValue(new Reference(reference)));
            }
        }

        condition.addIdentifier(new Identifier().
                setValue("D06C0517-4D1C-11E3-A2DD-010000000161").
                setSystem(ID_CROSS_CARE_SETTIING));

        condition.setClinicalStatus(ConditionClinicalStatus.ACTIVE);

        condition.addCategory(createCodeableConcept("problem-list-item", "Problem List Item", "https://fhir.hl7.org.uk/STU3/CodeSystem/CareConnect-ConditionCategory-1"));

        CodeableConcept cc = createCodeableConcept("231504006", "Mixed anxiety and depressive disorder", SNOMED_URL);
        cc.setText("Anxiety with depression");
        condition.setCode(cc);

        condition.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPartAsLong()));

        // not mandatory
//      condition.setOnset(new DateTimeType(BASE_DATE));
        condition.setAssertedDate(BASE_DATE);

        condition.setAsserter(new Reference("Practitioner/1"));

        return condition;
    }

    private static void addRelatedProblemHeader(Condition condition, String target, String type) {
        Extension relatedProblemHeader = new Extension("https://fhir.hl7.org.uk/STU3/StructureDefinition/Extension-CareConnect-RelatedProblemHeader-1");
        relatedProblemHeader.addExtension(new Extension("target").setValue(new Reference(target)));
        relatedProblemHeader.addExtension(new Extension("type").setValue(new CodeType(type)));
        condition.addExtension(relatedProblemHeader);
    }

    /**
     *
     * @param code
     * @param display
     * @param patient
     * @return populated list
     */
    public static ListResource createList(String code, String display, Patient patient) {
        return createList(code, display, SNOMED_URL, patient);
    }

    /**
     *
     * @param code
     * @param display
     * @param url
     * @param patient
     * @return populated list
     */
    public static ListResource createList(String code, String display, String url, Patient patient) {
        ListResource list = new ListResource();

        list.setMeta(new Meta().addProfile(SD_GPC_LIST));
        list.setStatus(ListResource.ListStatus.CURRENT);
        list.setMode(ListResource.ListMode.SNAPSHOT);
        list.setTitle(display);
        CodeableConcept cc = createCodeableConcept(code, display, url);
        list.setCode(cc);
        list.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPartAsLong()));
        list.setDate(BASE_DATE);

        cc = createCodeableConcept("event-date", null, "http://hl7.org/fhir/list-order");
        list.setOrderedBy(cc);

        return list;
    }

    /**
     * This method is only invoked for patients 2 or 3
     *
     * @param parsedBundle
     * @param duplicates
     * @param structuredBundle
     * @param parms
     * @param parameterName
     * @throws FHIRException
     */
    private void handleCannedClinicalArea(Bundle parsedBundle, HashMap<String, Integer> duplicates, Bundle structuredBundle,
            ArrayList<Parameters.ParametersParameterComponent> parms, String parameterName, Patient patient) throws FHIRException {

// populate resource types
        HashMap<ResourceType, HashMap<String, Resource>> resourceTypes = new HashMap<>();
        for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
            ResourceType resourceType = entry.getResource().getResourceType();
            if (resourceTypes.get(resourceType) == null) {
                resourceTypes.put(resourceType, new HashMap<>());
            }
            HashMap<String, Resource> hmResources = resourceTypes.get(resourceType);
            // Use the list title where there is no id ie List of Problems/Consultations
            // NB There is major misnomer here the id is in fact a fully qualified reference
            String id = entry.getResource().getId() != null ? entry.getResource().getId() : ((ListResource) entry.getResource()).getTitle();
            if (hmResources.get(id) == null) {
                hmResources.put(id, entry.getResource());
            } else {
                // count duplicates
                if (duplicates.get(id) == null) {
                    duplicates.put(id, 2);
                } else {
                    duplicates.put(id, duplicates.get(id) + 1);
                }
            }
        }

        if (DEBUG_LEVEL > 1) {
            dumpResources(resourceTypes);
        }
        walkRoot(structuredBundle, resourceTypes, parms);

        addInTopLevelResourceLists(parameterName, structuredBundle, resourceTypes);

        // add in all referenced resources if not already present
        for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
            // NB get id actually returns fully qualified references
            String reference = entry.getResource().getId();

            // #368 Now there are no references to PractitionerRole we have to force the addition of this
            // but we dont have to do this for referrals for some reason
            if (reference != null && reference.startsWith("PractitionerRole") && (parameterName.equals(INCLUDE_CONSULTATIONS_PARM) || parameterName.equals(INCLUDE_PROBLEMS_PARM))) {
                refCounts.put(reference, 1);
            }

            // if the refs count is not null then include the resource.
            if (refCounts.keySet().contains(reference)) {
                addEntryToBundleOnlyOnce(structuredBundle, reference, entry);
            }
        }

        if (DEBUG_LEVEL > 1) {
            compareRefCountsAndAddedToResponse();
        }

        switch (parameterName) {

            case INCLUDE_PROBLEMS_PARM:
                // add any related consultations/encounters
                ArrayList<String> conditionsIdsInResponse = new ArrayList<>();

                for (String id : addedToResponse.keySet()) {
                    if (id.startsWith("Condition/")) {
                        conditionsIdsInResponse.add(id);
                    }
                }

                // #320 remove encounters
                // #357 reinstate encounters related to problems
                // NB This ignores encounters referenced by Observations
                for (String conditionId : conditionsIdsInResponse) {
                    Condition condition = (Condition) resourceTypes.get(ResourceType.Condition).get(conditionId);
                    if (condition != null && condition.getContext() != null) {
                        Reference encounterReference = condition.getContext();
                        // does this problem/condition reference an encounter in the returning bundle?
                        // NB email from Matt only return Encounters, not Consultations nor a List of Consultations
                        Encounter encounter = (Encounter) resourceTypes.get(ResourceType.Encounter).get(encounterReference.getReference());
                        // NB the id is actually a reference
                        addEntryToBundleOnlyOnce(structuredBundle, encounter.getId(), new BundleEntryComponent().setResource(encounter));
                    } else {
                        System.err.println("WARNING condition " + conditionId + " is null or missing an encounter context");
                    }
                }
                break;

            default:
        } // switch Parameter Name

        // for patient 2 add any problems regardless of whether problems were requested
        // #347 only return problems for patient 2 and not for patient 3
        if (patient.getIdElement().getIdPartAsLong() == PATIENT_2 && resourceTypes.get(ResourceType.Condition) != null) {
            // for already added problems add any related problems/conditions
            resourceTypes.get(ResourceType.Condition).keySet().stream().sorted().forEachOrdered((conditionId) -> {
                Condition condition = (Condition) resourceTypes.get(ResourceType.Condition).get(conditionId);
                for (Extension extension : condition.getExtension()) {
                    if (extension.getValue() instanceof Reference) {
                        Reference reference = (Reference) extension.getValue();
                        // does this problem/condition reference something in the returning bundle?
                        if (refCounts.keySet().contains(reference.getReference())) {
                            if (addEntryToBundleOnlyOnce(structuredBundle, condition.getId(), new BundleEntryComponent().setResource(condition))) {

                                // #314 re add the problems list because there are some!
                                ListResource problemList = (ListResource) resourceTypes.get(ResourceType.List).get(SNOMED_PROBLEMS_LIST_DISPLAY);
                                if (parameterName.equals(INCLUDE_PROBLEMS_PARM) && problemList != null) {
                                    addEntryToBundleOnlyOnce(structuredBundle, SNOMED_PROBLEMS_LIST_DISPLAY, new BundleEntryComponent().setResource(problemList));
                                }
                                break;
                            }
                        }
                    }
                }
            });
        }

        // process MedicationStatements include if they reference an already referenced MedicationRequest or Medication
        // see https://developer.nhs.uk/apis/gpconnect-1-4-0/accessrecord_structured_development_linkages.html#problems
        if (resourceTypes.get(ResourceType.MedicationStatement) != null) {
            for (String medicationStatementReference : resourceTypes.get(ResourceType.MedicationStatement).keySet()) {
                MedicationStatement medicationStatement = (MedicationStatement) resourceTypes.get(ResourceType.MedicationStatement).get(medicationStatementReference);
                String medicationReference = medicationStatement.getMedicationReference().getReference();
                String medicationRequestReference = medicationStatement.getBasedOnFirstRep().getReference();
                if ((refCounts.keySet().contains(medicationReference) || refCounts.keySet().contains(medicationRequestReference))) {
                    addEntryToBundleOnlyOnce(structuredBundle, medicationStatementReference, new BundleEntryComponent().setResource(medicationStatement));
                }

                // #314
                if (parameterName.equals(INCLUDE_PROBLEMS_PARM)) {
                    boolean medicationsStatmentsPresent = false;
                    for (String reference : addedToResponse.keySet()) {
                        if (reference.startsWith("MedicationStatement/")) {
                            medicationsStatmentsPresent = true;
                            break;
                        }
                    }

                    // Do not constrcut primary meds lists for problems or comnsultations
                    if (false && medicationsStatmentsPresent) { // check to see if there are any medications statements that have been added 
                        long patientLogicalID = patient.getIdElement().getIdPartAsLong();
                        // add a medications list (only when problems are requested)
                        addEntryToBundleOnlyOnce(structuredBundle, SNOMED_MEDICATION_LIST_DISPLAY,
                                new BundleEntryComponent().setResource(
                                        createListEntry(SNOMED_MEDICATION_LIST_CODE, SNOMED_MEDICATION_LIST_DISPLAY, patient.getIdentifierFirstRep().getValue(), "" + patientLogicalID)));
                    }
                }
            }
        }

        if (parameterName.equals(INCLUDE_PROBLEMS_PARM) && patient.getIdElement().getIdPartAsLong() == PATIENT_2) {
            //handlePatient2ProblemsRequest(patient, structuredBundle);
            // add the secondary lists for problems
            for (String title : new String[]{
                PROBLEMS_MEDS_SECONDARY_LIST_TITLE,
                PROBLEMS_UNCATEGORISED_SECONDARY_LIST_TITLE}) {
                ListResource secondaryList = (ListResource) resourceTypes.get(ResourceType.List).get(title);
                addEntryToBundleOnlyOnce(structuredBundle, title, new BundleEntryComponent().setResource(secondaryList));
            }
        }

        if (parameterName.equals(INCLUDE_CONSULTATIONS_PARM) && patient.getIdElement().getIdPartAsLong() == PATIENT_2) {
            //handlePatient2ConsultationsRequest(patient, structuredBundle);
            // add the secondary lists for consultations
            for (String title : new String[]{
                CONSULTATION_MEDS_SECONDARY_LIST_TITLE,
                CONSULTATION_UNCATEGORISED_SECONDARY_LIST_TITLE,
                // #359 different secondary list for problems
                PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE}) {
                ListResource secondaryList = (ListResource) resourceTypes.get(ResourceType.List).get(title);
                addEntryToBundleOnlyOnce(structuredBundle, title, new BundleEntryComponent().setResource(secondaryList));
            }
        }

        HashMap<String, ListResource> listResources = rebuildBundledLists(structuredBundle, resourceTypes);

        // annotate the list if it is present but empty
        for (String listTitle : listResources.keySet()) {
            ListResource listResource = listResources.get(listTitle);
            if (listResource.getEntry().isEmpty()) {
                if (!listResource.hasNote()) {
                    addEmptyListNote(listResource);
                    addEmptyReasonCode(listResource);
                }
            }
        }

        if (DEBUG_LEVEL > 0) {
            for (String key : duplicates.keySet()) {
                System.err.println("Warning duplicate id: " + key + ", count = " + duplicates.get(key));
            }
        }
    }
// <editor-fold defaultstate="collapsed" desc="commented out but not used code">    
//    /**
//     *
//     * @param patient
//     * @param structuredBundle
//     */
//    private void handlePatient2ProblemsRequest(Patient patient, Bundle structuredBundle) {
//        // add a medications statement and a list
//        // There appears to be an empty list here but it does get populated since Medications are a special case for problems
//        //ListResource medicationStatementList = getOrCreateThenAddList(patient, structuredBundle, SNOMED_MEDICATION_LIST_CODE, SNOMED_MEDICATION_LIST_DISPLAY);
//       
//        // Much of this code is commented out since Pete G has requested the dummy resources which were previously added should now be removed.
//        // Following Pete S new sample message which includes secondary lists
//        
//        //ListResource allergyList = getOrCreateThenAddList(patient, structuredBundle, SNOMED_ACTIVE_ALLERGIES_CODE, SNOMED_ACTIVE_ALLERGIES_DISPLAY);
//        // this is atypical hence we have set set a title that is not the same as the display
//        //allergyList.setTitle(ACTIVE_ALLERGIES_TITLE);
//
//        //ListResource immunizationsList = getOrCreateThenAddList(patient, structuredBundle, SNOMED_IMMUNIZATIONS_LIST_CODE, SNOMED_IMMUNIZATIONS_LIST_DISPLAY);
//        //ListResource uncategorisedDataList = getOrCreateThenAddList(patient, structuredBundle, SNOMED_UNCATEGORISED_DATA_LIST_CODE, SNOMED_UNCATEGORISED_DATA_LIST_DISPLAY);
//
//        ListResource problemsList = getOrCreateThenAddList(patient, structuredBundle, SNOMED_PROBLEMS_LIST_CODE, SNOMED_PROBLEMS_LIST_DISPLAY);
//
//        // add any extants ca entries to the appropriate CA list
//        // make a copy because this collection will be modified.
//        String[] keys = addedToResponse.keySet().toArray(new String[0]);
//        HashSet<String> hs = new HashSet<>();
//        final String[] CLINICAL_AREAS = new String[]{"MedicationStatement"/*, "AllergyIntolerance", "Immunization", "Observation"*/};
//        for (String id : keys) {
//            String itemType = id.replaceFirst("/.*$", "");
//            for (String ca : CLINICAL_AREAS) {
//                if (id.startsWith(ca) && !hs.contains(itemType)) {
//                    ListResource listResource = null;
//                    switch (ca) {
//                        case "MedicationStatement":
//                            //listResource = medicationStatementList;
//                            break;
//
//                        case "AllergyIntolerance":
//                            //listResource = allergyList;
//                            break;
//
//                        case "Immunization":
//                            //listResource = immunizationsList;
//                            break;
//
//                        case "Observation":
//                            //listResource = uncategorisedDataList;
//                            break;
//                    }
//
//                    if (listResource != null) {
//                        //addExistingResourceReferenceToList(patient, listResource, id, structuredBundle, problemsList);
//                        hs.add(itemType);
//                    }
//                    break;
//                }
//            }
//        } // for id
//
//        //======================================================================
//        // handle cases where we know there are no Clinical Areas
//        // we know patient 2 does not have any of these so add some
//        if (!hs.contains("AllergyIntolerance")) {
//            //addDummyResourceAndProblem(patient, createAllergyIntolerance(), allergyList, structuredBundle, problemsList);
//        }
//        if (!hs.contains("Immunization")) {
//            //addDummyResourceAndProblem(patient, createImmunization(), immunizationsList, structuredBundle, problemsList);
//        }
//        if (!hs.contains("Observation")) {
//            // Uncategorised Data
//            //addDummyResourceAndProblem(patient, createObservation(), uncategorisedDataList, structuredBundle, problemsList);
//        }
//    }

//    /**
//     * minimal allergy with just mandatory elements
//     *
//     * @return instantiated object
//     */
//    private AllergyIntolerance createAllergyIntolerance() {
//        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
//
//        allergyIntolerance.setMeta(new Meta().addProfile(SystemURL.SD_CC_ALLERGY_INTOLERANCE));
//
//        // identifier
//        allergyIntolerance.setIdentifier(Collections.singletonList(new Identifier().
//                setSystem(ID_CROSS_CARE_SETTIING).
//                setValue("f827590b-5193-11ea-8153-1002b58598b5")));
//
//        // clinical status
//        allergyIntolerance.setClinicalStatus(AllergyIntolerance.AllergyIntoleranceClinicalStatus.ACTIVE);
//
//        // verification status unconfirmed
//        allergyIntolerance.setVerificationStatus(AllergyIntolerance.AllergyIntoleranceVerificationStatus.UNCONFIRMED);
//
//        // category Code value environment
//        allergyIntolerance.addCategory(AllergyIntoleranceCategory.ENVIRONMENT);
//
//        // code CodeableConcept
//        CodeableConcept cc = createCodeableConcept("89707004", "Sesame oil (substance)", SNOMED_URL);
//        allergyIntolerance.setCode(cc);
//
//        // patient
//        allergyIntolerance.setPatient(new Reference("Patient/2"));
//
//        // recorder practitioner/1
//        allergyIntolerance.setRecorder(new Reference("Practitioner/1"));
//
//        // reaction.manifestation
//        AllergyIntoleranceReactionComponent reaction = new AllergyIntoleranceReactionComponent();
//        cc = createCodeableConcept("230145002", "Difficulty breathing", SNOMED_URL);
//        reaction.setManifestation(Collections.singletonList(cc));
//        allergyIntolerance.setReaction(Collections.singletonList(reaction));
//
//        return allergyIntolerance;
//    }
//    /**
//     * minimal Immunization with just mandatory elements
//     *
//     * @return instantiated object
//     */
//    private Immunization createImmunization() {
//        Immunization immunization = new Immunization();
//
//        // meta
//        immunization.setMeta(new Meta().addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Immunization-1"));
//
//        // ext recordedDate
//        immunization.addExtension(new Extension("https://fhir.hl7.org.uk/STU3/StructureDefinition/Extension-CareConnect-DateRecorded-1").
//                setValue(new DateTimeType(BASE_DATE)));
//
//        CodeableConcept cc = createCodeableConcept("170378007", "First hepatitis A vaccination", SNOMED_URL);
//
//        // ext vaccinationProcedure
//        immunization.addExtension(new Extension("https://fhir.hl7.org.uk/STU3/StructureDefinition/Extension-CareConnect-VaccinationProcedure-1").
//                setValue(cc));
//
//        // identifier
//        immunization.setIdentifier(Collections.singletonList(new Identifier().
//                setSystem(ID_CROSS_CARE_SETTIING).
//                setValue("urn:uuid:eba25af1-5b74-4790-aa5a-2134fd27ad76")));
//
//        // status
//        immunization.setStatus(Immunization.ImmunizationStatus.COMPLETED);
//
//        // notgiven
//        immunization.setNotGiven(false);
//
//        // vaccineCode
//        cc = createCodeableConcept("UNK", null, "http://hl7.org/fhir/v3/NullFlavor");
//        cc.setText("Unknown");
//        immunization.setVaccineCode(cc);
//
//        // patient
//        immunization.setPatient(new Reference("Patient/2"));
//
//        // primarySource
//        immunization.setPrimarySource(true);
//
//        // vaccinationProtocol
//        ImmunizationVaccinationProtocolComponent component = new ImmunizationVaccinationProtocolComponent();
//
//        //  targetDisease
//        component.setTargetDisease(Collections.singletonList(createCodeableConcept("40468003", "Viral hepatitis, type A", SNOMED_URL)));
//
//        //  dosestatus
//        component.setDoseStatus(createCodeableConcept("count", "Counts", "http://hl7.org/fhir/stu3/valueset-vaccination-protocol-dose-status.html"));
//
//        immunization.setVaccinationProtocol(Collections.singletonList(component));
//
//        return immunization;
//    }
//    /**
//     * minimal Observation with just mandatory elements
//     *
//     * @return instantiated object
//     */
//    private Observation createObservation() {
//        Observation observation = new Observation();
//
//        // meta
//        observation.setMeta(new Meta().addProfile(SD_GPC_OBSERVATION));
//
//        // identifier
//        observation.setIdentifier(Collections.singletonList(new Identifier().
//                setSystem(ID_CROSS_CARE_SETTIING).
//                setValue("urn:uuid:eba25af1-5b74-4790-aa5a-2134fd27ad77")));
//
//        observation.setStatus(Observation.ObservationStatus.FINAL);
//
//        // code 
//        observation.setCode(createCodeableConcept("37331000000100", "Comment note", SNOMED_URL));
//
//        // subject
//        observation.setSubject(new Reference("Patient/2"));
//
//        // issued
//        observation.setIssued(BASE_DATE);
//
//        // performer
//        observation.setPerformer(Collections.singletonList(new Reference("Practitioner/1")));
//
//        return observation;
//    }
//    /**
//     *
//     * @param patient
//     * @param structuredBundle
//     */
//    private void handlePatient2ConsultationsRequest(Patient patient, Bundle structuredBundle) {
//        // there are medications statements but not yet a list with enries
//        ListResource medicationStatementList = getOrCreateThenAddList(patient, structuredBundle, SNOMED_MEDICATION_LIST_CODE, SNOMED_MEDICATION_LIST_DISPLAY);
//        // add the references to the list
//        for (BundleEntryComponent entry : structuredBundle.getEntry()) {
//            if (entry.getResource() instanceof MedicationStatement) {
//                medicationStatementList.addEntry(new ListEntryComponent().setItem(new Reference(entry.getResource().getResourceType() + "/" + entry.getResource().getId())));
//            }
//        }
//
//        // add an allergy into the bundle and add a reference entry to Consultation1
//        addDummyResourceForConsultation(createAllergyIntolerance(), patient, structuredBundle, SNOMED_ACTIVE_ALLERGIES_CODE, SNOMED_ACTIVE_ALLERGIES_DISPLAY);
//        addDummyResourceForConsultation(createImmunization(), patient, structuredBundle, SNOMED_IMMUNIZATIONS_LIST_CODE, SNOMED_IMMUNIZATIONS_LIST_DISPLAY);
//        addDummyResourceForConsultation(createObservation(), patient, structuredBundle, SNOMED_UNCATEGORISED_DATA_LIST_CODE, SNOMED_UNCATEGORISED_DATA_LIST_DISPLAY);
//    }
//    /**
//     *
//     * @param resource
//     * @param patient
//     * @param structuredBundle
//     * @param code
//     * @param display
//     */
//    private void addDummyResourceForConsultation(Resource resource, Patient patient, Bundle structuredBundle, String code, String display) {
//        resource.setId("Added" + resource.getResourceType() + "ForConsultation");
//        // add the resource reference to the topic
//        ((ListResource) addedToResponse.get("List/Consultation1-Topic1")).
//                addEntry(new ListEntryComponent().setItem(new Reference(resource.getResourceType() + "/" + resource.getId())));
//
//        // add the resource to the resource list
//        ListResource list = getOrCreateThenAddList(patient, structuredBundle, code, display);
//        // this is  a typical hence we have set set a title that is not the same as the display
//        if (resource instanceof AllergyIntolerance) {
//            list.setTitle(ACTIVE_ALLERGIES_TITLE);
//        }
//
//        // add the resource reference to the list
//        list.addEntry(new ListEntryComponent().setItem(new Reference(resource.getResourceType() + "/" + resource.getId())));
//
//        // add the resource to the bundle
//        addEntryToBundleOnlyOnce(structuredBundle, resource.getResourceType() + "/" + resource.getId(), new BundleEntryComponent().setResource(resource));
//    }
//    /**
//     * This should only be called once per resource type Created resource has
//     * minimal content
//     *
//     * @param patient
//     * @param resource resource object to be populated
//     * @param listResource associated list eg Allergies, Meds etc
//     * @param structuredBundle
//     * @param problemsList
//     */
//    private void addDummyResourceAndProblem(Patient patient, Resource resource, ListResource listResource, Bundle structuredBundle, ListResource problemsList) {
//
//        // the resource to the appropriate list
//        resource.setId(resource.getResourceType() + "ForAddedProblem");
//        listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(resource.getResourceType() + "/" + resource.getId())));
//        if (addEntryToBundleOnlyOnce(structuredBundle, resource.getResourceType() + "/" + resource.getId(), new BundleEntryComponent().setResource(resource))) {
//            refCounts.put(resource.getResourceType() + "/" + resource.getId(), 1);
//        }
//
//        // add the problem
//        Condition condition = createCondition(resource.getId().replaceFirst("^.*/", ""), patient, resource.fhirType() + "/" + resource.getId());
//        addEntryToBundleOnlyOnce(structuredBundle, condition.getResourceType() + "/" + condition.getId(), new BundleEntryComponent().setResource(condition));
//
//        // add the problem to the problem list
//        problemsList.addEntry(new ListEntryComponent().setItem(new Reference("Condition/" + condition.getId())));
//    }
//    /**
//     *
//     * @param patient
//     * @param listResource associated resource list eg Medications, Allergies
//     * etc
//     * @param reference resource id that is going be added to lists
//     * @param structuredBundle
//     * @param problemsList
//     */
//    private void addExistingResourceReferenceToList(Patient patient, ListResource listResource, String reference, Bundle structuredBundle, ListResource problemsList) {
//
//        // add to resource list
//        listResource.addEntry(new ListEntryComponent().setItem(new Reference(reference)));
//
//        // create a condition, add it to the response
//        Condition condition = createCondition(reference.replaceFirst("^(.*)/", ""), patient, reference);
//        addEntryToBundleOnlyOnce(structuredBundle, condition.getResourceType() + "/" + condition.getId(), new BundleEntryComponent().setResource(condition));
//
//        // add a reference to the condition to the problem list
//        problemsList.addEntry(new ListEntryComponent().setItem(new Reference(condition.getResourceType() + "/" + condition.getId())));
//    }
//    /**
//     *
//     * @param patient to whom query relates
//     * @param structuredBundle to add to
//     * @param code for list
//     * @param display for list
//     * @return populated ListResource
//     */
//    private ListResource getOrCreateThenAddList(Patient patient, Bundle structuredBundle, String code, String display) {
//        ListResource listResource = null;
//        if (addedToResponse.get(display) != null) {
//            listResource = (ListResource) addedToResponse.get(display);
//        } else {
//            listResource = createList(code, display, patient);
//            addEntryToBundleOnlyOnce(structuredBundle, display, new BundleEntryComponent().setResource(listResource));
//        }
//        return listResource;
//    }
// </editor-fold>
    /**
     * Compare addedToResponse set and refCounts Hash keys
     */
    private void compareRefCountsAndAddedToResponse() {
        System.out.println("addedToResponse count = " + addedToResponse.size());
        System.out.println("refCounts key count = " + refCounts.size());

        int matching = 0;
        for (String reference : addedToResponse.keySet()) {
            if (!reference.endsWith("are not supported by the provider system")) {
                if (!refCounts.keySet().contains(reference)) {
                    System.out.println("Present in addedToResponse but not refCounts: " + reference);
                } else {
                    matching++;
                }
            } else {
                System.out.println("Excluding from addedToResponse: " + reference);
            }
        }
        System.out.println("Also present in refCounts " + matching);

        matching = 0;
        for (String reference : refCounts.keySet()) {
            if (!reference.endsWith("are not supported by the provider system")) {
                if (!addedToResponse.keySet().contains(reference)) {
                    System.out.println("Present in refCounts but not addedToResponse: " + reference);
                } else {
                    matching++;
                }
            } else {
                System.out.println("Excluding from refCounts: " + reference);
            }
        }
        System.out.println("Also present in addedToResponse " + matching);
    }

    /**
     *
     * @param bundle
     * @param reference
     * @param entry
     * @return boolean indicating whether entry was added
     */
    private boolean addEntryToBundleOnlyOnce(Bundle bundle, String reference, BundleEntryComponent entry) {
        boolean retval = false;
        if (!addedToResponse.keySet().contains(reference)) {
            retval = true;
            bundle.addEntry(entry);
            addedToResponse.put(reference, entry.getResource());
            refCounts.put(reference, 1);
        }
        return retval;
    }

    /**
     * get the correct parser for the input format
     *
     * @param suffix
     * @param ctx
     * @return the correct parser
     */
    private IParser getParser(String suffix, FhirContext ctx) {
        IParser parser = null;
        switch (suffix.toLowerCase()) {
            case "xml":
                parser = ctx.newXmlParser();
                break;
            case "json":
                parser = ctx.newJsonParser();
                break;
            default:
                System.err.println("StructuredBuilder Unrecognised file type " + suffix);
        }
        return parser;
    }

    /**
     * Generic List creator
     *
     * @param snomed code
     * @param snodem display
     * @param nhsNumber
     * @param patientId
     * @return populated ListResource
     */
    private ListResource createListEntry(String snomedCode, String snomedDisplay, String nhsNumber, String patientId) {
        ListResource listResource = new ListResource();

        listResource.setMeta(new Meta().addProfile(SystemURL.SD_GPC_LIST));
        listResource.setStatus(ListResource.ListStatus.CURRENT);

        listResource.setMode(ListResource.ListMode.SNAPSHOT);
        listResource.setTitle(snomedDisplay);
        listResource.setCode(createCodeableConcept(snomedCode, snomedDisplay, SystemConstants.SNOMED_URL));

        listResource.setSubject(
                new Reference(new IdType("Patient", patientId)).setIdentifier(new Identifier().setValue(nhsNumber).setSystem(SystemURL.ID_NHS_NUMBER)));
        listResource.setDate(BASE_DATE);
        listResource.setOrderedBy(createCodeableConcept("event-date", "Sorted by Event Date", SystemURL.CS_LIST_ORDER));

        listResource.addExtension(setClinicalSetting());

        return listResource;
    }

    /**
     * modify the canned response to be consistent with demonstrator data
     */
    private String editResponse(Patient patient, String s) {
        // change all the references of the form .*/.* to be consistent
        String[][] referenceSubstitutions = new String[][]{
            // resource, reference to set on RHS of "/"
            {"Patient", "" + patient.getIdElement().getIdPartAsLong()},
            {"Practitioner", "1"},
            {"Organization", "7"}
        };
        // nb reference substitions
        for (String[] substitution : referenceSubstitutions) {
            s = s.replaceAll("\"" + substitution[0] + "/.*\"", "\"" + substitution[0] + "/" + substitution[1] + "\"");
        }

        HumanName name = patient.getNameFirstRep();
        // do other substitutions vanilla text substitutions
        String[][] quotedSubstitutions = new String[][]{
            {"REARDON, John", name.getFamily().toUpperCase() + ", " + name.getGiven().get(0)}, // Patient Name
            {"SMITH", "GILBERT"}, // GP
            {"GREENTOWN GENERAL HOSPITAL", "COXWOLD Surgery"} // Site
        };
        // nb straight substitutions
        // standard reg exp substitutions
        for (String[] substitution : quotedSubstitutions) {
            s = s.replaceAll("\"" + substitution[0] + "\"", "\"" + substitution[1] + "\"");
        }
        return s;
    }

    /**
     *
     * @param parameterName name of query parameter
     * @param structuredBundle target bundle to which lists are added
     * @param resourceTypes hash of parsed resource objects from canned response
     * file
     */
    private void addInTopLevelResourceLists(String parameterName, Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        AbstractListManager listManager = instantiateListManager(parameterName, resourceTypes);
        if (listManager != null) {
            // #290 only have the list returned that is relevant to the query.
            addEntryToBundleOnlyOnce(
                    structuredBundle, listManager.getListTitle(), new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listManager.getListTitle())));
        }
    }

    /**
     * rebuild all the canned bundled lists walks through the assembled bundle
     *
     * @param structuredBundle
     * @param resourceTypes
     * @return HashMap of ListResource
     */
    private HashMap<String, ListResource> rebuildBundledLists(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        HashMap<String, ListResource> listResources = new HashMap<>();
        for (BundleEntryComponent entry : structuredBundle.getEntry()) {
            if (entry.getResource() instanceof ListResource) {
                ListResource listResource = (ListResource) entry.getResource();
                if (listResource.getTitle() != null) {
                    String listTitle = listResource.getTitle();
                    System.out.println("Checking " + listTitle);
                    if (Arrays.asList(SNOMED_LIST_TITLES).contains(listTitle)
                            && // dont rebuild uncanned lists here, (only happens when canned are mixed with uncanned)
                            !Arrays.asList(new String[]{SNOMED_ACTIVE_ALLERGIES_DISPLAY, SNOMED_RESOLVED_ALLERGIES_DISPLAY, SNOMED_MEDICATION_LIST_DISPLAY}).contains(listTitle)) {
                        System.out.println("Rebuilding " + listTitle + " record count = " + listResource.getEntry().size());
                        for (ListEntryComponent listEntry : listResource.getEntry()) {
                            System.out.println("\t" + listEntry.getItem().getReference());
                        }

                        listResource.getEntry().clear();

                        // This is a reverse lookup
                        String includeParameter = getParameterFromTitle(listTitle);
                        AbstractListManager listManager = instantiateListManager(includeParameter, resourceTypes);
                        if (listManager != null) {
                            listManager.populateListResoure(listResources, listResource);
                        }
                        System.out.println("After - record count = " + listResource.getEntry().size());
                        for (ListEntryComponent listEntry : listResource.getEntry()) {
                            System.out.println("\t" + listEntry.getItem().getReference());
                        }

                        break;
                    } // if matching id
                } // if non null listTitle
            } // if ListResource
        } // for entry

        return listResources;
    }

    /**
     * enumerate the top level items for Problems or Consultation see
     * https://structured-1-4-0.netlify.com/accessrecord_structured_development_consultation_guidance.html
     * and
     * https://structured-1-4-0.netlify.com/accessrecord_structured_development_problems_guidance.html
     *
     * @param root Clinical area list Problems, Consultations, Referrals,
     * Investigations
     * @param structuredBundle response bundle to be appended to
     * @param resourceTypes hashmap of resources keyed by type
     * @param parms List of Parameters for this clinical area
     */
    private void walkRoot(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes,
            ArrayList<Parameters.ParametersParameterComponent> parms) throws FHIRException {

        AbstractListManager listManager = instantiateListManager(parms.get(0).getName(), resourceTypes);
        if (listManager != null) {
            listManager.processParm(resourceTypes, parms, structuredBundle);
        }

        if (DEBUG_LEVEL > 0) {
            // show all unreferenced resources
            for (ResourceType rtKey : resourceTypes.keySet()) {
                for (String key : resourceTypes.get(rtKey).keySet()) {
                    if (refCounts.get(key) == null) {
                        if (!Arrays.asList(SNOMED_LIST_TITLES).contains(key)) {
                            System.err.println("Warning Unreferenced key: " + key);
                        }
                    }
                }
            }
        }
    } // walkRoot

    /**
     * process a period and start date and decide whether to add the id to the
     * hashset
     *
     * @param searchPeriod
     * @param idsToAdd
     * @param resource
     * @param startDate
     */
    private void addIdIfInPeriod(Period searchPeriod, HashSet<String> idsToAdd, Resource resource, Date startDate) {
        // not sure this would ever happen
        if (searchPeriod.getStart() == null && searchPeriod.getEnd() == null) {
            idsToAdd.add(resource.getId());
        } else if (searchPeriod.getStart() != null && searchPeriod.getEnd() == null && startDate.compareTo(searchPeriod.getStart()) >= 0) {
            idsToAdd.add(resource.getId());
        } else if (searchPeriod.getStart() == null && searchPeriod.getEnd() != null && startDate.compareTo(searchPeriod.getEnd()) <= 0) {
            idsToAdd.add(resource.getId());
        } else if (searchPeriod.getStart() != null && searchPeriod.getEnd() != null
                && startDate.compareTo(searchPeriod.getStart()) >= 0 && startDate.compareTo(searchPeriod.getEnd()) <= 0) {
            idsToAdd.add(resource.getId());
        }
    }

    /**
     * populates ref counts as it goes recursion recursively descend resolving
     * references as we go. The completer for the is resources not containing
     * references For extensions add new Resource types here
     *
     * @param resourceTypes hashmap of resources keyed by type
     * @param referenceStr
     * @param indent indent level to use for tree
     */
    private void walkResource(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes,
            String referenceStr, int indent) throws FHIRException {
        if (referenceStr != null && referenceStr.matches("^.*/.*$")) {
            ResourceType rt = ResourceType.valueOf(referenceStr.replaceFirst("^(.*)/.*$", "$1"));
            HashMap<String, Resource> hm = resourceTypes.get(rt);
            if (hm != null) {
                DomainResource resource = (DomainResource) hm.get(referenceStr);
                if (resource != null) {
                    if (resource instanceof ListResource) {
                        ListResource list = (ListResource) resource;
                        for (ListResource.ListEntryComponent entry : list.getEntry()) {
                            if (entry.getItem() instanceof Reference) {
                                Reference reference = (Reference) entry.getItem();
                                if (reference.getReference() != null) {
                                    countReferences(reference, indent, structuredBundle, resourceTypes);
                                }
                            }
                        }
                    } else if (resource instanceof MedicationRequest) {
                        MedicationRequest mr = (MedicationRequest) resource;
                        Reference reference = mr.getMedicationReference();
                        if (reference != null) {
                            countReferences(reference, indent, structuredBundle, resourceTypes);
                        }
                        reference = mr.getRecorder();
                        if (reference != null) {
                            countReferences(reference, indent, structuredBundle, resourceTypes);
                        }
                    } else if (resource instanceof ReferralRequest) {
                        // This clause intentionally left blank
                    } else if (resource instanceof DiagnosticReport) {
                        DiagnosticReport diagnosticReport = (DiagnosticReport) resource;
                        for (Reference reference : diagnosticReport.getBasedOn()) {
                            countReferences(reference, indent, structuredBundle, resourceTypes);
                        }

                        for (Reference reference : diagnosticReport.getSpecimen()) {
                            countReferences(reference, indent, structuredBundle, resourceTypes);
                        }

                        for (Reference reference : diagnosticReport.getResult()) {
                            countReferences(reference, indent, structuredBundle, resourceTypes);
                        }

                    } else if (resource instanceof DomainResource) { // catch all
                        DomainResource domainResource = (DomainResource) resource;
                        for (Extension extension : domainResource.getExtension()) {
                            if (extension.getValue() instanceof Reference) {
                                Reference reference = (Reference) extension.getValue();
                                countReferences(reference, indent, structuredBundle, resourceTypes);
                            }
                        }
                    }
                }
            }
        } else {
            System.err.println("Warning Unexpected reference format: " + referenceStr);
        }
    }

    /**
     * add a reference to the count and continue to recurse
     *
     * @param reference
     * @param indent
     * @param structuredBundle
     * @param resourceTypes
     */
    private void countReferences(Reference reference, int indent, Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) throws FHIRException {
        String referenceStr = reference.getReference();
        if (refCounts.get(referenceStr) == null) {
            refCounts.put(referenceStr, 1);
        } else {
            refCounts.put(referenceStr, refCounts.get(referenceStr) + 1);
        }
        //System.out.println(StringUtils.repeat("\t", indent) + referenceStr);
        walkResource(structuredBundle, resourceTypes, referenceStr, indent + 1);
    }

    /**
     * List to stdout second indent is references
     *
     * @param resourceTypes
     */
    private void dumpResources(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        for (ResourceType resourceType : resourceTypes.keySet()) {
            System.out.println(resourceType);
            HashMap<String, Resource> hmResources = resourceTypes.get(resourceType);
            for (String key : hmResources.keySet()) {
                Resource resource = hmResources.get(key);
                System.out.println("\t" + key);
                if (resource instanceof ListResource) {
                    ListResource list = (ListResource) resource;
                    for (ListResource.ListEntryComponent entry : list.getEntry()) {
                        if (entry.getItem() instanceof Reference) {
                            Reference reference = (Reference) entry.getItem();
                            if (reference.getReference() != null) {
                                System.out.println("\t\t" + reference.getReference());
                            }
                        }
                    }
                } else if (resource instanceof DomainResource) {
                    DomainResource domainResource = (DomainResource) resource;
                    for (Extension extension : domainResource.getExtension()) {
                        if (extension.getValue() instanceof Reference) {
                            System.out.println("\t\t" + ((Reference) extension.getValue()).getReference());
                        }
                    }
                }
            }
        } // for
    }

// <editor-fold defaultstate="collapsed" desc="List manager classes">  
    /**
     * Base class for List Managers There is some magic required here to invoke
     * an inner class constructor see
     * https://stackoverflow.com/questions/17485297/how-to-instantiate-an-inner-class-with-reflection-in-java
     *
     * @param paramName
     * @return AbstractListManager instance
     */
    private AbstractListManager instantiateListManager(String paramName, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        StructuredBuilder.AbstractListManager listManager = null;
        try {
            // map from name of clinical area parameter to appropriate class name
            Class<?> toRun = Class.forName(getClass().getName() + "$" + paramName.replaceFirst("include", "") + LIST_MANAGER_SUFFIX);
            Constructor<?> ctor = toRun.getDeclaredConstructor(getClass(), resourceTypes.getClass());
            listManager = (AbstractListManager) ctor.newInstance(this, resourceTypes);
        } catch (Exception ex) {
            System.err.println("Failed to instantiate ListManager " + paramName + "" + ex.getMessage());
        }
        return listManager;
    }

    /**
     * Lazy evaluation of lookup table. This mapping is done automatically via
     * introspection. Get the base class enumerate derived classes and construct
     * and invoke title method accordingly There is some magic required here to
     * invoke an inner class constructor see
     * https://stackoverflow.com/questions/17485297/how-to-instantiate-an-inner-class-with-reflection-in-java
     *
     * @param List title
     * @return associatedParameterName
     */
    private String getParameterFromTitle(String title) {
        if (listTitleToParameterName == null) {
            listTitleToParameterName = new HashMap<>();
            for (Class<?> clas : getClass().getDeclaredClasses()) {
                String name = clas.getName();
                if (!name.contains("Abstract") && name.endsWith(LIST_MANAGER_SUFFIX)) {
                    try {
                        Constructor<?> ctor = clas.getDeclaredConstructor(getClass(), new HashMap<>().getClass());
                        if (ctor != null) {
                            try {
                                AbstractListManager listManager = (AbstractListManager) ctor.newInstance(this, new HashMap<>());
                                if (listManager != null) {
                                    String pTitle = listManager.getListTitle();
                                    listTitleToParameterName.put(pTitle, name.replaceFirst("^" + getClass().getName() + "\\$(.*)" + LIST_MANAGER_SUFFIX + "$", "include$1"));

                                }
                            } catch (Exception ex) {
                                Logger.getLogger(StructuredBuilder.class
                                        .getName()).log(Level.SEVERE, null, ex);

                            }
                        }
                    } catch (NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(StructuredBuilder.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listTitleToParameterName.get(title);
    }

    private static final String LIST_MANAGER_SUFFIX = "ListManager";
    private static HashMap<String, String> listTitleToParameterName = null;

    /**
     * encapsulates parameter specific handling related to lists
     */
    abstract public class AbstractListManager {

        protected HashMap<ResourceType, HashMap<String, Resource>> resourceTypes = null;

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public AbstractListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            this.resourceTypes = resourceTypes;
        }

        /**
         *
         * @param resourceTypes
         * @param parms
         * @param structuredBundle
         * @throws FHIRException
         */
        public void processParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
        }

        /**
         * puts references to resources into Fhir list resource
         *
         * @param listResources map of maps top level key being resource name
         * lower level key is id
         * @param listResource List resource to be populated
         */
        public void populateListResoure(HashMap<String, ListResource> listResources, ListResource listResource) {
            listResources.put(getListTitle(), listResource);
            refCounts.keySet().stream().filter((key) -> (key.startsWith(getFhirResourceName() + "/"))).sorted().forEachOrdered((String key) -> {
                listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(key)));
            });
        }

        /**
         *
         * @return
         */
        abstract public String getListTitle();

        /**
         *
         * @return the prefix of the id associated with this list This is a Fhir
         * Resource Name
         */
        protected String getFhirResourceName() {
            return "";
        }

    } // AbstractListManager

    /**
     * Allergies. This is not typical there are two lists that may be returned
     * active allergies and ended allergies
     */
    public class AllergiesListManager extends AbstractListManager {

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public AllergiesListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            super(resourceTypes);
        }

        @Override
        public String getListTitle() {
            return SNOMED_ACTIVE_ALLERGIES_DISPLAY;
        }

        @Override
        protected String getFhirResourceName() {
            return "AllergyIntolerance";
        }

    } // Allergies

    /**
     * Medication
     */
    public class MedicationListManager extends AbstractListManager {

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public MedicationListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            super(resourceTypes);
        }

        @Override
        public String getListTitle() {
            return SNOMED_MEDICATION_LIST_DISPLAY;
        }

        @Override
        protected String getFhirResourceName() {
            return "MedicationStatement";
        }
    } // Medications

    /**
     * Problems
     */
    public class ProblemsListManager extends AbstractListManager {

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public ProblemsListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            super(resourceTypes);
        }

        @Override
        /**
         * Problems increment the ref counts for resources matching the search
         * criteria to the response NB All the parameters have been validated by
         * now so we don't need to do any further checks
         *
         * @param resourceTypes
         * @param parms
         * @param structuredBundle
         * @throws FHIRException
         */
        public void processParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes,
                ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
            HashMap<String, Resource> conditions = resourceTypes.get(ResourceType.Condition);

            for (Parameters.ParametersParameterComponent parm : parms) {
                String filterSignificance = null;
                ConditionClinicalStatus filterStatus = null;

                for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                    switch (paramPart.getName()) {
                        case FILTER_SIGNIFICANCE_PARAM_PART:
                            filterSignificance = ((CodeType) paramPart.getValue()).getValue();
                            break;
                        case FILTER_STATUS_PARAM_PART:
                            filterStatus = ConditionClinicalStatus.valueOf(((CodeType) paramPart.getValue()).getValue().toUpperCase());
                            break;
                    }
                }

                Resource[] resources = conditions.values().toArray(new Resource[0]);
                for (Resource resource : resources) {
                    Condition condition = (Condition) resource;
                    List<Extension> significances = condition.getExtensionsByUrl(SD_CC_EXT_PROBLEM_SIGNIFICANCE);
                    if ((filterSignificance == null || significances.isEmpty()
                            // #300 significance changed from CodeableConcept to Code
                            || filterSignificance.equalsIgnoreCase(significances.get(0).getValue().toString()))
                            && (filterStatus == null || condition.getClinicalStatus() == filterStatus)) {
                        refCounts.put(condition.getId(), 1);
                        walkResource(structuredBundle, resourceTypes, condition.getId(), 1);
                    }
                }
            } // for Parameters

        } // processParm

        @Override
        public String getListTitle() {
            return SNOMED_PROBLEMS_LIST_DISPLAY;
        }

        @Override
        protected String getFhirResourceName() {
            return "Condition";
        }
    } // Problems

    /**
     * Consultations
     */
    public class ConsultationsListManager extends AbstractListManager {

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public ConsultationsListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            super(resourceTypes);
        }

        @Override
        /**
         * Consultations increment the ref counts for resources matching the
         * search criteria to the response
         *
         * @param resourceTypes
         * @param parms
         * @param structuredBundle
         * @throws FHIRException
         */
        public void processParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes,
                ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
            HashMap<String, Resource> encounters = resourceTypes.get(ResourceType.Encounter);
            HashMap<String, Resource> lists = resourceTypes.get(ResourceType.List);

            HashMap<String, ListResource> consultations = new HashMap<>();
            for (String key : lists.keySet()) {
                ListResource list = (ListResource) lists.get(key);
                if (list.getId() != null && list.getCode().getCodingFirstRep().getCode().equals(SNOMED_CONSULTATION_ENCOUNTER_TYPE)) {
                    consultations.put(list.getId(), list);
                }
            }

            Period searchPeriod = null;
            int numberofMostRecent = -1;
            for (Parameters.ParametersParameterComponent parm : parms) {

                for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                    switch (paramPart.getName()) {
                        case CONSULTATION_SEARCH_PERIOD_PARAM_PART:
                            searchPeriod = (Period) paramPart.getValue();
                            break;
                        case NUMBER_OF_MOST_RECENT_PARAM_PART:
                            numberofMostRecent = ((IntegerType) paramPart.getValue()).getValue();
                            break;
                    }
                }

            } // for Parameters

            HashSet<String> encounterIdsToAdd = new HashSet<>();

            // Consultations are basically 1 - 1 with encounters
            List<Resource> se = new ArrayList<>(encounters.values());
            // sorts descending on encounter start date
            Comparator<Resource> comparator
                    = (Resource left, Resource right) -> ((Encounter) right).getPeriod().getStart().compareTo(((Encounter) left).getPeriod().getStart());
            // add the most recent n encounters
            Collections.sort(se, comparator);
            int addedCount = 0;
            if (numberofMostRecent > 0) {
                for (Resource e : se) {
                    encounterIdsToAdd.add(e.getId());
                    if (++addedCount >= numberofMostRecent) {
                        break;
                    }
                }
            }

            if (searchPeriod != null) {
                for (ListResource consultation : consultations.values()) {
                    // get the start date of the consultation from the encounter
                    Encounter encounter = (Encounter) encounters.get(consultation.getEncounter().getReference());
                    Date startDate = encounter.getPeriod().getStart();
                    addIdIfInPeriod(searchPeriod, encounterIdsToAdd, encounter, startDate);
                }
            }

            // no filters - add everything
            if (numberofMostRecent < 0 && searchPeriod == null) {
                for (Resource encounter : encounters.values()) {
                    encounterIdsToAdd.add(encounter.getId());
                }
            }

            ListResource consultationList = (ListResource) lists.get(SNOMED_CONSULTATION_LIST_DISPLAY);

            // make sure the included Consultation are added
            for (ListResource consultation : consultations.values()) {
                String referencedEncounter = consultation.getEncounter().getReference();
                if (encounterIdsToAdd.contains(referencedEncounter)) {
                    for (ListResource.ListEntryComponent encounterEntry : consultationList.getEntry()) {
                        // check the entries in Consultations and add consultation if it has a references to the same encounter
                        if (encounterEntry.getItem().getReference().equals(referencedEncounter)) {
                            refCounts.put(consultation.getId(), 1);
                            walkResource(structuredBundle, resourceTypes, consultation.getId(), 1);

                            // and for the corresponding encounter
                            refCounts.put(referencedEncounter, 1);
                            walkResource(structuredBundle, resourceTypes, referencedEncounter, 1);
                        }
                    }
                }
            }

            for (String encounterId : encounterIdsToAdd) {
                walkResource(structuredBundle, resourceTypes, encounterId, 1);
            }

        } // processParm

        @Override
        public String getListTitle() {
            return SNOMED_CONSULTATION_LIST_DISPLAY;
        }

        /**
         * NB This is the only subclass where there is an override of this
         * method.
         *
         * @param listResources
         * @param listResource
         */
        @Override
        public void populateListResoure(HashMap<String, ListResource> listResources, ListResource listResource) {
            listResources.put(getListTitle(), listResource);
            refCounts.keySet().stream().filter((key) -> (key.startsWith(getFhirResourceName()))).sorted().forEachOrdered((key) -> {
                ListResource listResourceConsultation = (ListResource) resourceTypes.get(ResourceType.List).get(key);
                if (listResourceConsultation.getCode().getCodingFirstRep().getCode().equals(SNOMED_CONSULTATION_ENCOUNTER_TYPE)) {
                    listResource.addEntry(new ListResource.ListEntryComponent().setItem(listResourceConsultation.getEncounter()));
                }
            });
        }

        @Override
        protected String getFhirResourceName() {
            return "List";
        }
    } // Consultations

    /**
     * Investigations
     */
    public class InvestigationsListManager extends AbstractListManager {

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public InvestigationsListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            super(resourceTypes);
        }

        @Override
        /**
         * Investigations increment the ref counts for resources matching the
         * search criteria to the response
         *
         * @param resourceTypes
         * @param parms
         * @param structuredBundle
         * @throws FHIRException
         */
        public void processParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes,
                ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
            HashMap<String, Resource> diagnosticReports = resourceTypes.get(ResourceType.DiagnosticReport);

            Period searchPeriod = null;
            for (Parameters.ParametersParameterComponent parm : parms) {

                for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                    switch (paramPart.getName()) {
                        case INVESTIGATION_SEARCH_PERIOD_PARAM_PART:
                            searchPeriod = (Period) paramPart.getValue();
                            break;
                    }
                }

            } // for Parameters

            HashSet<String> diagnosticReportIdsToAdd = new HashSet<>();
            if (searchPeriod != null) {
                for (Resource diagnosticReportResource : diagnosticReports.values()) {
                    // get the start date of the consultation from the encounter
                    DiagnosticReport diagnosticReport = (DiagnosticReport) diagnosticReportResource;
                    Date startDate = diagnosticReport.getIssued();
                    addIdIfInPeriod(searchPeriod, diagnosticReportIdsToAdd, diagnosticReport, startDate);
                }
            }

            // no filters - add everything
            if (searchPeriod == null) {
                for (Resource diagnosticReport : diagnosticReports.values()) {
                    diagnosticReportIdsToAdd.add(diagnosticReport.getId());
                }
            }

            for (String diagnosticReportId : diagnosticReportIdsToAdd) {
                refCounts.put(diagnosticReportId, 1);
                walkResource(structuredBundle, resourceTypes, diagnosticReportId, 1);
            }

        } // processParm

        @Override
        public String getListTitle() {
            return SNOMED_INVESTIGATIONS_LIST_DISPLAY;
        }

        @Override
        protected String getFhirResourceName() {
            return "DiagnosticReport";
        }

    } // Investigations

    /**
     * Referrals
     */
    public class ReferralsListManager extends AbstractListManager {

        /**
         * public constructor
         *
         * @param resourceTypes
         */
        public ReferralsListManager(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
            super(resourceTypes);
        }

        @Override
        /**
         * Referrals increment the ref counts for resources matching the search
         * criteria to the response
         *
         * @param resourceTypes
         * @param parms
         * @param structuredBundle
         * @throws FHIRException
         */
        public void processParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes,
                ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
            HashMap<String, Resource> referrals = resourceTypes.get(ResourceType.ReferralRequest);

            Period searchPeriod = null;
            for (Parameters.ParametersParameterComponent parm : parms) {

                for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                    switch (paramPart.getName()) {
                        case REFERRAL_SEARCH_PERIOD_PARAM_PART:
                            searchPeriod = (Period) paramPart.getValue();
                            break;
                    }
                }

            } // for Parameters

            HashSet<String> referralsIdsToAdd = new HashSet<>();
            if (searchPeriod != null) {
                for (Resource referralResource : referrals.values()) {
                    // get the start date of the consultation from the encounter
                    ReferralRequest referralRequest = (ReferralRequest) referralResource;
                    Date startDate = referralRequest.getAuthoredOn();
                    // not sure this would ever happen
                    addIdIfInPeriod(searchPeriod, referralsIdsToAdd, referralRequest, startDate);
                }
            }

            // no filters - add everything
            if (searchPeriod == null) {
                for (Resource referral : referrals.values()) {
                    referralsIdsToAdd.add(referral.getId());
                }
            }

            for (String referralId : referralsIdsToAdd) {
                refCounts.put(referralId, 1);
                walkResource(structuredBundle, resourceTypes, referralId, 1);
            }

        } // processParm

        @Override
        public String getListTitle() {
            return SNOMED_REFERRALS_LIST_DISPLAY;
        }

        @Override
        protected String getFhirResourceName() {
            return "ReferralRequest";
        }
    } // Referrals
// </editor-fold>
}
