package uk.gov.hscic.appointments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public Schedule getScheduleById(@IdParam IdType scheduleId) {
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
        
        String resourceId = String.valueOf(scheduleDetail.getId());
        String versionId = String.valueOf(scheduleDetail.getLastUpdated().getTime());
        String resourceType = schedule.getResourceType().toString();
        
        IdType id = new IdType(resourceType, resourceId, versionId);

        schedule.setId(id);
        schedule.getMeta().setVersionId(versionId);
        schedule.getMeta().setLastUpdated(scheduleDetail.getLastUpdated());        
        
        if(scheduleDetail.getPractitionerId() != null) {
        	schedule.addActor(new Reference("Practitioner/" + scheduleDetail.getPractitionerId()));
        }
        
        if(scheduleDetail.getPractitionerRoleCode() != null) {
            
            Coding roleCoding = new Coding(SystemURL.VS_GPC_PRACTITIONER_ROLE, scheduleDetail.getPractitionerRoleCode(),
            		scheduleDetail.getPractitionerRoleDisplay());
            
            Extension practitionerRoleExtension = new Extension(SystemURL.SD_EXTENSION_GPC_PRACTITIONER_ROLE, 
            		new CodeableConcept().addCoding(roleCoding));
        	
        	schedule.addExtension(practitionerRoleExtension);
        }
        
        // # 194
//        Identifier identifier = new Identifier();
//        identifier.setSystem(SystemURL.ID_GPC_SCHEDULE_IDENTIFIER);
//        identifier.setValue(scheduleDetail.getIdentifier());
//        schedule.addIdentifier(identifier);
                
        Coding coding = new Coding().setSystem(SystemURL.HL7_VS_C80_PRACTICE_CODES)
                .setCode(scheduleDetail.getTypeCode()).setDisplay(scheduleDetail.getTypeDescription());
        CodeableConcept codableConcept = new CodeableConcept().addCoding(coding);
        codableConcept.setText(scheduleDetail.getTypeDescription());
        
        schedule.addActor(new Reference("Location/" + scheduleDetail.getLocationId()));    
        
        Period period = new Period();
        period.setStart(scheduleDetail.getStartDateTime());
        period.setEnd(scheduleDetail.getEndDateTime());
        schedule.setPlanningHorizon(period);
        
        // 1.2.7 remove comment
        //schedule.setComment(scheduleDetail.getComment());
        
        // 1.2.7 add schedule type description as service category
        schedule.setServiceCategory(new CodeableConcept().setText(scheduleDetail.getTypeDescription()));
        
        return schedule;
    }

    public List<Reference> getPractitionerReferences(Schedule schedule) {
    	return schedule.getActor().stream()
    			.filter(actor -> actor.getReference().startsWith("Practitioner"))
    			.collect(Collectors.toList());
    }
   
    public List<Extension> getPractitionerRoleReferences(Schedule schedule) {
        return schedule.getExtensionsByUrl(SystemURL.SD_EXTENSION_GPC_PRACTITIONER_ROLE);
    }
    
}
