package uk.gov.hscic.appointments;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Schedule;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.model.appointment.ScheduleDetail;
import uk.gov.hscic.model.order.OrderDetail;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.slots.PopulateSlotBundle;
import uk.gov.hscic.appointment.schedule.ScheduleSearch;

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
                    SystemCode.BAD_REQUEST, IssueTypeEnum.NOT_FOUND);
        }

        return scheduleDetailToScheduleResourceConverter(scheduleDetail);
    }

    public List<Schedule> getSchedulesForLocationId(String locationId, Date startDateTime, Date endDateTime) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        List<ScheduleDetail> scheduleDetails = scheduleSearch.findScheduleForLocationId(Long.valueOf(locationId), startDateTime, endDateTime);

        if (scheduleDetails != null && !scheduleDetails.isEmpty()) {
            for (ScheduleDetail scheduleDetail : scheduleDetails) {
                schedules.add(scheduleDetailToScheduleResourceConverter(scheduleDetail));
            }
        }

        return schedules;
    }


    private Schedule scheduleDetailToScheduleResourceConverter(ScheduleDetail scheduleDetail){
        Schedule schedule = new Schedule();
        schedule.setId(String.valueOf(scheduleDetail.getId()));
        schedule.getMeta().setLastUpdated(scheduleDetail.getLastUpdated());
        schedule.getMeta().setVersionId(String.valueOf(scheduleDetail.getLastUpdated().getTime()));
        schedule.addUndeclaredExtension(false, SystemURL.SD_EXTENSION_GPC_PRACTITIONER, new ResourceReferenceDt("Practitioner/"+scheduleDetail.getPractitionerId()));
        schedule.setIdentifier(Collections.singletonList(new IdentifierDt(SystemURL.ID_GPC_SCHEDULE_IDENTIFIER, scheduleDetail.getIdentifier())));
        CodingDt coding = new CodingDt().setSystem(SystemURL.HL7_VS_C80_PRACTICE_CODES).setCode(scheduleDetail.getTypeCode()).setDisplay(scheduleDetail.getTypeDescription());
        CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding);
        codableConcept.setText(scheduleDetail.getTypeDescription());
        schedule.setType(Collections.singletonList(codableConcept));
        schedule.setActor(new ResourceReferenceDt("Location/"+scheduleDetail.getLocationId()));
        PeriodDt period = new PeriodDt();
        period.setStartWithSecondsPrecision(scheduleDetail.getStartDateTime());
        period.setEndWithSecondsPrecision(scheduleDetail.getEndDateTime());
        schedule.setPlanningHorizon(period);
        schedule.setComment(scheduleDetail.getComment());
        return schedule;
    }

    public List<ExtensionDt> getPractitionerReferences(Schedule schedule) {
        return schedule.getUndeclaredExtensionsByUrl(SystemURL.SD_EXTENSION_GPC_PRACTITIONER);
    }
}
