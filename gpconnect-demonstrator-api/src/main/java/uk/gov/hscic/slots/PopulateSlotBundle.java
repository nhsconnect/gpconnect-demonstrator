package uk.gov.hscic.slots;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Schedule;
import org.hl7.fhir.dstu3.model.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.hl7.fhir.dstu3.model.Organization;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointment.slot.SlotSearch;
import uk.gov.hscic.appointments.ScheduleResourceProvider;
import uk.gov.hscic.location.LocationResourceProvider;
import uk.gov.hscic.location.LocationSearch;
import uk.gov.hscic.model.appointment.SlotDetail;
import uk.gov.hscic.model.location.LocationDetails;
import uk.gov.hscic.model.organization.OrganizationDetails;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.organization.OrganizationSearch;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

/**
 * A Plain Provider. The $gpc.getschedule operation is not tied to a specific
 * resource.
 */
@Component
public class PopulateSlotBundle {

    @Autowired
    private LocationResourceProvider locationResourceProvider;

    @Autowired
    private PractitionerResourceProvider practitionerResourceProvider;

    @Autowired
    private SlotResourceProvider slotResourceProvider;

    @Autowired
    private ScheduleResourceProvider scheduleResourceProvider;

    @Autowired
    private OrganizationResourceProvider organizationResourceProvider;

    @Autowired
    private OrganizationSearch organizationSearch;

    @Autowired
    private LocationSearch locationSearch;

    @Autowired
    private SlotSearch slotSearch;

    @Value("${serverBaseUrl}")
    private String serverBaseUrl;

    @PostConstruct
    private void postConstruct() {
        if (!serverBaseUrl.endsWith("/")) {
            serverBaseUrl = serverBaseUrl + "/";
        }
    }

