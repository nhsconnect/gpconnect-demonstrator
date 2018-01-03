package uk.gov.hscic.slots;

import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Extension;
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

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointments.ScheduleResourceProvider;
import uk.gov.hscic.location.LocationResourceProvider;
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

    public void populateBundle(Bundle bundle, OperationOutcome operationOutcome, Date planningHorizonStart,
            Date planningHorizonEnd, boolean actorPractitioner, boolean actorLocation) {
        bundle.getMeta().addProfile(SystemURL.SD_GPC_GET_SCHEDULE_BUNDLE);

        List<Location> locations = locationResourceProvider.getAllLocationDetails();

        BundleEntryComponent locationEntry = new BundleEntryComponent();
        locationEntry.setResource(locations.get(0));
        locationEntry.setFullUrl("Location/" + locations.get(0).getId());

        // schedules
        List<Schedule> schedules = scheduleResourceProvider.getSchedulesForLocationId(
                locations.get(0).getIdElement().getIdPart(), planningHorizonStart, planningHorizonEnd);

        if (!schedules.isEmpty()) {
            for (Schedule schedule : schedules) {

                schedule.getMeta().addProfile(SystemURL.SD_GPC_SCHEDULE);

                BundleEntryComponent scheduleEntry = new BundleEntryComponent();
                scheduleEntry.setResource(schedule);
                scheduleEntry.setFullUrl("Schedule/" + schedule.getIdElement().getIdPart());

                // practitioners
                List<Extension> practitionerExtensions = scheduleResourceProvider.getPractitionerReferences(schedule);

                if (!practitionerExtensions.isEmpty()) {
                    for (Extension practionerExtension : practitionerExtensions) {
                        Reference practitionerRef = (Reference) practionerExtension.getValue();
                        Practitioner practitioner = practitionerResourceProvider
                                .getPractitionerById((IdType) practitionerRef.getReferenceElement());

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
                            BundleEntryComponent practionerEntry = new BundleEntryComponent();
                            practionerEntry.setResource(practitioner);
                            practionerEntry.setFullUrl("Practitioner/" + practitioner.getIdElement().getIdPart());
                            bundle.addEntry(practionerEntry);
                        }
                    }

                   
                    List<Slot> slots = slotResourceProvider.getSlotsForScheduleId(schedule.getIdElement().getIdPart(),
                            planningHorizonStart, planningHorizonEnd);
                    String freeBusyType = "FREE";
                    if (!slots.isEmpty()) {
                        for (Slot slot : slots) {
                            
                            if (freeBusyType.equalsIgnoreCase(slot.getStatus().toString())) {
                                BundleEntryComponent slotEntry = new BundleEntryComponent();
                                slotEntry.setResource(slot);
                                slotEntry.setFullUrl("Slot/" + slot.getIdElement().getIdPart());                    
                                bundle.addEntry(slotEntry);
                                bundle.addEntry(scheduleEntry);
                                
                                if (actorLocation == true){
                                bundle.addEntry(locationEntry);
                                }
                            
                            }
                        }
                    }
                }
            }
        }

    }
}
