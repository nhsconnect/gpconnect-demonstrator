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
import org.hl7.fhir.dstu3.model.Organization;
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

    /**
     *
     * @param bundle Bundle resource to populate
     * @param operationOutcome
     * @param planningHorizonStart Date
     * @param planningHorizonEnd Date
     * @param actorPractitioner boolean
     * @param actorLocation boolean
     * @param bookingOdsCode String
     * @param bookingOrgType String eg "Urgent Care"
     */
    public void populateBundle(Bundle bundle, OperationOutcome operationOutcome, Date planningHorizonStart,
            Date planningHorizonEnd, boolean actorPractitioner, boolean actorLocation, String bookingOdsCode, String bookingOrgType) {
        bundle.getMeta().addProfile(SystemURL.SD_GPC_SRCHSET_BUNDLE);

        String ourOdsCode = "A20047";

        // find first locationDetails for this ODS practice code
        List<LocationDetails> locationDetails = locationSearch.findAllLocations();
        LocationDetails locationDetail = null;
        for (LocationDetails alocationDetail : locationDetails) {
            if (alocationDetail.getOrgOdsCode().equals(ourOdsCode)) {
                locationDetail = alocationDetail;
                break;
            }
        }

        // find the matching location resource. There must be a better way
        Location location = null;
        List<Location> locations = locationResourceProvider.getAllLocationDetails();
        if (locationDetail != null) {
            for (Location aLocation : locations) {
                if (locationDetail.getSiteOdsCode().equals(aLocation.getIdentifierFirstRep().getValue())) {
                    location = aLocation;
                    break;
                }
            }
        }

        if (location == null) {
            // default if matching location not found
            location = locations.get(0);
        }

        BundleEntryComponent locationEntry = new BundleEntryComponent();
        locationEntry.setResource(location);
        locationEntry.setFullUrl("Location/" + location.getIdElement().getIdPart());

        // find the provider organization from the ods code
        List<OrganizationDetails> ourOrganizationsDetails = organizationSearch.findOrganizationDetailsByOrgODSCode(ourOdsCode);
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

        // schedules for given location
        List<Schedule> schedules = scheduleResourceProvider.getSchedulesForLocationId(
                location.getIdElement().getIdPart(), planningHorizonStart, planningHorizonEnd);

        if (!schedules.isEmpty()) {
            HashSet<BundleEntryComponent> addedSchedule = new HashSet<>();
            HashSet<BundleEntryComponent> addedLocation = new HashSet<>();
            HashSet<String> addedPractitioner = new HashSet<>();
            HashSet<String> addedOrganization = new HashSet<>();

            // process the schedules
            for (Schedule schedule : schedules) {

                schedule.getMeta().addProfile(SystemURL.SD_GPC_SCHEDULE);

                BundleEntryComponent scheduleEntry = new BundleEntryComponent();
                scheduleEntry.setResource(schedule);
                scheduleEntry.setFullUrl("Schedule/" + schedule.getIdElement().getIdPart());

                // practitioners
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
                        if (actorPractitioner == true) {
                            if (!addedPractitioner.contains(practitioner.getIdElement().getIdPart())) {
                                addPractitioner(practitioner, bundle);
                                addedPractitioner.add(practitioner.getIdElement().getIdPart());
                            }
                        }
                    } // for
                } // if

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
                                && (slotDetail.getOrganizationIds().isEmpty() || bookingOrganizationDetails != null &&  slotDetail.getOrganizationIds().contains(bookingOrganizationDetails.getId()))) {
                            slots.add(slot);
                        } 
                    }
                }

                // added at 1.2.2 add the organisation but only if there are some slots available
                if (slots.size() > 0 && ourOrganizationDetails != null && !addedOrganization.contains(ourOrganizationDetails.getOrgCode())) {
                    addOrganisation(ourOrganizationDetails, bundle);
                    addedOrganization.add(ourOrganizationDetails.getOrgCode());
                }

                String freeBusyType = "FREE";

                // issue #165 don't add duplicate slots, hashSet contains slot id as String
                HashSet<String> addedSlot = new HashSet<>();
                // process all the slots to be returned
                for (Slot slot : slots) {
                    if (freeBusyType.equalsIgnoreCase(slot.getStatus().toString())) {
                        String slotId = slot.getIdElement().getIdPart();
                        if (!addedSlot.contains(slotId)) {
                            BundleEntryComponent slotEntry = new BundleEntryComponent();
                            slotEntry.setResource(slot);
                            slotEntry.setFullUrl("Slot/" + slotId);
                            bundle.addEntry(slotEntry);
                            addedSlot.add(slotId);
                        }

                        if (!addedSchedule.contains(scheduleEntry)) {
                            // only add a schedule if there's a reference to it and only add it once
                            bundle.addEntry(scheduleEntry);
                            addedSchedule.add(scheduleEntry);
                        }

                        if (actorLocation == true) {
                            // only add a unique location once
                            if (!addedLocation.contains(locationEntry)) {
                                bundle.addEntry(locationEntry);
                                addedLocation.add(locationEntry);
                            }
                        }
                    } // if
                } // for slots
            } // for schedules
        } // if non empty schedules
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
        practionerEntry.setFullUrl("Practitioner/" + practitioner.getIdElement().getIdPart());
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
        organizationEntry.setFullUrl("Organization/" + organization.getId());
        bundle.addEntry(organizationEntry);
    }
}