    /**
     *
     * @param bundle Bundle resource to populate
     * @param operationOutcome
     * @param planningHorizonStart Date
     * @param planningHorizonEnd Date
     * @param actorPractitioner boolean
     * @param actorLocation boolean
     * @param managingOrganisation
     * @param bookingOdsCode String
     * @param bookingOrgType String eg "urgent-care"
     */
    public void populateBundle(Bundle bundle, OperationOutcome operationOutcome, Date planningHorizonStart,
            Date planningHorizonEnd, boolean actorPractitioner, boolean actorLocation, boolean managingOrganisation, String bookingOdsCode, String bookingOrgType) {
        bundle.getMeta().addProfile(SystemURL.SD_GPC_SRCHSET_BUNDLE);

        // TODO remove hard coding pick up from providerRouting.json ?
        final String OUR_ODS_CODE = "A20047";

        // find all locations for this ODS practice code and construct Resources for them
        // #144 generalise to handle 1..n locations for a practice
        HashMap<String, BundleEntryComponent> locationEntries = new HashMap<>();
        for (LocationDetails aLocationDetail : locationSearch.findAllLocations()) {
            if (aLocationDetail.getOrgOdsCode().equals(OUR_ODS_CODE)) {
                Location aLocationResource = locationResourceProvider.getLocationById(new IdType(aLocationDetail.getId()));
                BundleEntryComponent locationEntry = new BundleEntryComponent();
                locationEntry.setResource(aLocationResource);
                // #202 use full urls
                // #215 full url removed completely
                //locationEntry.setFullUrl(serverBaseUrl + "Location/" + aLocationResource.getIdElement().getIdPart());
                locationEntries.put(aLocationResource.getIdElement().getIdPart(), locationEntry);
            }
        }

        // find the provider organization from the ods code
        List<OrganizationDetails> ourOrganizationsDetails = organizationSearch.findOrganizationDetailsByOrgODSCode(OUR_ODS_CODE);
        OrganizationDetails ourOrganizationDetails = null;
        if (!ourOrganizationsDetails.isEmpty()) {
            // at 1.2.2 these are added regardless of the status of the relevant parameter
            // Just picks the first organization. There should only be one.
            ourOrganizationDetails = ourOrganizationsDetails.get(0);
        } else {
            // do something here .. should never happen
        }

        // find the bookng organization from the ods code
        List<OrganizationDetails> bookingOrganizationsDetails = organizationSearch.findOrganizationDetailsByOrgODSCode(bookingOdsCode);
        OrganizationDetails bookingOrganizationDetails = null;
        if (!bookingOrganizationsDetails.isEmpty()) {
            // at 1.2.2 these are added regardless of the status of the relevant parameter
            // Just picks the first organization. There should only be one.
            bookingOrganizationDetails = bookingOrganizationsDetails.get(0);
        }

        HashSet<BundleEntryComponent> addedSchedule = new HashSet<>();
        HashSet<BundleEntryComponent> addedLocation = new HashSet<>();
        HashSet<String> addedPractitioner = new HashSet<>();
        HashSet<String> addedOrganization = new HashSet<>();
        // issue #165 don't add duplicate slots, hashSet contains slot id as String
        HashSet<String> addedSlot = new HashSet<>();

        // #144 process all locations
        for (String locationId : locationEntries.keySet()) {

            // process the schedules
            for (Schedule schedule : scheduleResourceProvider.getSchedulesForLocationId(locationId, planningHorizonStart, planningHorizonEnd)) {
                boolean slotsAdded = false;

                schedule.getMeta().addProfile(SystemURL.SD_GPC_SCHEDULE);

                BundleEntryComponent scheduleEntry = new BundleEntryComponent();
                scheduleEntry.setResource(schedule);
                // #202 use full urls
                // #215 full url removed completely
                //scheduleEntry.setFullUrl(serverBaseUrl + "Schedule/" + schedule.getIdElement().getIdPart());

                // This Set does not work as expected because Slot does not implement hashCode
                // so the second set call to getSlots returns different objects with the same slot id
                Set<Slot> slots = new HashSet<>();

                //  # 166 see https://nhsconnect.github.io/gpconnect/appointments_slotavailabilitymanagement.html
                // for the details of the logic implemeneted here
                // 
                if (bookingOrgType.isEmpty() && bookingOdsCode.isEmpty()) {
                    // OPTION 1 get slots Specfying  neither org type nor org code
                    slots.addAll(slotResourceProvider.getSlotsForScheduleIdNoOrganizationTypeOrODS(schedule.getIdElement().getIdPart(),
                            planningHorizonStart, planningHorizonEnd));
                } else if (!bookingOrgType.isEmpty() && bookingOdsCode.isEmpty()) {
                    // OPTION 2 organisation type only
                    for (Slot slot : slotResourceProvider.getSlotsForScheduleId(schedule.getIdElement().getIdPart(),
                            planningHorizonStart, planningHorizonEnd)) {
                        SlotDetail slotDetail = slotSearch.findSlotByID(Long.parseLong(slot.getId()));
                        if (slotDetail.getOrganizationIds().isEmpty()
                                && (slotDetail.getOrganizationTypes().isEmpty()
                                || slotDetail.getOrganizationTypes().contains(bookingOrgType))) {
                            slots.add(slot);
                        }
                    }
                } else if (!bookingOdsCode.isEmpty() && bookingOrgType.isEmpty()) {
                    // OPTION 3 org code only
                    for (Slot slot : slotResourceProvider.getSlotsForScheduleId(schedule.getIdElement().getIdPart(),
                            planningHorizonStart, planningHorizonEnd)) {
                        SlotDetail slotDetail = slotSearch.findSlotByID(Long.parseLong(slot.getId()));
                        if (slotDetail.getOrganizationTypes().isEmpty()
                                && (slotDetail.getOrganizationIds().isEmpty() || bookingOrganizationDetails != null && slotDetail.getOrganizationIds().contains(bookingOrganizationDetails.getId()))) {
                            slots.add(slot);
                        }
                    }
                } else if (!bookingOrgType.isEmpty() && !bookingOdsCode.isEmpty()) {
                    // OPTION 4 both org code and org type
                    for (Slot slot : slotResourceProvider.getSlotsForScheduleId(schedule.getIdElement().getIdPart(),
                            planningHorizonStart, planningHorizonEnd)) {
                        SlotDetail slotDetail = slotSearch.findSlotByID(Long.parseLong(slot.getId()));
                        if (((slotDetail.getOrganizationTypes().isEmpty() || slotDetail.getOrganizationTypes().contains(bookingOrgType)))
                                && (slotDetail.getOrganizationIds().isEmpty() || bookingOrganizationDetails != null && slotDetail.getOrganizationIds().contains(bookingOrganizationDetails.getId()))) {
                            slots.add(slot);
                        }
                    }
                }

                // added at 1.2.2 add the organisation but only if there are some slots available
                // #216 added at 1.2.3 only supply org if requested
                if (managingOrganisation && slots.size() > 0 && ourOrganizationDetails != null && !addedOrganization.contains(ourOrganizationDetails.getOrgCode())) {
                    addOrganisation(ourOrganizationDetails, bundle);
                    addedOrganization.add(ourOrganizationDetails.getOrgCode());
                }

                String freeBusyType = "FREE";

                // process all the slots to be returned
                for (Slot slot : slots) {
                    if (freeBusyType.equalsIgnoreCase(slot.getStatus().toString())) {
                        String slotId = slot.getIdElement().getIdPart();
                        if (!addedSlot.contains(slotId)) {
                            BundleEntryComponent slotEntry = new BundleEntryComponent();
                            slotEntry.setResource(slot);
                            // #202 use full urls
                            // #215 full url removed completely
//                          slotEntry.setFullUrl(serverBaseUrl + "Slot/" + slotId);
                            bundle.addEntry(slotEntry);
                            addedSlot.add(slotId);
                            slotsAdded = true;
                        }

                        if (!addedSchedule.contains(scheduleEntry)) {
                            // only add a schedule if there's a reference to it and only add it once
                            bundle.addEntry(scheduleEntry);
                            addedSchedule.add(scheduleEntry);
                        }

                        if (actorLocation == true) {
                            // only add a unique location once
                            if (!addedLocation.contains(locationEntries.get(locationId))) {
                                bundle.addEntry(locationEntries.get(locationId));
                                addedLocation.add(locationEntries.get(locationId));
                            }
                        }
                    } // if free/busy status matches
                } // for slots

                //  # 193 for each schedule only add a practitioner if there have been some slots added.
                if (slotsAdded) {
                    // practitioners for this schedule
                    List<Reference> practitionerActors = scheduleResourceProvider.getPractitionerReferences(schedule);

                    if (!practitionerActors.isEmpty()) {
                        for (Reference practitionerActor : practitionerActors) {
                            Practitioner practitioner = practitionerResourceProvider
                                    .getPractitionerById((IdType) practitionerActor.getReferenceElement());

                            if (practitioner == null) {
                                Coding errorCoding = new Coding().setSystem(SystemURL.VS_GPC_ERROR_WARNING_CODE)
                                        .setCode(SystemCode.REFERENCE_NOT_FOUND);
                                CodeableConcept errorCodableConcept = new CodeableConcept().addCoding(errorCoding);
                                errorCodableConcept.setText("Invalid Reference");
                                operationOutcome.addIssue().setSeverity(IssueSeverity.ERROR)
                                        .setCode(IssueType.NOTFOUND).setDetails(errorCodableConcept);
                                throw new ResourceNotFoundException("Practitioner Reference returning null");
                            }
                            if (actorPractitioner) {
                                if (!addedPractitioner.contains(practitioner.getIdElement().getIdPart())) {
                                    addPractitioner(practitioner, bundle);
                                    addedPractitioner.add(practitioner.getIdElement().getIdPart());
                                }
                            }
                        } // for practitioner
                    } // if non empty practitioner list
                } // if slots added
            } // for schedules
        } // for location
    } // populateBundle 

