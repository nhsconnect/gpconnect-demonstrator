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
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.exceptions.FHIRException;
import static uk.gov.hscic.SystemConstants.CONSULTATION_SEARCH_PERIOD;
import static uk.gov.hscic.SystemConstants.FILTER_SIGNIFICANCE;
import static uk.gov.hscic.SystemConstants.FILTER_STATUS;
import static uk.gov.hscic.SystemConstants.INCLUDE_CONSULTATIONS_PARM;
import static uk.gov.hscic.SystemConstants.INCLUDE_PROBLEMS_PARM;
import static uk.gov.hscic.SystemConstants.NUMBER_OF_MOST_RECENT;
import static uk.gov.hscic.SystemConstants.PROBLEMS_LIST;
import static uk.gov.hscic.SystemConstants.CONSULTATION_LIST;

/**
 * Appends canned responses to a Bundle
 *
 * @author simonfarrow
 */
public class StructuredBuilder {

    private final HashMap<String, Integer> refCounts = new HashMap<>();
    private final HashSet<String> alreadyAdded = new HashSet<>();

    private static final String SNOMED_CONSULTATION_ENCOUNTER_TYPE = "325851000000107";
    private static final boolean DEBUG = true;

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
        IParser parser = null;
        String s = new String(Files.readAllBytes(new File(filename).toPath()));
        // change all the references to be consistent
        String[][] substitutions = new String[][]{
            {"Patient", "" + patientLogicalID}};

        for (String[] substitution : substitutions) {
            s = s.replaceAll("\"" + substitution[0] + "/.*\"", "\"" + substitution[0] + "/" + substitution[1] + "\"");
        }
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

        Bundle parsedBundle = (Bundle) parser.parseResource(s);
        HashMap<String, Integer> duplicates = new HashMap<>();
        String parameterName = parms.get(0).getName();

