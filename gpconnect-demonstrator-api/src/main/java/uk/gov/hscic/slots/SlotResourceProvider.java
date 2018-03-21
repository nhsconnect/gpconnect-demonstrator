package uk.gov.hscic.slots;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Slot;
import org.hl7.fhir.dstu3.model.Slot.SlotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.SystemVariable;
import uk.gov.hscic.appointment.slot.SlotSearch;
import uk.gov.hscic.model.appointment.SlotDetail;

@Component
public class SlotResourceProvider implements IResourceProvider {

    @Autowired
    private SlotSearch slotSearch;

    @Autowired
    public PopulateSlotBundle getScheduleOperation;

    @Override
    public Class<Slot> getResourceType() {
        return Slot.class;
    }

    @Read()
    public Slot getSlotById(@IdParam IdType slotId) {
        SlotDetail slotDetail = slotSearch.findSlotByID(slotId.getIdPartAsLong());
        if (slotDetail == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR)
                    .setDiagnostics("No slot details found for ID: " + slotId.getIdPart());
            throw new InternalErrorException("No slot details found for ID: " + slotId.getIdPart(), operationalOutcome);
        }

        return slotDetailToSlotResourceConverter(slotDetail);
    }

    @Search
    public Bundle getSlotByIds(@RequiredParam(name = "start") DateParam startDate,
            @RequiredParam(name = "end") DateParam endDate, @RequiredParam(name = "status") String status,
            @RequiredParam(name= "searchFilter") TokenAndListParam searchFilters,
            @IncludeParam(allow = { "Slot:schedule", "Schedule:actor:Practitioner",
                    "Schedule:actor:Location" }) Set<Include> theIncludes) {
    	
        Bundle bundle = new Bundle();
        boolean actorPractitioner = false;
        boolean actorLocation = false;
        String bookingOdsCode = "";
        String bookingOrgType = "";

        if (!status.equals("free")) {
        	throwInvalidParameterOperationalOutcome("Status incorrect: Must be equal to free");
        }

        try {
            startDate.isEmpty();
            endDate.isEmpty();
        } catch (Exception e) {
        	throwInvalidParameterOperationalOutcome("Start Date and End Date must be populated with a correct date format");
        }

        if (startDate.getPrefix() != ParamPrefixEnum.GREATERTHAN_OR_EQUALS
                || endDate.getPrefix() != ParamPrefixEnum.LESSTHAN_OR_EQUALS) {
        	throwInvalidParameterOperationalOutcome("Invalid Prefix used");

        }

        validateStartDateParamAndEndDateParam(startDate, endDate);
        
        List<TokenOrListParam> searchFilter = searchFilters.getValuesAsQueryTokens();
        for (TokenOrListParam filter : searchFilter) {
        	TokenParam token = filter.getValuesAsQueryTokens().get(0);
        	if (token.getSystem().equals(SystemURL.VS_GPC_ORG_TYPE)) {
        		bookingOrgType = token.getValue();
        	}
        	if (token.getSystem().equals(SystemURL.ID_ODS_ORGANIZATION_CODE)) {
        		bookingOdsCode = token.getValue();
        	}
        }
        
        try {
        	bookingOdsCode.isEmpty();
        	bookingOrgType.isEmpty();
        } catch (Exception e) {
            throwInvalidParameterOperationalOutcome("The ODS code and organisation type for the booking organisation must be supplied.");
        }

        for (Include include : theIncludes) {

            if (include.getValue().equals("Schedule:actor:Practitioner")) {
                actorPractitioner = true;
            }
            if (include.getValue().equals("Schedule:actor:Location")) {
                actorLocation = true;
            }
            ;
        }
        startDate.getValueAsInstantDt().getValue();
        getScheduleOperation.populateBundle(bundle, new OperationOutcome(), startDate.getValueAsInstantDt().getValue(), 
                endDate.getValueAsInstantDt().getValue(), actorPractitioner, actorLocation, bookingOdsCode, bookingOrgType);

        return bundle;

    }

    public List<Slot> getSlotsForScheduleIdAndOrganizationId(String scheduleId, Date startDateTime, Date endDateTime, Long orgId) {
        ArrayList<Slot> slots = new ArrayList<>();
        List<SlotDetail> slotDetails = slotSearch.findSlotsForScheduleIdAndOrganizationId(Long.valueOf(scheduleId), startDateTime,
                endDateTime, orgId);

        if (slotDetails != null && !slotDetails.isEmpty()) {
            for (SlotDetail slotDetail : slotDetails) {
                slots.add(slotDetailToSlotResourceConverter(slotDetail));
            }
        }

        return slots;
    }
    

	public List<Slot> getSlotsForScheduleIdAndOrganizationType(String scheduleId, Date startDateTime, Date endDateTime, String bookingOrgType) {
		ArrayList<Slot> slots = new ArrayList<>();
        List<SlotDetail> slotDetails = slotSearch.getSlotsForScheduleIdAndOrganizationType(Long.valueOf(scheduleId), startDateTime,
                endDateTime, bookingOrgType);

        if (slotDetails != null && !slotDetails.isEmpty()) {
            for (SlotDetail slotDetail : slotDetails) {
                slots.add(slotDetailToSlotResourceConverter(slotDetail));
            }
        }

        return slots;
	}

    private Slot slotDetailToSlotResourceConverter(SlotDetail slotDetail) {
        Slot slot = new Slot();
        
        Date lastUpdated = slotDetail.getLastUpdated() == null 
                ? new Date() 
                : slotDetail.getLastUpdated();
        
        String resourceId = String.valueOf(slotDetail.getId());
        String versionId = String.valueOf(lastUpdated.getTime());
        String resourceType = slot.getResourceType().toString();
        
        IdType id = new IdType(resourceType, resourceId, versionId);

        slot.setId(id);
        slot.getMeta().setVersionId(versionId);
        slot.getMeta().setLastUpdated(lastUpdated);        
        slot.getMeta().addProfile(SystemURL.SD_GPC_SLOT);

        slot.setIdentifier(Collections.singletonList(
                new Identifier().setSystem(SystemURL.ID_SDS_USER_ID).setValue(slotDetail.getId().toString())));

        Coding coding = new Coding().setSystem(SystemURL.HL7_VS_C80_PRACTICE_CODES)
                .setCode(String.valueOf(slotDetail.getTypeCode())).setDisplay(slotDetail.getTypeDisply());
        CodeableConcept codableConcept = new CodeableConcept().addCoding(coding);
        codableConcept.setText(slotDetail.getTypeDisply());

        List<CodeableConcept> serviceType = new ArrayList<CodeableConcept>();
        serviceType.add(codableConcept);
        slot.setServiceType(serviceType);

        slot.setSchedule(new Reference("Schedule/" + slotDetail.getScheduleReference()));
        slot.setStart(slotDetail.getStartDateTime());
        slot.setEnd(slotDetail.getEndDateTime());

        switch (slotDetail.getFreeBusyType().toLowerCase(Locale.UK)) {
        case "free":
            slot.setStatus(SlotStatus.FREE);
            break;
        default:
            slot.setStatus(SlotStatus.BUSY);
            break;
        }

        return slot;
    }

    private void validateStartDateParamAndEndDateParam(DateParam startDate, DateParam endDate) {

        Pattern dateTimePattern = Pattern.compile(SystemVariable.DATE_TIME_REGEX);
        Pattern dateOnlyPattern = Pattern.compile(SystemVariable.DATE_REGEX);
        
        //If the time is included then match against the date/time regex
        if ((startDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(startDate.getValueAsString()).matches())
        		|| (endDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(endDate.getValueAsString()).matches())) {
        	throwInvalidParameterOperationalOutcome("Invalid date/time used");
        }
        //if only a date then match against the date regex
        else if ( (startDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(startDate.getValueAsString()).matches())
        		|| (endDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(endDate.getValueAsString()).matches()) ) {
        	throwInvalidParameterOperationalOutcome("Invalid date used");
        }
        
        InstantDt instantDt = startDate.getValueAsInstantDt();
        InstantDt instantDt2 = endDate.getValueAsInstantDt();
        
        if (instantDt != null && instantDt2 != null) {
            Date start = instantDt.getValue();
            Date end = instantDt2.getValue();

            if (start != null && end != null) {
                long period = ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
                if (period < 0l || period > 14l) {
                	throwInvalidParameterOperationalOutcome("Invalid time period, was " + period + " days between (max is 14)");
                }
            } else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException(
                                "Invalid timePeriod one or both of start and end date are not valid dates"),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }
        } else {
            throwInvalidParameterOperationalOutcome("Invalid timePeriod one or both of start and end date were missing");
        }
    }

    private void throwInvalidParameterOperationalOutcome(String error) {
    	throw OperationOutcomeFactory.buildOperationOutcomeException(
    			new UnprocessableEntityException(
    					error),
    			SystemCode.INVALID_PARAMETER, IssueType.INVALID);
	}
}
