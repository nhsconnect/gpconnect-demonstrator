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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.DateTimeType;
import static org.hl7.fhir.dstu3.model.DeviceRequest.EVENT_DATE;
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
import org.hl7.fhir.dstu3.model.Narrative;
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
import static uk.gov.hscic.SystemURL.SD_CC_EXT_PROBLEM_SIGNIFICANCE;
import static uk.gov.hscic.medications.PopulateMedicationBundle.setClinicalSetting;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyListNote;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyReasonCode;

/**
 * Appends canned responses to a Bundle Note that the Clinical Item List
 * Resources do not have an id so where these are referenced in collections we
 * use the list title to identify them
 *
 * @author simonfarrow
 */
public class StructuredBuilder {

    private final HashMap<String, Integer> refCounts = new HashMap<>();
    private final HashSet<String> alreadyAdded = new HashSet<>();

    private static final String SNOMED_CONSULTATION_ENCOUNTER_TYPE = "325851000000107";

    private static final String[] SNOMED_CANNED_LIST_TITLES = {SNOMED_PROBLEMS_LIST_DISPLAY, // 1.3
        SNOMED_CONSULTATION_LIST_DISPLAY,// 1.3
        SNOMED_MEDICATION_LIST_DISPLAY /*
        SNOMED_REFERRALS_LIST_DISPLAY, // 1.4
        SNOMED_INVESTIGATIONS_LIST_DISPLAY*/}; // 1.4

    // add any new clinical areas supported as canned responses here
    private final static String[] STRUCTURED_CANNED_CLINICAL_AREAS = {INCLUDE_PROBLEMS_PARM,
        INCLUDE_CONSULTATIONS_PARM/*,
        INCLUDE_REFERRALS_PARM,
        INCLUDE_INVESTIGATIONS_PARM*/
// extension point add new clinical areas here
    };

    private static final int DEBUG_LEVEL = 2;

