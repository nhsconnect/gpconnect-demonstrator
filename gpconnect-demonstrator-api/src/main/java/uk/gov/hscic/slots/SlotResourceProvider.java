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
import ca.uhn.fhir.model.primitive.InstantDt;
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
import org.hl7.fhir.dstu3.model.CodeType;
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
        startDate.getValueAsInstantDt().getValue();
        getScheduleOperation.populateBundle(bundle, new OperationOutcome(), startDate.getValueAsInstantDt().getValue(),
                endDate.getValueAsInstantDt().getValue(), actorPractitioner, actorLocation, managingOrganisation, bookingOdsCode, bookingOrgType);

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

        return slot;
    }

    private void validateStartDateParamAndEndDateParam(DateParam startDate, DateParam endDate) {

        Pattern dateTimePattern = Pattern.compile(SystemVariable.DATE_TIME_REGEX);
        Pattern dateOnlyPattern = Pattern.compile(SystemVariable.DATE_REGEX);

        //If the time is included then match against the date/time regex
        if ((startDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(startDate.getValueAsString()).matches())
                || (endDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(endDate.getValueAsString()).matches())) {
            throwUnprocessableEntityInvalid422_ParameterException("Invalid date/time used");
        } //if only a date then match against the date regex
        else if ((startDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(startDate.getValueAsString()).matches())
                || (endDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(endDate.getValueAsString()).matches())) {
            throwUnprocessableEntityInvalid422_ParameterException("Invalid date used");
        }

        InstantDt instantDt = startDate.getValueAsInstantDt();
        InstantDt instantDt2 = endDate.getValueAsInstantDt();

        if (instantDt != null && instantDt2 != null) {
            Date start = instantDt.getValue();
            Date end = instantDt2.getValue();

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

}
