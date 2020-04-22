package uk.gov.hscic.slots;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Slot;
import org.hl7.fhir.dstu3.model.Slot.SlotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
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
import java.util.TimeZone;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Extension;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.SystemVariable;
import uk.gov.hscic.appointment.slot.SlotSearch;
import uk.gov.hscic.model.appointment.SlotDetail;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwInvalidRequest400_BadRequestException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntityInvalid422_ParameterException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntity422_BadRequestException;

@Component
public class SlotResourceProvider implements IResourceProvider {

    private static final String DATE_OFFSET_REGEXP = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(.+)$";

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
    public Bundle getSlotByIds(
            @RequiredParam(name = "start") DateParam startDate,
            @RequiredParam(name = "end") DateParam endDate,
            @RequiredParam(name = "status") String status,
            @OptionalParam(name = "searchFilter") TokenAndListParam searchFilters,
            @IncludeParam(allow = {"Slot:schedule",
        "Schedule:actor:Practitioner",
        "Schedule:actor:Location",
        "Location:managingOrganization"
    }) Set<Include> theIncludes) {

        boolean foundSchedule = false;
        for (Include anInclude : theIncludes) {
            // getParamName returns any text between the first and second colons
            if (anInclude.getParamName().equals("schedule")) {
                foundSchedule = true;
                break;
            }
        }

        if (!foundSchedule) {
            // TODO check not invalid parameter?
            throwInvalidRequest400_BadRequestException("No include Slot:schedule parameter has been provided");
        }

        Bundle bundle = new Bundle();
        String bookingOdsCode = "";
        String bookingOrgType = "";

        if (!status.equals("free")) {
            throwUnprocessableEntityInvalid422_ParameterException("Status incorrect: Must be equal to free");
        }

        try {
            startDate.isEmpty();
            endDate.isEmpty();
        } catch (Exception e) {
            throwUnprocessableEntityInvalid422_ParameterException("Start Date and End Date must be populated with a correct date format");
        }

        if (startDate.getPrefix() != ParamPrefixEnum.GREATERTHAN_OR_EQUALS
                || endDate.getPrefix() != ParamPrefixEnum.LESSTHAN_OR_EQUALS) {
            throwUnprocessableEntityInvalid422_ParameterException("Invalid Prefix used");
        }

        validateStartDateParamAndEndDateParam(startDate, endDate);

        if (searchFilters != null) {
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
        }

        boolean actorPractitioner = false;
        boolean actorLocation = false;
        boolean managingOrganisation = false;
        for (Include include : theIncludes) {

            switch (include.getValue()) {
                case "Schedule:actor:Practitioner":
                    actorPractitioner = true;
                    break;
                case "Schedule:actor:Location":
                    actorLocation = true;
                    break;
                case "Location:managingOrganization":
                    managingOrganisation = true;
                    break;
            }
        }
        getScheduleOperation.populateBundle(bundle, new OperationOutcome(), startDate.getValue(),
                endDate.getValue(), actorPractitioner, actorLocation, managingOrganisation, bookingOdsCode, bookingOrgType);

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
        List<SlotDetail> slotDetails = slotSearch.findSlotsForScheduleIdAndOrganizationType(Long.valueOf(scheduleId), startDateTime,
                endDateTime, bookingOrgType);

        if (slotDetails != null && !slotDetails.isEmpty()) {
            for (SlotDetail slotDetail : slotDetails) {
                slots.add(slotDetailToSlotResourceConverter(slotDetail));
            }
        }

        return slots;
    }

