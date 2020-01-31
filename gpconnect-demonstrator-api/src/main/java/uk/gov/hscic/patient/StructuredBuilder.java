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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.exceptions.FHIRException;
import static uk.gov.hscic.SystemConstants.*;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyListNote;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyReasonCode;

/**
 * Appends canned responses to a Bundle
 *
 * @author simonfarrow
 */
public class StructuredBuilder {

    private final HashMap<String, Integer> refCounts = new HashMap<>();
    private final HashSet<String> alreadyAdded = new HashSet<>();

    private static final String SNOMED_CONSULTATION_ENCOUNTER_TYPE = "325851000000107";

    private static final String[] SNOMED_CANNED_LIST_TITLES = {SNOMED_PROBLEMS_LIST_DISPLAY, // 1.3
        SNOMED_CONSULTATION_LIST_DISPLAY, // 1.3
        SNOMED_REFERRALS_LIST_DISPLAY, // 1.4
        SNOMED_INVESTIGATIONS_LIST_DISPLAY}; // 1.4

    // add any new clinical areas supported as canned responses here
    private final static String[] STRUCTURED_CANNED_CLINICAL_AREAS = {INCLUDE_PROBLEMS_PARM,
        INCLUDE_CONSULTATIONS_PARM,
        INCLUDE_REFERRALS_PARM,
        INCLUDE_INVESTIGATIONS_PARM
// extension point add new clinical areas here
    };

    private static final int DEBUG_LEVEL = 2;

