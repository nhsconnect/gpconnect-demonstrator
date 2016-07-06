package uk.gov.hscic.appointments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.ApplicationContext;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Schedule;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.util.Date;
import uk.gov.hscic.appointment.schedule.model.ScheduleDetail;
import uk.gov.hscic.appointment.schedule.search.ScheduleSearch;
import uk.gov.hscic.appointment.schedule.search.ScheduleSearchFactory;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;

public class ScheduleResourceProvider implements IResourceProvider {

    private static final String EXTENSION_GPCONNECT_PRACTITIONER_1_0 = "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-practitioner-1-0";

	ApplicationContext applicationContext;
    
    public ScheduleResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<Schedule> getResourceType() {
        return Schedule.class;
    }

    @Read()
    public Schedule getScheduleById(@IdParam IdDt scheduleId) {

        RepoSource sourceType = RepoSourceType.fromString(null);
        ScheduleSearch scheduleSearch = applicationContext.getBean(ScheduleSearchFactory.class).select(sourceType);
        ScheduleDetail scheduleDetail = scheduleSearch.findScheduleByID(scheduleId.getIdPartAsLong());

        if (scheduleDetail == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No schedule details found for ID: " + scheduleId.getIdPart());
            throw new InternalErrorException("No schedule details found for ID: " + scheduleId.getIdPart(), operationalOutcome);
        }
        
        return scheduleDetailToScheduleResourceConverter(scheduleDetail);
    }
    
    @Search
    public List<Schedule> getSchedulesForLocationId(@RequiredParam(name = "locationId") String locationId, @RequiredParam(name = "startDateTime") String startDateTime, @RequiredParam(name = "endDateTime") String endDateTime) {
        RepoSource sourceType = RepoSourceType.fromString(null);
        ScheduleSearch scheduleSearch = applicationContext.getBean(ScheduleSearchFactory.class).select(sourceType);
        ArrayList<Schedule> schedules = new ArrayList();

        List<ScheduleDetail> scheduleDetails = null;
        scheduleDetails = scheduleSearch.findScheduleForLocationId(Long.valueOf(locationId), new Date(startDateTime), new Date(endDateTime));
        if (scheduleDetails != null && scheduleDetails.size() > 0) {
            for(ScheduleDetail scheduleDetail : scheduleDetails){
                schedules.add(scheduleDetailToScheduleResourceConverter(scheduleDetail));
            }
        }
        
        return schedules;
    }
     
    public Schedule scheduleDetailToScheduleResourceConverter(ScheduleDetail scheduleDetail){
        
        Schedule schedule = new Schedule();
        schedule.setId(String.valueOf(scheduleDetail.getId()));
        
        schedule.addUndeclaredExtension(true, EXTENSION_GPCONNECT_PRACTITIONER_1_0, new ResourceReferenceDt("Practitioner/"+scheduleDetail.getPractitionerId()));
        
        schedule.setIdentifier(Collections.singletonList(new IdentifierDt("http://fhir.nhs.net/Id/gpconnect-schedule-identifier", scheduleDetail.getIdentifier())));
        
        CodingDt coding = new CodingDt().setSystem("http://hl7.org/fhir/ValueSet/c80-practice-codes").setCode(scheduleDetail.getTypeCode()).setDisplay(scheduleDetail.getTypeDescription());
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
    	return schedule.getUndeclaredExtensionsByUrl(EXTENSION_GPCONNECT_PRACTITIONER_1_0);
    }
}