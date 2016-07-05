package uk.gov.hscic.appointments;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Slot;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.search.SlotSearch;
import uk.gov.hscic.appointment.slot.search.SlotSearchFactory;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;

public class SlotResourceProvider  implements IResourceProvider {

    ApplicationContext applicationContext;

    public SlotResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<Slot> getResourceType() {
        return Slot.class;
    }

    @Read()
    public Slot getSlotById(@IdParam IdDt slotId) {

        RepoSource sourceType = RepoSourceType.fromString(null);
        SlotSearch slotSearch = applicationContext.getBean(SlotSearchFactory.class).select(sourceType);
        SlotDetail slotDetail = slotSearch.findSlotByID(slotId.getIdPartAsLong());

        if (slotDetail == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No slot details found for ID: " + slotId.getIdPart());
            throw new InternalErrorException("No slot details found for ID: " + slotId.getIdPart(), operationalOutcome);
        }
        
        return slotDetailToSlotResourceConverter(slotDetail);
    }
    
    @Search
    public List<Slot> getSlotsForScheduleId(@RequiredParam(name = "scheduleId") String scheduleId, @RequiredParam(name = "startDateTime") String startDateTime, @RequiredParam(name = "endDateTime") String endDateTime) {
        RepoSource sourceType = RepoSourceType.fromString(null);
        SlotSearch slotSearch = applicationContext.getBean(SlotSearchFactory.class).select(sourceType);
        ArrayList<Slot> slots = new ArrayList();

        List<SlotDetail> slotDetails = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            slotDetails = slotSearch.findSlotsForScheduleId(Long.valueOf(scheduleId), format.parse(startDateTime), format.parse(endDateTime));
        } catch (Exception e){
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("The date format could not be parsed, please use the format yyyy-MM-dd HH:mm:ss");
            throw new InternalErrorException("The date format could not be parsed, please use the format yyyy-MM-dd HH:mm:ss");
        }
        if (slotDetails != null && slotDetails.size() > 0) {
            for(SlotDetail slotDetail : slotDetails){
                slots.add(slotDetailToSlotResourceConverter(slotDetail));
            }
        }
        
        return slots;
    }
    
    public Slot slotDetailToSlotResourceConverter(SlotDetail slotDetail){
        Slot slot = new Slot();
        slot.setId(String.valueOf(slotDetail.getId()));
        slot.setIdentifier(Collections.singletonList(new IdentifierDt("http://fhir.nhs.net/Id/gpconnect-schedule-identifier", String.valueOf(slotDetail.getId()))));
        CodingDt coding = new CodingDt().setSystem("http://hl7.org/fhir/ValueSet/c80-practice-codes").setCode(String.valueOf(slotDetail.getTypeCode())).setDisplay(slotDetail.getTypeDisply());
        CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding);
        codableConcept.setText(slotDetail.getTypeDisply());
        slot.setType(codableConcept);
        slot.setSchedule(new ResourceReferenceDt("Schedule/"+slotDetail.getScheduleReference()));
        slot.setStartWithMillisPrecision(slotDetail.getStartDateTime());
        slot.setEndWithMillisPrecision(slotDetail.getEndDateTime());
        return slot;
    }
}