    /**
     * append the entries in the xml bundle file to the response bundle can
     * handle xml or json source
     *
     * @param filename fully qualified file path to canned response file
     * @param structuredBundle
     * @param patientLogicalID
     * @param parms
     * @throws DataFormatException
     * @throws ConfigurationException
     * @throws org.hl7.fhir.exceptions.FHIRException
     * @throws java.io.IOException
     */
    public void appendCannedResponse(String filename, Bundle structuredBundle, long patientLogicalID, ArrayList<Parameters.ParametersParameterComponent> parms) throws DataFormatException, ConfigurationException, FHIRException, IOException {
        // read from a pre prepared file
        FhirContext ctx = FhirContext.forDstu3();
        String suffix = filename.replaceFirst("^.*\\.([^\\.]+)$", "$1");
        String s = new String(Files.readAllBytes(new File(filename).toPath()));

        s = editResponse(patientLogicalID, s);

        IParser parser = getParser(suffix, ctx);

        Bundle parsedBundle = (Bundle) parser.parseResource(s);
        HashMap<String, Integer> duplicates = new HashMap<>();
        String parameterName = parms.get(0).getName();

        if (Arrays.asList(STRUCTURED_CANNED_CLINICAL_AREAS).contains(parameterName)) {
            handleCannedClinicalArea(parsedBundle, duplicates, structuredBundle, parms, parameterName);
        } else {
            // for other than canned just transcribe as is
            // TODO Don't think this will ever get called
            for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
                structuredBundle.addEntry(entry);
            }
        }
    } // appendCannedResponse

    /**
     *
     * @param parsedBundle
     * @param duplicates
     * @param structuredBundle
     * @param parms
     * @param parameterName
     * @throws FHIRException
     */
    private void handleCannedClinicalArea(Bundle parsedBundle, HashMap<String, Integer> duplicates, Bundle structuredBundle, ArrayList<Parameters.ParametersParameterComponent> parms, String parameterName) throws FHIRException {
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
            if (refCounts.keySet().contains(id) && !alreadyAdded.contains(id)) {
                structuredBundle.addEntry(entry);
                alreadyAdded.add(id);
            }
        }

        // add any related problems/conditions if this is a request for consultations
        if (parameterName.equals(INCLUDE_CONSULTATIONS_PARM)) {
            resourceTypes.get(ResourceType.Condition).keySet().stream().sorted().forEachOrdered((conditionId) -> {
                Condition condition = (Condition) resourceTypes.get(ResourceType.Condition).get(conditionId);
                for (Extension extension : condition.getExtension()) {
                    if (extension.getValue() instanceof Reference) {
                        Reference reference = (Reference) extension.getValue();
                        // does this problem/condition reference something in the returning bundle?
                        if (refCounts.keySet().contains(reference.getReference()) && !alreadyAdded.contains(condition.getId())) {
                            structuredBundle.addEntry(new BundleEntryComponent().setResource(condition));
                            alreadyAdded.add(condition.getId());
                            refCounts.put(condition.getId(), 1);
                            break;
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
                if ((refCounts.keySet().contains(medicationId) || refCounts.keySet().contains(medicationRequestId)) && !alreadyAdded.contains(medicationStatementId)) {
                    structuredBundle.addEntry(new BundleEntryComponent().setResource(medicationStatement));
                    alreadyAdded.add(medicationStatementId);
                    refCounts.put(medicationStatementId, 1);
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
     * get the correct parser for the input format
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
     * modify the canned response to be consistent with demonstrator data
     */
    private String editResponse(long patientLogicalID, String s) {
        // change all the references of the form .*/.* to be consistent
        String[][] referenceSubstitutions = new String[][]{
            {"Patient", "" + patientLogicalID},
            {"Practitioner", "1"},
            {"Organization", "7"}
        };
        // nb refernce substitions
        for (String[] substitution : referenceSubstitutions) {
            s = s.replaceAll("\"" + substitution[0] + "/.*\"", "\"" + substitution[0] + "/" + substitution[1] + "\"");
        }

        // do other substitutions
        String[][] quotedSubstitutions = new String[][]{
            {"REARDON, John", "MEAKIN, Mike"},
            {"SMITH", "GILBERT"},
            {"GREENTOWN GENERAL HOSPITAL", "Dr Legg's Surgery"}
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
     * @param resourceTypes hash of parsed resource objects
     */
    private void addInTopLevelResourceLists(String parameterName, Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        for (String listTitle : SNOMED_CANNED_LIST_TITLES) {
            // #290 only have the list returned that is relevant to the query.
            switch (parameterName) {
                // 1.3
                case INCLUDE_PROBLEMS_PARM:
                    // only add problems if problems requested
                    if (listTitle.equals(SNOMED_PROBLEMS_LIST_DISPLAY)) {
                        structuredBundle.addEntry(new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listTitle)));
                    }
                    break;

                // 1.3
                case INCLUDE_CONSULTATIONS_PARM:
                    // only add consultations if consultations requested
                    if (listTitle.equals(SNOMED_CONSULTATION_LIST_DISPLAY)) {
                        structuredBundle.addEntry(new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listTitle)));
                    }
                    break;

                // 1.4
                case INCLUDE_REFERRALS_PARM:
                    // only add referrals if referrals requested
                    if (listTitle.equals(SNOMED_REFERRALS_LIST_DISPLAY)) {
                        structuredBundle.addEntry(new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listTitle)));
                    }
                    break;

                // 1.4
                case INCLUDE_INVESTIGATIONS_PARM:
                    // only add investigations if investigations requested
                    if (listTitle.equals(SNOMED_INVESTIGATIONS_LIST_DISPLAY)) {
                        structuredBundle.addEntry(new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listTitle)));
                    }
                    break;

                // extension point
            }
        } // resourceLists
    }

    /**
     * rebuild all the canned bundled lists
     * @param structuredBundle
     * @param resourceTypes
     * @return
     */
    private void rebuildBundledLists(HashMap<String, ListResource> listResources, Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes) {
        for (String listTitle : SNOMED_CANNED_LIST_TITLES) {
            for (BundleEntryComponent entry : structuredBundle.getEntry()) {
                if (entry.getResource() instanceof ListResource) {
                    ListResource listResource = (ListResource) entry.getResource();
                    if (listResource.getTitle() != null && listResource.getTitle().equals(listTitle)) {
                        listResource.getEntry().clear();
                        // the events are now sorted alpha which mimics sorted by event date
                        switch (listTitle) {
                            // 1.3
                            case SNOMED_PROBLEMS_LIST_DISPLAY:
                                listResources.put(listTitle, listResource);
                                refCounts.keySet().stream().filter((key) -> (key.startsWith("Condition/"))).sorted().forEachOrdered((String key) -> {
                                    listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(key)));
                                });
                                break;

                            // 1.3
                            case SNOMED_CONSULTATION_LIST_DISPLAY:
                                listResources.put(listTitle, listResource);
                                refCounts.keySet().stream().filter((key) -> (key.startsWith("List/"))).sorted().forEachOrdered((key) -> {
                                    ListResource listResourceConsultation = (ListResource) resourceTypes.get(ResourceType.List).get(key);
                                    if (listResourceConsultation.getCode().getCodingFirstRep().getCode().equals(SNOMED_CONSULTATION_ENCOUNTER_TYPE)) {
                                        listResource.addEntry(new ListResource.ListEntryComponent().setItem(listResourceConsultation.getEncounter()));
                                    }
                                });
                                break;

                            // 1.4
                            case SNOMED_REFERRALS_LIST_DISPLAY:
                                //referralsListResource = listResource;
                                listResources.put(listTitle, listResource);
                                refCounts.keySet().stream().filter((key) -> (key.startsWith("ReferralRequest/"))).sorted().forEachOrdered((String key) -> {
                                    listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(key)));
                                });
                                break;

                            // 1.4
                            case SNOMED_INVESTIGATIONS_LIST_DISPLAY:
                                //investigationsListResource = listResource;
                                listResources.put(listTitle, listResource);
                                refCounts.keySet().stream().filter((key) -> (key.startsWith("DiagnosticReport/"))).sorted().forEachOrdered((String key) -> {
                                    listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(key)));
                                });
                                break;
                            // extension point add new clinical area list types here
                        }
                        break;
                    } // if matching id
                } // if ListResource
            } // for bundle entry
        } // for list id
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
    private void walkRoot(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms) throws FHIRException {

        switch (parms.get(0).getName()) {
            // 1.3 Problems can have > 1 cardinality queries
            case INCLUDE_PROBLEMS_PARM:
                processProblemsParm(resourceTypes, parms, structuredBundle);
                break;
            // 1.3
            case INCLUDE_CONSULTATIONS_PARM:
                processConsultationsParm(resourceTypes, parms, structuredBundle);
                break;
            // 1.4
            case INCLUDE_REFERRALS_PARM:
                processReferralsParm(resourceTypes, parms, structuredBundle);
                break;
            // 1.4
            case INCLUDE_INVESTIGATIONS_PARM:
                processInvestigationsParm(resourceTypes, parms, structuredBundle);
                break;
            // extension point
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
     * Problems increment the ref counts for resources matching the search
     * criteria to the response
     * NB All the parameters have been validated by now so qwe don't need to do any further checks
     * @param resourceTypes
     * @param parms
     * @param structuredBundle
     * @throws FHIRException
     */
    private void processProblemsParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
        HashMap<String, Resource> conditions = resourceTypes.get(ResourceType.Condition);
        HashSet<String> conditionIdsToAdd = new HashSet<>();

        for (Parameters.ParametersParameterComponent parm : parms) {
            String filterSignificance = null;
            ConditionClinicalStatus filterStatus = null;

            for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                switch (paramPart.getName()) {
                    case FILTER_SIGNIFICANCE:
                        filterSignificance = ((CodeType) paramPart.getValue()).getValue();
                        break;
                    case FILTER_STATUS:
                        filterStatus = ConditionClinicalStatus.valueOf(((CodeType) paramPart.getValue()).getValue().toUpperCase());
                        break;
                }
            }

            for (Resource resource : conditions.values()) {
                Condition condition = (Condition) resource;
                List<Extension> significances = condition.getExtensionsByUrl("https://fhir.hl7.org.uk/STU3/StructureDefinition/Extension-CareConnect-ProblemSignificance-1");
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

    } // processProblemsParm

    /**
     * Consultations increment the ref counts for resources matching the search
     * criteria to the response
     *
     * @param resourceTypes
     * @param parms
     * @param structuredBundle
     * @throws FHIRException
     */
    private void processConsultationsParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
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
                    case CONSULTATION_SEARCH_PERIOD:
                        searchPeriod = (Period) paramPart.getValue();
                        break;
                    case NUMBER_OF_MOST_RECENT:
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

    } // processConsultationsParm

    /**
     * Referrals increment the ref counts for resources matching the search
     * criteria to the response
     *
     * @param resourceTypes
     * @param parms
     * @param structuredBundle
     * @throws FHIRException
     */
    private void processReferralsParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
        HashMap<String, Resource> referrals = resourceTypes.get(ResourceType.ReferralRequest);

        Period searchPeriod = null;
        for (Parameters.ParametersParameterComponent parm : parms) {

            for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                switch (paramPart.getName()) {
                    case REFERRAL_SEARCH_PERIOD:
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

    } // processReferralsParm

    /**
     * Investigations increment the ref counts for resources matching the search
     * criteria to the response
     *
     * @param resourceTypes
     * @param parms
     * @param structuredBundle
     * @throws FHIRException
     */
    private void processInvestigationsParm(HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms, Bundle structuredBundle) throws FHIRException {
        HashMap<String, Resource> diagnosticReports = resourceTypes.get(ResourceType.DiagnosticReport);

        Period searchPeriod = null;
        for (Parameters.ParametersParameterComponent parm : parms) {

            for (Parameters.ParametersParameterComponent paramPart : parm.getPart()) {
                switch (paramPart.getName()) {
                    case INVESTIGATION_SEARCH_PERIOD:
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

    } // processInvestigationsParm

    // extension point for process methods
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
    private void walkResource(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, String referenceStr, int indent) throws FHIRException {
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
}