    /**
     * append the entries in the xml bundle file to the response bundle can
     * handle xml or json source
     *
     * @param filename fully qualified file path to canned response file
     * @param structuredBundle
     * @param patient
     * @param parms
     * @param patients
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
        if (Arrays.asList(STRUCTURED_CANNED_CLINICAL_AREAS).contains(parameterName)) {
            // this handles canned responses that need reprocessing ie problems and consultations at 1.3
            handleCannedClinicalArea(parsedBundle, duplicates, structuredBundle, parms, parameterName, patient);
        } else {
            // for other canned responses just transcribe as is ie uncat and imm for 1.3
            for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
                structuredBundle.addEntry(entry);
            }
            if (patient.getIdElement().getIdPartAsLong() == PATIENT_2) {
                structuredBundle.addEntry().setResource(createProblem(parameterName, patient, structuredBundle));
                ListResource problemList = createList(SystemConstants.SNOMED_PROBLEMS_LIST_CODE, SystemConstants.SNOMED_PROBLEMS_LIST_DISPLAY ,patient);
                problemList.addEntry(new ListEntryComponent().setItem(new Reference("Condition/"+parameterName.replaceFirst("^include(.*)$", "$1Problem"))));
                structuredBundle.addEntry().setResource(problemList);
            }
        }
    } // appendCannedResponse

    private Condition createProblem(String parameterName, Patient patient, Bundle structuredBundle) {
        // add a dummy problem for patient 2 but not for patient 3
        Condition condition = new Condition();
        condition.setId(parameterName.replaceFirst("^include(.*)$", "$1Problem"));
        condition.setMeta(new Meta().addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-ProblemHeader-Condition-1"));

        // add a reference to an observation
        condition.addExtension(new Extension("").setValue(new Reference("Observation/obs1")));

        condition.addIdentifier(new Identifier().
                setValue("D06C0517-4D1C-11E3-A2DD-010000000161").
                setSystem("https://fhir.nhs.uk/Id/cross-care-setting-identifier"));

        CodeableConcept cc = new CodeableConcept().addCoding(new Coding().
                setSystem("http://snomed.info/sct").
                setCode("231504006").
                setDisplay("Mixed anxiety and depressive disorder"));
        cc.setText("Anxiety with depression");
        condition.setCode(cc);

        condition.setClinicalStatus(ConditionClinicalStatus.ACTIVE);

        condition.addCategory(new CodeableConcept().
                addCoding(new Coding().
                        setSystem("http://hl7.org/fhir/condition-category").
                        setCode("problem-list-item").
                        setDisplay("Problem List Item")
                ));

        condition.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPartAsLong()));

        condition.setContext(new Reference("Encounter/Encounter1"));
        condition.setOnset(new DateTimeType(new Date()));
        condition.setAssertedDate(new Date());
        condition.setAsserter(new Reference("Practitioner/1"));
        return condition;
    }

    /**
     * 
     * @param code
     * @param display
     * @param patient
     * @return 
     */
    private ListResource createList(String code, String display, Patient patient) {
        ListResource problemList = new ListResource();

        problemList.setMeta(new Meta().addProfile("https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-List-1"));
        problemList.setStatus(ListResource.ListStatus.CURRENT);
        problemList.setMode(ListResource.ListMode.SNAPSHOT);
        problemList.setTitle(display);
        CodeableConcept cc = new CodeableConcept().addCoding(new Coding().
                setSystem("http://snomed.info/sct").
                setCode(code).
                setDisplay(display));
        problemList.setCode(cc);
        problemList.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPartAsLong()));
        problemList.setDate(new Date());

        cc = new CodeableConcept().addCoding(new Coding().
                setSystem("http://hl7.org/fhir/list-order").
                setCode("event-date"));
        problemList.setOrderedBy(cc);
        
        return problemList;
    }

    /**
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
            String id = entry.getResource().getId();
            // if the refs count is not null then include the resource.
            if (refCounts.keySet().contains(id)) {
                addEntryToBundleOnlyOnce(structuredBundle, id, entry);
            }
        }

        if (DEBUG_LEVEL > 1) {
            compareRefCountsAndAlreadyAdded();
        }

        switch (parameterName) {

            case INCLUDE_PROBLEMS_PARM:
                // add any related consultations/encounters
                ArrayList<String> conditionsIdsInResponse = new ArrayList<>();

                Iterator<String> iter = alreadyAdded.iterator();
                while (iter.hasNext()) {
                    String id = iter.next();
                    if (id.startsWith("Condition/")) {
                        conditionsIdsInResponse.add(id);
                    }
                }

                // NB This ignores encounters referenced by Observations
                for (String conditionId : conditionsIdsInResponse) {
                    Condition condition = (Condition) resourceTypes.get(ResourceType.Condition).get(conditionId);
                    Reference encounterReference = condition.getContext();
                    // does this problem/condition reference an encounter in the returning bundle?
                    // NB email from Matt only return Encounters, not Consultations nor a List of Consultations
                    Encounter encounter = (Encounter) resourceTypes.get(ResourceType.Encounter).get(encounterReference.getReference());
                    if (addEntryToBundleOnlyOnce(structuredBundle, encounter.getId(), new BundleEntryComponent().setResource(encounter))) {
                        refCounts.put(encounter.getId(), 1);
                    }
                }

                // TODO add List (Allergies)
                // TODO add List (Immunisations)
                // TODO add List (Uncategorised)
                break;

            default:
        } // switch Parameter Name

        // add any problems reagrdless of whether problems were requested
        if (resourceTypes.get(ResourceType.Condition) != null) {
            // for already added problems add any related problems/conditions
            resourceTypes.get(ResourceType.Condition).keySet().stream().sorted().forEachOrdered((conditionId) -> {
                Condition condition = (Condition) resourceTypes.get(ResourceType.Condition).get(conditionId);
                for (Extension extension : condition.getExtension()) {
                    if (extension.getValue() instanceof Reference) {
                        Reference reference = (Reference) extension.getValue();
                        // does this problem/condition reference something in the returning bundle?
                        if (refCounts.keySet().contains(reference.getReference())) {
                            if (addEntryToBundleOnlyOnce(structuredBundle, condition.getId(), new BundleEntryComponent().setResource(condition))) {
                                refCounts.put(condition.getId(), 1);

                                // #314 re add the problems list because there are some!
                                ListResource problemList = (ListResource) resourceTypes.get(ResourceType.List).get(SNOMED_PROBLEMS_LIST_DISPLAY);
                                if (problemList != null) {
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
            for (String medicationStatementId : resourceTypes.get(ResourceType.MedicationStatement).keySet()) {
                MedicationStatement medicationStatement = (MedicationStatement) resourceTypes.get(ResourceType.MedicationStatement).get(medicationStatementId);
                String medicationId = medicationStatement.getMedicationReference().getReference();
                String medicationRequestId = medicationStatement.getBasedOnFirstRep().getReference();
                if ((refCounts.keySet().contains(medicationId) || refCounts.keySet().contains(medicationRequestId))) {
                    if (addEntryToBundleOnlyOnce(structuredBundle, medicationStatementId, new BundleEntryComponent().setResource(medicationStatement))) {
                        refCounts.put(medicationStatementId, 1);
                    }
                }

                // #314
                if (parameterName.equals(INCLUDE_PROBLEMS_PARM)) {
                    Iterator<String> iter = alreadyAdded.iterator();
                    boolean medicationsStatmentsPresent = false;
                    while (iter.hasNext()) {
                        String s = iter.next();
                        if (s.startsWith("MedicationStatement/")) {
                            medicationsStatmentsPresent = true;
                            break;
                        }
                    }

                    if (medicationsStatmentsPresent) { // check to see if there are any medications statements that have been added 
                        long patientLogicalID = patient.getIdElement().getIdPartAsLong();
                        // add a medications list (only when problems are requested)
                        addEntryToBundleOnlyOnce(structuredBundle, SNOMED_MEDICATION_LIST_DISPLAY,
                                new BundleEntryComponent().setResource(
                                        createListEntry(SNOMED_MEDICATION_LIST_CODE, SNOMED_MEDICATION_LIST_DISPLAY, patient.getIdentifierFirstRep().getValue(), "" + patientLogicalID)));
                    }
                }
            }
        }

        HashMap<String, ListResource> listResources = new HashMap<>();
        rebuildBundledLists(listResources, structuredBundle, resourceTypes);

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

    /**
     * Compare alreadyAdded set and refCounts Hash keys
     */
    private void compareRefCountsAndAlreadyAdded() {
        System.out.println("alreadyAdded count = " + alreadyAdded.size());
        System.out.println("refCounts key count = " + refCounts.size());

        Iterator<String> iter = alreadyAdded.iterator();
        int matching = 0;
        while (iter.hasNext()) {
            String id = iter.next();
            if (!id.endsWith("are not supported by the provider system")) {
                if (!refCounts.keySet().contains(id)) {
                    System.out.println("Present in alreadyAdded but not refCounts: " + id);
                } else {
                    matching++;
                }
            } else {
                System.out.println("Excluding from alreadyAdded: " + id);
            }
        }
        System.out.println("Also present in refCounts " + matching);

        matching = 0;
        for (Object key : refCounts.keySet()) {
            String id = (String) key;
            if (!id.endsWith("are not supported by the provider system")) {
                if (!alreadyAdded.contains(id)) {
                    System.out.println("Present in refCounts but not alreadyAdded: " + id);
                } else {
                    matching++;
                }
            } else {
                System.out.println("Excluding from refCounts: " + id);
            }
        }
        System.out.println("Also present in alreadyAdded " + matching);
    }

    /**
     *
     * @param bundle
     * @param id
     * @param entry
     * @return boolean indicating whether entry was added
     */
    private boolean addEntryToBundleOnlyOnce(Bundle bundle, String id, BundleEntryComponent entry) {
        boolean retval = false;
        if (!alreadyAdded.contains(id)) {
            retval = true;
            bundle.addEntry(entry);
            alreadyAdded.add(id);
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
        listResource.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.VS_SNOMED, snomedCode, snomedDisplay)));

        listResource.setSubject(
                new Reference(new IdType("Patient", patientId)).setIdentifier(new Identifier().setValue(nhsNumber).setSystem(SystemURL.ID_NHS_NUMBER)));
        listResource.setDate(new Date());
        listResource.setOrderedBy(
                new CodeableConcept().addCoding(new Coding(SystemURL.CS_LIST_ORDER, "event-date", "Sorted by Event Date")));

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
            {"GREENTOWN GENERAL HOSPITAL", "Dr Legg's Surgery"} // Site
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
    private void rebuildBundledLists(HashMap<String, ListResource> listResources, Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        for (BundleEntryComponent entry : structuredBundle.getEntry()) {
            if (entry.getResource() instanceof ListResource) {
                ListResource listResource = (ListResource) entry.getResource();
                if (listResource.getTitle() != null) {
                    String listTitle = listResource.getTitle();
                    if (Arrays.asList(SNOMED_CANNED_LIST_TITLES).contains(listTitle)) {
                        listResource.getEntry().clear();

                        // This is a reverse lookup
                        String includeParameter = getParameterFromTitle(listTitle);
                        AbstractListManager listManager = instantiateListManager(includeParameter, resourceTypes);
                        if (listManager != null) {
                            listManager.populateListResoure(listResources, listResource);
                        }
                        break;
                    } // if matching id
                } // if non null listTitle
            } // if ListResource
        } // for entry
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
                        if (!Arrays.asList(SNOMED_CANNED_LIST_TITLES).contains(key)) {
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
        if (referenceStr.matches("^.*/.*$")) {
            ResourceType rt = ResourceType.valueOf(referenceStr.replaceFirst("^(.*)/.*$", "$1"));
            DomainResource resource = (DomainResource) resourceTypes.get(rt).get(referenceStr);

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
                                Logger.getLogger(StructuredBuilder.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(StructuredBuilder.class.getName()).log(Level.SEVERE, null, ex);
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
            return "";
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
            HashSet<String> conditionIdsToAdd = new HashSet<>();

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

                for (Resource resource : conditions.values()) {
                    Condition condition = (Condition) resource;
                    List<Extension> significances = condition.getExtensionsByUrl(SD_CC_EXT_PROBLEM_SIGNIFICANCE);
                    if ((filterSignificance == null || significances.isEmpty()
                            // #300 significance changed from CodeableConcept to Code
                            || filterSignificance.equalsIgnoreCase(significances.get(0).getValue().toString()))
                            && (filterStatus == null || condition.getClinicalStatus() == filterStatus)) {
                        conditionIdsToAdd.add(condition.getId());
                    }
                }
            } // for Parameters

            for (String conditionId : conditionIdsToAdd) {
                refCounts.put(conditionId, 1);
                walkResource(structuredBundle, resourceTypes, conditionId, 1);
            }

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
}