        if (parameterName.equals(INCLUDE_PROBLEMS_PARM) || parameterName.equals(INCLUDE_CONSULTATIONS_PARM)) {
            // populate resource types
            HashMap<ResourceType, HashMap<String, Resource>> resourceTypes = new HashMap<>();
            for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
                ResourceType resourceType = entry.getResource().getResourceType();
                if (resourceTypes.get(resourceType) == null) {
                    resourceTypes.put(resourceType, new HashMap<>());
                }
                HashMap<String, Resource> hmResources = resourceTypes.get(resourceType);
                // resource id is only null for the top level Problems or Consultation ListResource so use the title for those
                String id = entry.getResource().getId() == null ? ((ListResource) entry.getResource()).getTitle() : entry.getResource().getId();
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

//              dumpResources(resourceTypes);
            walkRoot(structuredBundle, resourceTypes, parms);

            // add in the two top level ResourceLists
            for (String listTitle : new String[]{PROBLEMS_LIST, CONSULTATION_LIST}) {
                boolean added = false;
                for (BundleEntryComponent entry : structuredBundle.getEntry()) {
                    if (entry.getResource().getId() == null && entry.getResource() instanceof ListResource) {
                        ListResource listResource = (ListResource) entry.getResource();
                        if (listResource.getTitle().equals(listTitle)) {
                            added = true;
                            break;
                        }
                    }
                }

                if (parameterName.equals(INCLUDE_PROBLEMS_PARM) && listTitle.equals(PROBLEMS_LIST) && !added) {
                    // only add problems 
                    structuredBundle.addEntry(new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listTitle)));
                } else if (parameterName.equals(INCLUDE_CONSULTATIONS_PARM) && !added) {
                    // add both problems and consultations
                    structuredBundle.addEntry(new BundleEntryComponent().setResource(resourceTypes.get(ResourceType.List).get(listTitle)));
                }
            } // resourceLists

            // add in all referenced resources if not already present
            for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
                String id = entry.getResource().getId();
                // if the refs count is not null then include the resource.
                if (refCounts.keySet().contains(id) && !alreadyAdded.contains(id)) {
                    structuredBundle.addEntry(entry);
                    alreadyAdded.add(id);
                }
            }

            // rebuild the problemans and consultations lists
            for (String listTitle : new String[]{PROBLEMS_LIST, CONSULTATION_LIST}) {
                for (BundleEntryComponent entry : structuredBundle.getEntry()) {
                    if (entry.getResource().getId() == null && entry.getResource() instanceof ListResource) {
                        ListResource listResource = (ListResource) entry.getResource();
                        if (listResource.getTitle().equals(listTitle)) {
                            listResource.getEntry().clear();
                            // the events are now sorted alpha which mimics sorted by event date
                            switch (listTitle) {
                                case PROBLEMS_LIST:
                                    refCounts.keySet().stream().filter((key) -> (key.startsWith("Condition/"))).sorted().forEachOrdered((key) -> {
                                        listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(key)));
                                    });
                                    break;
                                case CONSULTATION_LIST:
                                    refCounts.keySet().stream().filter((key) -> (key.startsWith("List/"))).sorted().forEachOrdered((key) -> {
                                        ListResource listResource1 = (ListResource) resourceTypes.get(ResourceType.List).get(key);
                                        if (listResource1.getCode().getCodingFirstRep().getCode().equals(SNOMED_CONSULTATION_ENCOUNTER_TYPE)) {
                                            listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(key)));
                                        }
                                    });
                                    break;
                            }
                            break;
                        } // if matching title
                    } // if null id and ListResource
                } // for bundkle entry
            } // for list title

            if (DEBUG) {
                for (String key : duplicates.keySet()) {
                    System.err.println("Warning duplicate id " + key + ", count = " + duplicates.get(key));
                }
            }

        } else {
            // for other than problems and consultations just transcribe as is
            for (Bundle.BundleEntryComponent entry : parsedBundle.getEntry()) {
                structuredBundle.addEntry(entry);
            }
        }
    } // appendCannedResponse

    /**
     * enumerate the top level items for Problems or Consultation see
     * https://structured-1-4-0.netlify.com/accessrecord_structured_development_consultation_guidance.html
     * and
     * https://structured-1-4-0.netlify.com/accessrecord_structured_development_problems_guidance.html
     *
     * @param root Problems or Consultation
     * @param structuredBundle response bundle to be appended to
     * @param resourceTypes hashmap of resources keyed by type
     * @param parms List of Parameters for this clinical area
     */
    private void walkRoot(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, ArrayList<Parameters.ParametersParameterComponent> parms) throws FHIRException {

        switch (parms.get(0).getName()) {
            // Problems can have > 1 cardinality queries
            case INCLUDE_PROBLEMS_PARM:
                processProblemsParm(resourceTypes, parms, structuredBundle);
                break;
            case INCLUDE_CONSULTATIONS_PARM:
                processConsultationsParm(resourceTypes, parms, structuredBundle);
                break;
        }

        if (DEBUG) {
            // show all unreferenced resources
            for (ResourceType rtKey : resourceTypes.keySet()) {
                for (String key : resourceTypes.get(rtKey).keySet()) {
                    if (refCounts.get(key) == null) {
                        System.err.println("Warning Unreferenced key " + key);
                    }
                }
            }
        }
    } // walkRoot

    /**
     * Problems
     * increment the ref counts for resources matching the search criteria to the response
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
                // TODO problemSignificance is probably not correct its just a place holder
                List<Extension> significances = condition.getExtensionsByUrl("problemSignificance");
                if ((filterSignificance == null || significances.isEmpty() || filterSignificance.toUpperCase().equals(significances.get(0)))
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
     * Consultations
     * increment the ref counts for resources matching the search criteria to the response
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
            // There are 4 having this code but one is a top level with no id its not a consultation
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
                // get the strta date of the consultation from the encounter
                Encounter encounter = (Encounter) encounters.get(consultation.getEncounter().getReference());
                Date startDate = encounter.getPeriod().getStart();
                // not sure this would ever happen
                if (searchPeriod.getStart() == null && searchPeriod.getEnd() == null) {
                    encounterIdsToAdd.add(encounter.getId());
                } else if (searchPeriod.getStart() != null && searchPeriod.getEnd() == null && startDate.compareTo(searchPeriod.getStart()) >= 0) {
                    encounterIdsToAdd.add(encounter.getId());
                } else if (searchPeriod.getStart() == null && searchPeriod.getEnd() != null && startDate.compareTo(searchPeriod.getEnd()) <= 0) {
                    encounterIdsToAdd.add(encounter.getId());
                } else if (searchPeriod.getStart() != null && searchPeriod.getEnd() != null
                        && startDate.compareTo(searchPeriod.getStart()) >= 0 && startDate.compareTo(searchPeriod.getEnd()) <= 0) {
                    encounterIdsToAdd.add(encounter.getId());
                }
            }
        }

        // no filters - add everything
        if (numberofMostRecent < 0 && searchPeriod == null) {
            for (Resource encounter : encounters.values()) {
                encounterIdsToAdd.add(encounter.getId());
            }
        }

        ListResource consultationList = (ListResource) lists.get(CONSULTATION_LIST);

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
     * recursively descend resolving references as we go. The completer for the
     * recursion is resources not containing references
     *
     * @param resourceTypes hashmap of resources keyed by type
     * @param referenceStr
     * @param indent indent level to use for tree
     */
    private void walkResource(Bundle structuredBundle, HashMap<ResourceType, HashMap<String, Resource>> resourceTypes, String referenceStr, int indent) throws FHIRException {
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
        } else if (resource instanceof DomainResource) {
            DomainResource domainResource = (DomainResource) resource;
            for (Extension extension : domainResource.getExtension()) {
                if (extension.getValue() instanceof Reference) {
                    Reference reference = (Reference) extension.getValue();
                    countReferences(reference, indent, structuredBundle, resourceTypes);
                }
            }
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
