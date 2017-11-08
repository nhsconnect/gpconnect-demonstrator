package uk.gov.hscic.appointments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointment.schedule.ScheduleSearch;
import uk.gov.hscic.model.appointment.ScheduleDetail;
import uk.gov.hscic.slots.PopulateSlotBundle;

@Component
public class ScheduleResourceProvider implements IResourceProvider {

    @Autowired
    private ScheduleSearch scheduleSearch;

    @Autowired
    public PopulateSlotBundle getScheduleOperation;

    @Override
    public Class<Schedule> getResourceType() {
        return Schedule.class;
    }

    @Read()
    public Schedule getScheduleById(@IdParam IdDt scheduleId) {
        ScheduleDetail scheduleDetail = scheduleSearch.findScheduleByID(scheduleId.getIdPartAsLong());

        if (scheduleDetail == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InternalErrorException("No schedule details found for ID: " + scheduleId.getIdPart()),
                    SystemCode.BAD_REQUEST, IssueType.NOTFOUND);
        }

        return scheduleDetailToScheduleResourceConverter(scheduleDetail);
    }

    public List<Schedule> getSchedulesForLocationId(String locationId, Date startDateTime, Date endDateTime) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        List<ScheduleDetail> scheduleDetails = scheduleSearch.findScheduleForLocationId(Long.valueOf(locationId),
                startDateTime, endDateTime);

        if (scheduleDetails != null && !scheduleDetails.isEmpty()) {
            for (ScheduleDetail scheduleDetail : scheduleDetails) {
                schedules.add(scheduleDetailToScheduleResourceConverter(scheduleDetail));
            }
        }

        return schedules;
    }

    private Schedule scheduleDetailToScheduleResourceConverter(ScheduleDetail scheduleDetail) {
        Schedule schedule = new Schedule();
        schedule.setId(String.valueOf(scheduleDetail.getId()));
        schedule.getMeta().setLastUpdated(scheduleDetail.getLastUpdated());
        schedule.getMeta().setVersionId(String.valueOf(scheduleDetail.getLastUpdated().getTime()));
        Extension extension = new Extension(SystemURL.SD_EXTENSION_GPC_PRACTITIONER,
                new Reference("Practitioner/" + scheduleDetail.getPractitionerId()));
        schedule.addExtension(extension);
        Identifier identifier = new Identifier();
        identifier.setSystem(SystemURL.ID_GPC_SCHEDULE_IDENTIFIER);
        identifier.setValue(scheduleDetail.getIdentifier());
        schedule.addIdentifier(identifier);
        Coding coding = new Coding().setSystem(SystemURL.HL7_VS_C80_PRACTICE_CODES)
                .setCode(scheduleDetail.getTypeCode()).setDisplay(scheduleDetail.getTypeDescription());
        CodeableConcept codableConcept = new CodeableConcept().addCoding(coding);
        codableConcept.setText(scheduleDetail.getTypeDescription());
        schedule.setServiceType(Collections.singletonList(codableConcept));
        schedule.setActor((List<Reference>) new Reference("Location/" + scheduleDetail.getLocationId()));
        Period period = new Period();
        period.setStart(scheduleDetail.getStartDateTime());
        period.setEnd(scheduleDetail.getEndDateTime());
        schedule.setPlanningHorizon(period);
        schedule.setComment(scheduleDetail.getComment());
        return schedule;
    }

    public List<ExtensionDt> getPractitionerReferences(Schedule schedule) {
        return schedule.getUndeclaredExtensionsByUrl(SystemURL.SD_EXTENSION_GPC_PRACTITIONER);
    }
}
