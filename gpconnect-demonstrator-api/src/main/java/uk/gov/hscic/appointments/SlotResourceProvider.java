package uk.gov.hscic.appointments;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Slot;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.SlotStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.search.SlotSearch;

@Component
public class SlotResourceProvider  implements IResourceProvider {

    @Autowired
    private SlotSearch slotSearch;

    @Override
    public Class<Slot> getResourceType() {
        return Slot.class;
    }

    @Read()
    public Slot getSlotById(@IdParam IdDt slotId) {
        SlotDetail slotDetail = slotSearch.findSlotByID(slotId.getIdPartAsLong());

        if (slotDetail == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No slot details found for ID: " + slotId.getIdPart());
            throw new InternalErrorException("No slot details found for ID: " + slotId.getIdPart(), operationalOutcome);
        }

        return slotDetailToSlotResourceConverter(slotDetail);
    }

    public List<Slot> getSlotsForScheduleId(String scheduleId, String startDateTime, String endDateTime) {
        ArrayList<Slot> slots = new ArrayList<>();
        List<SlotDetail> slotDetails = slotSearch.findSlotsForScheduleId(Long.valueOf(scheduleId), new Date(startDateTime), new Date(endDateTime));

        if (slotDetails != null && slotDetails.size() > 0) {
            for (SlotDetail slotDetail : slotDetails) {
                slots.add(slotDetailToSlotResourceConverter(slotDetail));
            }
        }

        return slots;
    }

    public Slot slotDetailToSlotResourceConverter(SlotDetail slotDetail){
        Slot slot = new Slot();
        slot.setId(String.valueOf(slotDetail.getId()));

        if (slotDetail.getLastUpdated() == null) {
            slotDetail.setLastUpdated(new Date());
        }

        slot.getMeta().setLastUpdated(slotDetail.getLastUpdated());
        slot.getMeta().setVersionId(String.valueOf(slotDetail.getLastUpdated().getTime()));
        slot.setIdentifier(Collections.singletonList(new IdentifierDt("http://fhir.nhs.net/Id/gpconnect-schedule-identifier", String.valueOf(slotDetail.getId()))));
        CodingDt coding = new CodingDt().setSystem("http://hl7.org/fhir/ValueSet/c80-practice-codes").setCode(String.valueOf(slotDetail.getTypeCode())).setDisplay(slotDetail.getTypeDisply());
        CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding);
        codableConcept.setText(slotDetail.getTypeDisply());
        slot.setType(codableConcept);
        slot.setSchedule(new ResourceReferenceDt("Schedule/"+slotDetail.getScheduleReference()));
        slot.setStartWithMillisPrecision(slotDetail.getStartDateTime());
        slot.setEndWithMillisPrecision(slotDetail.getEndDateTime());

        switch (slotDetail.getFreeBusyType().toLowerCase()) {
            case "free":
                slot.setFreeBusyType(SlotStatusEnum.FREE);
                break;
            default:
                slot.setFreeBusyType(SlotStatusEnum.BUSY);
                break;
        }

        return slot;
    }
}