    /**
     * returns any slots not having org type or org id assigned
     *
     * @param scheduleId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public List<Slot> getSlotsForScheduleIdNoOrganizationTypeOrODS(String scheduleId, Date startDateTime, Date endDateTime) {
        ArrayList<Slot> slots = new ArrayList<>();
        List<SlotDetail> slotDetails = slotSearch.findSlotsForScheduleIdNoOrganizationTypeOrODS(Long.valueOf(scheduleId), startDateTime,
                endDateTime);

        if (slotDetails != null && !slotDetails.isEmpty()) {
            for (SlotDetail slotDetail : slotDetails) {
                slots.add(slotDetailToSlotResourceConverter(slotDetail));
            }
        }
        return slots;
    }

    public List<Slot> getSlotsForScheduleId(String scheduleId, Date startDateTime, Date endDateTime) {
        ArrayList<Slot> slots = new ArrayList<>();
        List<SlotDetail> slotDetails = slotSearch.findSlotsForScheduleId(Long.valueOf(scheduleId), startDateTime,
                endDateTime);

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
        IdType id = new IdType(resourceId);

        slot.setId(id);
        slot.getMeta().setVersionId(versionId);
        slot.getMeta().addProfile(SystemURL.SD_GPC_SLOT);

        slot.setSchedule(new Reference("Schedule/" + slotDetail.getScheduleReference()));
        slot.setStart(slotDetail.getStartDateTime());
        slot.setEnd(slotDetail.getEndDateTime());
        // #218 Date time formats
        slot.getStartElement().setPrecision(TemporalPrecisionEnum.SECOND);
        slot.getEndElement().setPrecision(TemporalPrecisionEnum.SECOND);

        switch (slotDetail.getFreeBusyType().toLowerCase(Locale.UK)) {
            case "free":
                slot.setStatus(SlotStatus.FREE);
                break;
            default:
                slot.setStatus(SlotStatus.BUSY);
                break;
        }

        String deliveryChannelCode = slotDetail.getDeliveryChannelCode();
        ArrayList<Extension> al = new ArrayList<>();
        if (deliveryChannelCode != null && !deliveryChannelCode.trim().isEmpty()) {
            Extension deliveryChannelExtension = new Extension(SystemURL.SD_EXTENSION_GPC_DELIVERY_CHANNEL, new CodeType(deliveryChannelCode));
            al.add(deliveryChannelExtension);
        }
        slot.setExtension(al);
        
        // 1.2.7 add slot type description as service type
        slot.addServiceType(new CodeableConcept().setText(slotDetail.getTypeDisply()));

        return slot;
    }

    private void validateStartDateParamAndEndDateParam(DateParam startDate, DateParam endDate) {

        Pattern dateTimePattern = Pattern.compile(SystemVariable.DATE_TIME_REGEX);
        Pattern dateOnlyPattern = Pattern.compile(SystemVariable.DATE_REGEX);

        String startString = startDate.getValueAsString();
        String endString = endDate.getValueAsString();

        validateOffset("start", startDate);
        validateOffset("end", endDate);

        //If the time is included then match against the date/time regex
        if ((startDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(startString).matches())
                || (endDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(endString).matches())) {
            throwUnprocessableEntityInvalid422_ParameterException("Invalid date/time used");
        } //if only a date then match against the date regex
        else if ((startDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(startString).matches())
                || (endDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(endString).matches())) {
            throwUnprocessableEntityInvalid422_ParameterException("Invalid date used");
        }

//        InstantDt instantDt = startDate.getValueAsInstantDt();
//        InstantDt instantDt2 = endDate.getValueAsInstantDt();

        if (startDate.getValue() != null && endDate.getValue() != null) {
            Date start = startDate.getValue();
            Date end = endDate.getValue();

            if (start != null && end != null) {
                long period = ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
                if (period < 0l || period > 14l) {
                    throwUnprocessableEntityInvalid422_ParameterException("Invalid time period, was " + period + " days between (max is 14)");
                }
            } else {
                throwUnprocessableEntity422_BadRequestException("Invalid timePeriod one or both of start and end date are not valid dates");
            }
        } else {
            throwUnprocessableEntityInvalid422_ParameterException("Invalid timePeriod one or both of start and end date were missing");
        }
    }

    /**
     * #218 validating timezone offsets
     * @param type String descriptor
     * @param date DateParam 
     */
    private void validateOffset(String type, DateParam date) {
        String dateStr = date.getValueAsString();
        if (dateStr.matches(DATE_OFFSET_REGEXP)) {
            String offset = dateStr.replaceFirst(DATE_OFFSET_REGEXP, "$1");
            if (!offset.matches("^\\+0[01]:00$")) {
                throwUnprocessableEntityInvalid422_ParameterException("Invalid " + type + " date offset (" + offset + ")");
            }
            if (TimeZone.getDefault().inDaylightTime(date.getValue())) {
                if (!offset.equals("+01:00")) {
                    throwUnprocessableEntityInvalid422_ParameterException("Invalid " + type + " date offset for BST (" + offset + ")");
                }
            } else {
                if (!offset.equals("+00:00")) {
                    throwUnprocessableEntityInvalid422_ParameterException("Invalid " + type + " date offset for GMT (" + offset + ")");
                }
            }
        }
    }
}
