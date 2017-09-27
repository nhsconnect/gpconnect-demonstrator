package uk.gov.hscic.appointments;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Slot;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SlotStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointment.slot.SlotSearch;
import uk.gov.hscic.model.appointment.SlotDetail;
import uk.gov.hscic.organization.GetScheduleOperation;

@Component
public class SlotResourceProvider implements IResourceProvider {

    @Autowired
    private SlotSearch slotSearch;

    @Autowired
    public GetScheduleOperation getScheduleOperation;

    @Override
    public Class<Slot> getResourceType() {
        return Slot.class;
    }

    @Read()
    public Slot getSlotById(@IdParam IdDt slotId) {
        SlotDetail slotDetail = slotSearch.findSlotByID(slotId.getIdPartAsLong());

        if (slotDetail == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                    .setDetails("No slot details found for ID: " + slotId.getIdPart());
            throw new InternalErrorException("No slot details found for ID: " + slotId.getIdPart(), operationalOutcome);
        }

        return slotDetailToSlotResourceConverter(slotDetail);
    }

    @Search
    public Bundle getSlotByIds(@RequiredParam(name = "start") DateRangeParam startDate,
            @RequiredParam(name = "end") DateRangeParam endDate, @RequiredParam(name = "fb-type") String fbType,
            @IncludeParam(allow = { "Slot:schedule", "Schedule:actor:Practitioner",
                    "Schedule:actor:Location" }) Set<Include> theIncludes) {

        Bundle bundle = new Bundle();
        Date startLowerDate = null;
        Date startUpperDate = null;
        boolean actorPractitioner = false;
        boolean actorLocation = false;

        validateStartDateParamAndEndDateParam(startDate, endDate);

        if (startDate != null) {
            if (startDate.getLowerBound() != null) {
                if (startDate.getLowerBound().getPrefix() == ParamPrefixEnum.GREATERTHAN) {
                    startLowerDate = startDate.getLowerBound().getValue();
                } else {
                    if (startDate.getLowerBound().getPrecision() == TemporalPrecisionEnum.DAY) {
                        startLowerDate = startDate.getLowerBound().getValue(); // Remove
                                                                               // a
                                                                               // day
                                                                               // to
                                                                               // make
                                                                               // time
                                                                               // inclusive
                                                                               // of
                                                                               // parameter
                                                                               // date
                    } else {
                        startLowerDate = new Date(startDate.getLowerBound().getValue().getTime() - 1000); // Remove
                                                                                                          // a
                                                                                                          // second
                                                                                                          // to
                                                                                                          // make
                                                                                                          // time
                                                                                                          // inclusive
                                                                                                          // of
                                                                                                          // parameter
                                                                                                          // date
                    }
                }
            }

            if (startDate.getUpperBound() != null) {
                if (startDate.getUpperBound().getPrefix() == ParamPrefixEnum.LESSTHAN) {
                    startUpperDate = startDate.getUpperBound().getValue();
                } else {
                    if (startDate.getUpperBound().getPrecision() == TemporalPrecisionEnum.DAY) {
                        startUpperDate = new Date(startDate.getUpperBound().getValue().getTime() + 86400000); // Add
                                                                                                              // a
                                                                                                              // day
                                                                                                              // to
                                                                                                              // make
                                                                                                              // time
                                                                                                              // inclusive
                                                                                                              // of
                                                                                                              // parameter
                                                                                                              // date
                    } else {
                        startUpperDate = new Date(startDate.getUpperBound().getValue().getTime() + 1000); // Add
                                                                                                          // a
                                                                                                          // second
                                                                                                          // to
                                                                                                          // make
                                                                                                          // time
                                                                                                          // inclusive
                                                                                                          // of
                                                                                                          // parameter
                                                                                                          // date
                    }
                }
            }
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

        getScheduleOperation.populateBundle(bundle, new OperationOutcome(), startLowerDate, startUpperDate,
                actorPractitioner, actorLocation);

        return bundle;

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

    public Slot slotDetailToSlotResourceConverter(SlotDetail slotDetail) {
        Slot slot = new Slot();
        slot.setId(String.valueOf(slotDetail.getId()));

        if (slotDetail.getLastUpdated() == null) {
            slotDetail.setLastUpdated(new Date());
        }

        slot.getMeta().setLastUpdated(slotDetail.getLastUpdated());
        slot.getMeta().setVersionId(String.valueOf(slotDetail.getLastUpdated().getTime()));
        slot.getMeta().addProfile(SystemURL.SD_GPC_SLOT);
        slot.setIdentifier(Collections.singletonList(new IdentifierDt(
                "http://fhir.nhs.net/Id/gpconnect-slot-identifier", String.valueOf(slotDetail.getId()))));
        CodingDt coding = new CodingDt().setSystem(SystemURL.HL7_VS_C80_PRACTICE_CODES)
                .setCode(String.valueOf(slotDetail.getTypeCode())).setDisplay(slotDetail.getTypeDisply());
        CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding);
        codableConcept.setText(slotDetail.getTypeDisply());
        slot.setType(codableConcept);
        slot.setSchedule(new ResourceReferenceDt("Schedule/" + slotDetail.getScheduleReference()));
        slot.setStartWithMillisPrecision(slotDetail.getStartDateTime());
        slot.setEndWithMillisPrecision(slotDetail.getEndDateTime());

        switch (slotDetail.getFreeBusyType().toLowerCase(Locale.UK)) {
        case "free":
            slot.setFreeBusyType(SlotStatusEnum.FREE);
            break;
        default:
            slot.setFreeBusyType(SlotStatusEnum.BUSY);
            break;
        }

        return slot;
    }

    private void validateStartDateParamAndEndDateParam(DateRangeParam startParam, DateRangeParam endParam) {

        if (startParam != null && endParam != null) {
            Date start = startParam.getLowerBoundAsInstant();
            Date end = endParam.getLowerBoundAsInstant();

            if (start != null && end != null) {
                long period = ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
                if (period < 0l || period > 14l) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException(
                                    "Invalid time period, was " + period + " days between (max is 14)"),
                            SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
                }
            } else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException(
                                "Invalid timePeriod one or both of start and end date are not valid dates"),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
            }
        } else {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(
                            "Invalid timePeriod one or both of start and end date were missing"),
                    SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }
    }
}