    /**
     * Add practitioner to Bundle
     *
     * @param practitioner
     * @param bundle
     */
    private void addPractitioner(Practitioner practitioner, Bundle bundle) {
        BundleEntryComponent practionerEntry = new BundleEntryComponent();
        practionerEntry.setResource(practitioner);
        // #202 use full urls
        // #215 full url removed completely
        //practionerEntry.setFullUrl(serverBaseUrl + "Practitioner/" + practitioner.getIdElement().getIdPart());
        bundle.addEntry(practionerEntry);
    }

    /**
     * Add OrganizationResource to Bundle
     *
     * @param organization OrganizationDetails object to add to bundle
     * @param bundle Bundle Resource to add organisation to
     */
    private void addOrganisation(OrganizationDetails organization, Bundle bundle) {
        BundleEntryComponent organizationEntry = new BundleEntryComponent();
        Long organizationId = organization.getId();
        OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(organizationId);
        Organization organizationResource = organizationResourceProvider
                .convertOrganizationDetailsToOrganization(organizationDetails);
        organizationEntry.setResource(organizationResource);
        // #202 use full urls
        // #215 full url removed completely
        //organizationEntry.setFullUrl(serverBaseUrl + "Organization/" + organization.getId());
        bundle.addEntry(organizationEntry);
    }
}
