package uk.gov.hscic.appointments;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.*;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Appointment.AppointmentParticipantComponent;
import org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus;
import org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointment.appointment.AppointmentSearch;
import uk.gov.hscic.appointment.appointment.AppointmentStore;
import uk.gov.hscic.appointment.slot.SlotSearch;
import uk.gov.hscic.appointment.slot.SlotStore;
import uk.gov.hscic.model.appointment.AppointmentDetail;
import uk.gov.hscic.model.appointment.BookingOrgDetail;
import uk.gov.hscic.model.appointment.SlotDetail;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static uk.gov.hscic.InteractionId.REST_CANCEL_APPOINTMENT;
import static uk.gov.hscic.SystemURL.ID_ODS_ORGANIZATION_CODE;
import uk.gov.hscic.appointment.schedule.ScheduleSearch;
import static uk.gov.hscic.appointments.AppointmentValidation.APPOINTMENT_COMMENT_LENGTH;
import static uk.gov.hscic.appointments.AppointmentValidation.APPOINTMENT_DESCRIPTION_LENGTH;
import uk.gov.hscic.common.filters.FhirRequestGenericIntercepter;
import uk.gov.hscic.model.appointment.ScheduleDetail;
import uk.gov.hscic.model.practitioner.PractitionerDetails;
import uk.gov.hscic.practitioner.PractitionerSearch;

@Component
public class AppointmentResourceProvider implements IResourceProvider {

    @Autowired
    private AppointmentSearch appointmentSearch;

    @Autowired
    private AppointmentStore appointmentStore;

    @Autowired
    private SlotSearch slotSearch;

    @Autowired
    private SlotStore slotStore;

    @Autowired
    private AppointmentValidation appointmentValidation;

    @Autowired
    private PractitionerSearch practitionerSearch;

    @Autowired
    private ScheduleSearch scheduleSearch;

    private static final int HTTP422_UNPROCESSABLE_ENTITY = 422;

    @Override
    public Class<Appointment> getResourceType() {
        return Appointment.class;
    }

    @Read(version = true)
    public Appointment getAppointmentById(@IdParam IdType appointmentId) {
        Appointment appointment = null;

        try {
            Long id = appointmentId.getIdPartAsLong();

            AppointmentDetail appointmentDetail = null;

            // are we dealing with a request for a specific version of the
            // appointment
            if (appointmentId.hasVersionIdPart()) {

                try {
                    Long versionId = appointmentId.getVersionIdPartAsLong();

                    appointmentDetail = appointmentSearch.findAppointmentByIDAndLastUpdated(id, new Date(versionId));

                    if (appointmentDetail == null) {
                        // 404 version of resource not found
                        String msg = String.format("No appointment details found for ID: %s with versionId %s",
                                appointmentId.getIdPart(), versionId);
                        throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(msg),
                                SystemCode.REFERENCE_NOT_FOUND, IssueType.NOTFOUND);
                    }
                } catch (NumberFormatException nfe) {
                    // 404 resource not found - the versionId is valid according
                    // to FHIR
                    // however we have no entities matching that versionId
                    String msg = String.format("The version ID %s of the Appointment (ID - %s) is not valid",
                            appointmentId.getVersionIdPart(), id);
                    throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(msg),
                            SystemCode.REFERENCE_NOT_FOUND, IssueType.NOTFOUND);
                }
            } else {
                appointmentDetail = appointmentSearch.findAppointmentByID(id);

                if (appointmentDetail == null) {
                    // 404 resource not found
                    String msg = String.format("No appointment details found for ID: %s", appointmentId.getIdPart());
                    throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(msg),
                            SystemCode.REFERENCE_NOT_FOUND, IssueType.NOTFOUND);
                }
            }

            appointment = appointmentDetailToAppointmentResourceConverter(appointmentDetail);
        } catch (NumberFormatException nfe) {
            // 404 resource not found - the identifier is valid according to
            // FHIR
            // however we have no entities matching that identifier
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No appointment details found for ID: " + appointmentId.getIdPart()),
                    SystemCode.REFERENCE_NOT_FOUND, IssueType.NOTFOUND);
        }

        return appointment;
    }

    @Search
    public List<Appointment> getAppointmentsForPatientIdAndDates(@RequiredParam(name = "patient") IdType patientLocalId,
            @Sort SortSpec sort, @Count Integer count, @RequiredParam(name = "start") DateAndListParam startDates) {

        Date startLowerDate = null;
        Date startUpperDate = null;

        List<DateOrListParam> startDateList = startDates.getValuesAsQueryTokens();

        if (startDateList.size() != 2) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Appointment search must have both 'le' and 'ge' start date parameters."),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        }

        Pattern dateOnlyPattern = Pattern.compile("[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])))");

        boolean lePrefix = false;
        boolean gePrefix = false;

        for (DateOrListParam filter : startDateList) {
            DateParam token = filter.getValuesAsQueryTokens().get(0);

            if (!dateOnlyPattern.matcher(token.getValueAsString()).matches()) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException("Search dates must be of the format: yyyy-MM-dd and should not include time or timezone."),
                        SystemCode.INVALID_PARAMETER, IssueType.INVALID);
            }

            if (null != token.getPrefix()) {
                switch (token.getPrefix()) {
                    case GREATERTHAN_OR_EQUALS:
                        startLowerDate = token.getValue();
                        gePrefix = true;
                        break;
                    case LESSTHAN_OR_EQUALS:
                        Calendar upper = Calendar.getInstance();
                        upper.setTime(token.getValue());
                        upper.add(Calendar.HOUR, 23);
                        upper.add(Calendar.MINUTE, 59);
                        upper.add(Calendar.SECOND, 59);
                        startUpperDate = upper.getTime();
                        lePrefix = true;
                        break;
                    default:
                        throw OperationOutcomeFactory.buildOperationOutcomeException(
                                new UnprocessableEntityException("Unknown prefix on start date parameter: only le and ge prefixes are allowed."),
                                SystemCode.INVALID_PARAMETER, IssueType.INVALID);
                }
            }
        }

        if (!gePrefix || !lePrefix) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Parameters must contain two start parameters, one with prefix 'ge' and one with prefix 'le'."),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        } else if (startLowerDate.before(getYesterday())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Search dates from the past: " + startLowerDate.toString() + " are not allowed in appointment search."),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        } else if (startUpperDate.before(getYesterday())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Search dates from the past: " + startUpperDate.toString() + " are not allowed in appointment search."),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        } else if (startUpperDate.before(startLowerDate)) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Upper search date must be after the lower search date."),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        }

        List<Appointment> appointment = appointmentSearch
                .searchAppointments(patientLocalId.getIdPartAsLong(), startLowerDate, startUpperDate).stream()
                .map(this::appointmentDetailToAppointmentResourceConverter).collect(Collectors.toList());

        List<Appointment> futureAppointments = appointmentValidation.filterToReturnFutureAppointments(appointment);

        if (futureAppointments.isEmpty()) {

            return null;
        }
        // Update startIndex if we do paging
        return count != null ? futureAppointments.subList(0, count) : futureAppointments;
    }

    private Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }

    @Create
    public MethodOutcome createAppointment(@ResourceParam Appointment appointment) {
        if (appointment.getStatus() == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("No status supplied"), SystemCode.INVALID_RESOURCE,
                    IssueType.INVALID);
        }

        if (appointment.getStart() == null || appointment.getEnd() == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Both start and end date are required"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }

        if (appointment.getSlot().isEmpty()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("At least one slot is required"), SystemCode.INVALID_RESOURCE,
                    IssueType.INVALID);
        }

        if (appointment.getParticipant().isEmpty()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("At least one participant is required"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }

        if (!appointment.getIdentifierFirstRep().isEmpty() && appointment.getIdentifierFirstRep().getValue() == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Appointment identifier value is required"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }

        if (appointment.getId() != null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Appointment id shouldn't be provided!"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }
        if (!appointment.getReason().isEmpty()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Appointment reason shouldn't be provided!"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }

        // #157 refers to typeCode but means specialty  (name was changed)
        if (!appointment.getSpecialty().isEmpty()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Appointment speciality shouldn't be provided!"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }

        boolean hasRequiredResources = appointment.getParticipant().stream()
                .map(participant -> participant.getActor().getReference()).collect(Collectors.toList())
                .containsAll(Arrays.asList("Patient", "Location"));

        List<String> actors = new ArrayList<>();
        for (int i = 0; i < appointment.getParticipant().size(); i++) {
            actors.add(appointment.getParticipant().get(i).getActor().getReference());
        }
        boolean patientFound = false;
        boolean locationFound = false;

        for (int i = 0; i < appointment.getParticipant().size(); i++) {
            if (appointment.getParticipant().get(i).getActor().getReference().contains("Patient")) {
                patientFound = true;
            }
            if (appointment.getParticipant().get(i).getActor().getReference().contains("Location")) {
                locationFound = true;
            }

        }

        if (patientFound == false || locationFound == false) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(
                            "Appointment resource is not a valid resource required valid Patient and Location"),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }

        for (AppointmentParticipantComponent participant : appointment.getParticipant()) {

            Reference participantActor = participant.getActor();
            Boolean searchParticipant = (participantActor != null);

            if (searchParticipant) {
                appointmentValidation.validateParticipantActor(participantActor);
            }

            CodeableConcept participantType = participant.getTypeFirstRep();
            Boolean validParticipantType = appointmentValidation.validateParticipantType(participantType);

            if (!searchParticipant || !validParticipantType) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException(
                                "Supplied Participant is not valid. Must have an Actor or Type."),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }

            appointmentValidation.validateParticipantStatus(participant.getStatus(), participant.getStatusElement(),
                    participant.getStatusElement());
        }

        // Store New Appointment
        AppointmentDetail appointmentDetail = appointmentResourceConverterToAppointmentDetail(appointment);
        List<SlotDetail> slots = new ArrayList<>();

        // we'll get the delivery channel from the slot
        String deliveryChannel = null;
        ScheduleDetail schedule = null;
        for (Long slotId : appointmentDetail.getSlotIds()) {
            SlotDetail slotDetail = slotSearch.findSlotByID(slotId);

            if (slotDetail == null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException(
                                String.format("Slot resource reference value %s is not a valid resource.", slotId)),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            if (slotDetail.getFreeBusyType().equals("BUSY")) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new ResourceVersionConflictException(
                                String.format("Slot is already in use.", slotId)),
                        SystemCode.DUPLICATE_REJECTED, IssueType.CONFLICT);
            }

            if (deliveryChannel == null) {
                deliveryChannel = slotDetail.getDeliveryChannelCode();
            } else if (!deliveryChannel.equals(slotDetail.getDeliveryChannelCode())) {
                // added at 1.2.2
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException(
                                String.format("Subsequent slot (Slot/%s) delivery channel (%s) is not equal to initial slot delivery channel (%s).",
                                        slotId, deliveryChannel, slotDetail.getDeliveryChannelCode())),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            if (schedule == null) {
                // load the schedule so we can get the Practitioner ID
                schedule = scheduleSearch.findScheduleByID(slotDetail.getScheduleReference());

                // add practitioner id so we can get the practitioner role
                appointmentDetail.setPractitionerId(schedule.getPractitionerId());
            }
            slots.add(slotDetail);
        }

        // add deliveryChannel #157 removed
        //appointmentDetail.setDeliveryChannel(deliveryChannel);
        appointmentDetail = appointmentStore.saveAppointment(appointmentDetail, slots);

        for (SlotDetail slot : slots) {
            // slot.setAppointmentId(appointmentDetail.getId());
            slot.setFreeBusyType("BUSY");
            slot.setLastUpdated(new Date());
            slotStore.saveSlot(slot);
        }

        // Build response containing the new resource id
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdDt("Appointment", appointmentDetail.getId()));
        methodOutcome.setResource(appointmentDetailToAppointmentResourceConverter(appointmentDetail));
        methodOutcome.setCreated(Boolean.TRUE);

        return methodOutcome;
    } // createAppointment

    @Update
    public MethodOutcome updateAppointment(@IdParam IdType appointmentId, @ResourceParam Appointment appointment) {
        MethodOutcome methodOutcome = new MethodOutcome();
        OperationOutcome operationalOutcome = new OperationOutcome();
        AppointmentDetail appointmentDetail = appointmentResourceConverterToAppointmentDetail(appointment);

        // URL ID and Resource ID must be the same
        if (!Objects.equals(appointmentId.getIdPartAsLong(), appointmentDetail.getId())) {
            operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics("Id in URL ("
                    + appointmentId.getIdPart() + ") should match Id in Resource (" + appointmentDetail.getId() + ")");
            methodOutcome.setOperationOutcome(operationalOutcome);
            return methodOutcome;
        }

        // Make sure there is an existing appointment to be amended
        AppointmentDetail oldAppointmentDetail = appointmentSearch.findAppointmentByID(appointmentId.getIdPartAsLong());
        if (oldAppointmentDetail == null) {
            operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR)
                    .setDiagnostics("No appointment details found for ID: " + appointmentId.getIdPart());
            methodOutcome.setOperationOutcome(operationalOutcome);
            return methodOutcome;
        }

        String oldAppointmentVersionId = String.valueOf(oldAppointmentDetail.getLastUpdated().getTime());
        String newAppointmentVersionId = appointmentId.getVersionIdPart();
        if (newAppointmentVersionId != null && !newAppointmentVersionId.equalsIgnoreCase(oldAppointmentVersionId)) {
            throw new ResourceVersionConflictException("The specified version (" + newAppointmentVersionId
                    + ") did not match the current resource version (" + oldAppointmentVersionId + ")");
        }

        // Determine if it is a cancel or an amend. This was previously a check for the rpesence of a cancellation reason
        // but that is not sufficient.
        if (FhirRequestGenericIntercepter.getInteractionId().equals(REST_CANCEL_APPOINTMENT)) {
            // added at 1.2.2
            if (isInThePast(appointmentDetail.getStartDateTime())) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HTTP422_UNPROCESSABLE_ENTITY, "The cancellation start date cannot be in the past"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            if (appointmentDetail.getCancellationReason().isEmpty()) {
                operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR)
                        .setDiagnostics("The cancellation reason can not be blank");
                methodOutcome.setOperationOutcome(operationalOutcome);
                return methodOutcome;
            }

            // #172
            String appointmentType = appointment.getAppointmentType().getText();
            if (appointmentType != null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HTTP422_UNPROCESSABLE_ENTITY, "The appointment type cannot be updated on a cancellation"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            // This is a Cancellation - so copy across fields which can be
            // altered
            boolean cancelComparisonResult = compareAppointmentsForInvalidPropertyCancel(oldAppointmentDetail,
                    appointmentDetail);

            if (cancelComparisonResult) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(new UnclassifiedServerFailureException(HTTP422_UNPROCESSABLE_ENTITY,
                        "Invalid Appointment property has been amended (cancellation)"),
                        SystemCode.INVALID_RESOURCE, IssueType.FORBIDDEN);
            }

            oldAppointmentDetail.setCancellationReason(appointmentDetail.getCancellationReason());
            String oldStatus = oldAppointmentDetail.getStatus();
            appointmentDetail = oldAppointmentDetail;
            appointmentDetail.setStatus("cancelled");

            if (!"cancelled".equalsIgnoreCase(oldStatus)) {
                for (Long slotId : appointmentDetail.getSlotIds()) {
                    SlotDetail slotDetail = slotSearch.findSlotByID(slotId);
                    // slotDetail.setAppointmentId(null);
                    slotDetail.setFreeBusyType("FREE");
                    slotDetail.setLastUpdated(new Date());
                    slotStore.saveSlot(slotDetail);
                }
            }
        } else { // amend appointment
            if (appointment.getStatus().equals("cancelled")) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(400,
                                "Appointment has been cancelled and cannot be amended"),
                        SystemCode.BAD_REQUEST, IssueType.FORBIDDEN);
            }

            if (appointmentDetail.getCancellationReason() != null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HTTP422_UNPROCESSABLE_ENTITY, ""
                                + "Cannot amend cancellation reason in appointment amend"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            // added at 1.2.2
            if (isInThePast(appointmentDetail.getStartDateTime())) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HTTP422_UNPROCESSABLE_ENTITY, 
                                "The appointment amend start date cannot be in the past"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            boolean amendComparisonResult = compareAppointmentsForInvalidPropertyAmend(appointmentDetail,
                    oldAppointmentDetail);

            if (amendComparisonResult) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HTTP422_UNPROCESSABLE_ENTITY, "Invalid Appointment property has been amended"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }

            // This is an Amend
            oldAppointmentDetail.setComment(appointmentDetail.getComment());
            oldAppointmentDetail.setDescription(appointmentDetail.getDescription());
            appointmentDetail = oldAppointmentDetail;
        }

        List<SlotDetail> slots = new ArrayList<>();
        for (Long slotId : appointmentDetail.getSlotIds()) {
            SlotDetail slotDetail = slotSearch.findSlotByID(slotId);

            if (slotDetail == null) {
                throw new UnprocessableEntityException("Slot resource reference is not a valid resource");
            }

            slots.add(slotDetail);
        }

        appointmentDetail.setLastUpdated(new Date()); // Update version and
        // lastUpdated timestamp
        appointmentDetail = appointmentStore.saveAppointment(appointmentDetail, slots);

        methodOutcome.setId(new IdDt("Appointment", appointmentDetail.getId()));
        methodOutcome.setResource(appointmentDetailToAppointmentResourceConverter(appointmentDetail));

        return methodOutcome;
    } // updateAppointment

    private boolean compareAppointmentsForInvalidPropertyAmend(AppointmentDetail oldAppointmentDetail,
            AppointmentDetail appointmentDetail) {
        List<Boolean> results = new ArrayList<>();

        results.add(Objects.equals(oldAppointmentDetail.getId(), appointmentDetail.getId()));
        results.add(Objects.equals(oldAppointmentDetail.getStatus(), appointmentDetail.getStatus()));
        results.add(Objects.equals(oldAppointmentDetail.getStartDateTime(), appointmentDetail.getStartDateTime()));
        results.add(Objects.equals(oldAppointmentDetail.getEndDateTime(), appointmentDetail.getEndDateTime()));
        results.add(Objects.equals(oldAppointmentDetail.getPatientId(), appointmentDetail.getPatientId()));
        results.add(Objects.equals(oldAppointmentDetail.getPractitionerId(), appointmentDetail.getPractitionerId()));
        results.add(Objects.equals(oldAppointmentDetail.getLocationId(), appointmentDetail.getLocationId()));
        results.add(Objects.equals(oldAppointmentDetail.getMinutesDuration(), appointmentDetail.getMinutesDuration()));
        results.add(Objects.equals(oldAppointmentDetail.getPriority(), appointmentDetail.getPriority()));

        results.add(BookingOrganizationsEqual(oldAppointmentDetail.getBookingOrganization(),
                appointmentDetail.getBookingOrganization()));

        return results.contains(false);
    }

    private boolean compareAppointmentsForInvalidPropertyCancel(AppointmentDetail oldAppointmentDetail,
            AppointmentDetail appointmentDetail) {
        List<Boolean> results = new ArrayList<>();

        results.add(Objects.equals(oldAppointmentDetail.getId(), appointmentDetail.getId()));
        results.add(Objects.equals(oldAppointmentDetail.getPatientId(), appointmentDetail.getPatientId()));
        results.add(Objects.equals(oldAppointmentDetail.getPractitionerId(), appointmentDetail.getPractitionerId()));
        results.add(Objects.equals(oldAppointmentDetail.getLocationId(), appointmentDetail.getLocationId()));
        results.add(Objects.equals(oldAppointmentDetail.getMinutesDuration(), appointmentDetail.getMinutesDuration()));
        results.add(Objects.equals(oldAppointmentDetail.getPriority(), appointmentDetail.getPriority()));
        results.add(Objects.equals(oldAppointmentDetail.getComment(), appointmentDetail.getComment()));
        results.add(Objects.equals(oldAppointmentDetail.getDescription(), appointmentDetail.getDescription()));
        results.add(BookingOrganizationsEqual(oldAppointmentDetail.getBookingOrganization(),
                appointmentDetail.getBookingOrganization()));

        return results.contains(false);
    }

    private Boolean BookingOrganizationsEqual(BookingOrgDetail bookingOrg1, BookingOrgDetail bookingOrg2) {
        if (bookingOrg1 != null && bookingOrg2 != null) {
            Boolean equalNames = bookingOrg1.getName().equals(bookingOrg2.getName());
            Boolean equalNumbers = bookingOrg1.getTelephone().equals(bookingOrg2.getTelephone());
            return equalNames && equalNumbers;
        } else // One of the booking orgs is null so both should be null
        {
            if (bookingOrg1 == bookingOrg2) {
                return true;
            } else {
                return false;
            }
        }
    }

    private Appointment appointmentDetailToAppointmentResourceConverter(AppointmentDetail appointmentDetail) {
        Appointment appointment = new Appointment();

        String appointmentId = String.valueOf(appointmentDetail.getId());
        String versionId = String.valueOf(appointmentDetail.getLastUpdated().getTime());
        String resourceType = appointment.getResourceType().toString();

        IdType id = new IdType(resourceType, appointmentId, versionId);

        appointment.setId(id);
        appointment.getMeta().setVersionId(versionId);
        appointment.getMeta().setLastUpdated(appointmentDetail.getLastUpdated());
        appointment.getMeta().addProfile(SystemURL.SD_GPC_APPOINTMENT);

        Extension extension = new Extension(SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON,
                new StringType(appointmentDetail.getCancellationReason()));
        appointment.addExtension(extension);

        Identifier identifier = new Identifier();
        identifier.setSystem(SystemURL.ID_GPC_APPOINTMENT_IDENTIFIER)
                .setValue(String.valueOf(appointmentDetail.getId()));

        appointment.addIdentifier(identifier);

        // #157 derive delivery channel from slot
        List<Long> sids = appointmentDetail.getSlotIds();
        if (sids.size() > 0) {
            // get the first slot but it should not matter because for multi slot appts the deliveryChannel is always the same
            SlotDetail slotDetail = slotSearch.findSlotByID(sids.get(0));
            String deliveryChannel = slotDetail.getDeliveryChannelCode();
            if (deliveryChannel != null && !deliveryChannel.trim().isEmpty()) {
                Extension deliveryChannelExtension = new Extension(SystemURL.SD_EXTENSION_GPC_DELIVERY_CHANNEL, new CodeType(deliveryChannel));
                appointment.addExtension(deliveryChannelExtension);
            }
        }

        // practitioner role extension here
        // lookup the practitioner
        Long practitionerID = appointmentDetail.getPractitionerId();
        if (practitionerID != null) {
            PractitionerDetails practitionerDetails = practitionerSearch.findPractitionerDetails(Long.toString(practitionerID));
            if (practitionerDetails != null) {
                Coding roleCoding = new Coding(SystemURL.VS_GPC_PRACTITIONER_ROLE, practitionerDetails.getRoleCode(),
                        practitionerDetails.getRoleDisplay());

                Extension practitionerRoleExtension = new Extension(SystemURL.SD_EXTENSION_GPC_PRACTITIONER_ROLE,
                        new CodeableConcept().addCoding(roleCoding));
                appointment.addExtension(practitionerRoleExtension);
            }
        }

        switch (appointmentDetail.getStatus().toLowerCase(Locale.UK)) {
            case "pending":
                appointment.setStatus(AppointmentStatus.PENDING);
                break;
            case "booked":
                appointment.setStatus(AppointmentStatus.BOOKED);
                break;
            case "arrived":
                appointment.setStatus(AppointmentStatus.ARRIVED);
                break;
            case "fulfilled":
                appointment.setStatus(AppointmentStatus.FULFILLED);
                break;
            case "cancelled":
                appointment.setStatus(AppointmentStatus.CANCELLED);
                break;
            case "noshow":
                appointment.setStatus(AppointmentStatus.NOSHOW);
                break;
            default:
                appointment.setStatus(AppointmentStatus.PENDING);
                break;
        }

        appointment.setStart(appointmentDetail.getStartDateTime());
        appointment.setEnd(appointmentDetail.getEndDateTime());

        List<Reference> slotResources = new ArrayList<>();

        for (Long slotId : appointmentDetail.getSlotIds()) {
            slotResources.add(new Reference("Slot/" + slotId));
        }

        appointment.setSlot(slotResources);

        if (appointmentDetail.getPriority() != null) {
            appointment.setPriority(appointmentDetail.getPriority());
        }
        appointment.setComment(appointmentDetail.getComment());
        appointment.setDescription(appointmentDetail.getDescription());

        appointment.addParticipant().setActor(new Reference("Patient/" + appointmentDetail.getPatientId()))
                .setStatus(ParticipationStatus.ACCEPTED);

        appointment.addParticipant().setActor(new Reference("Location/" + appointmentDetail.getLocationId()))
                .setStatus(ParticipationStatus.ACCEPTED);

        if (null != appointmentDetail.getPractitionerId()) {
            appointment.addParticipant()
                    .setActor(new Reference("Practitioner/" + appointmentDetail.getPractitionerId()))
                    .setStatus(ParticipationStatus.ACCEPTED);
        }

        if (null != appointmentDetail.getCreated()) {
            appointment.setCreated(appointmentDetail.getCreated());
        }

        if (null != appointmentDetail.getBookingOrganization()) {

            String reference = "#1";
            Reference orgRef = new Reference(reference);
            Extension bookingOrgExt = new Extension(SystemURL.SD_CC_APPOINTMENT_BOOKINGORG, orgRef);

            appointment.addExtension(bookingOrgExt);
            BookingOrgDetail bookingOrgDetail = appointmentDetail.getBookingOrganization();

            Organization bookingOrg = new Organization();
            bookingOrg.setId(reference);
            bookingOrg.getNameElement().setValue(bookingOrgDetail.getName());
            bookingOrg.getTelecomFirstRep().setValue(bookingOrgDetail.getTelephone()).setUse(ContactPointUse.TEMP)
                    .setSystem(ContactPointSystem.PHONE);
            bookingOrg.getMeta().addProfile(SystemURL.SD_GPC_ORGANIZATION);

            if (null != bookingOrgDetail.getOrgCode()) {
                bookingOrg.getIdentifierFirstRep().setSystem(SystemURL.ID_ODS_ORGANIZATION_CODE)
                        .setValue(bookingOrgDetail.getOrgCode());
            }

            appointment.getContained().add(bookingOrg);
        }

        appointment.setMinutesDuration(appointmentDetail.getMinutesDuration());

        return appointment;
    } // appointmentDetailToAppointmentResourceConverter

    private Date getLastUpdated(Date lastUpdated) {
        if (lastUpdated == null) {
            lastUpdated = new Date();
        }

        // trim off milliseconds as we do not store
        // to this level of granularity
        Instant instant = lastUpdated.toInstant();
        instant = instant.truncatedTo(ChronoUnit.SECONDS);

        lastUpdated = Date.from(instant);

        return lastUpdated;
    }

    private AppointmentDetail appointmentResourceConverterToAppointmentDetail(Appointment appointment) {

        appointmentValidation.validateAppointmentExtensions(appointment.getExtension());

        if (appointmentValidation.appointmentDescriptionTooLong(appointment)) {
            throw new UnprocessableEntityException("Appointment description cannot be greater then " + APPOINTMENT_DESCRIPTION_LENGTH + " characters");
        }

        AppointmentDetail appointmentDetail = new AppointmentDetail();

        Long id = appointment.getIdElement().getIdPartAsLong();
        appointmentDetail.setId(id);

        appointmentDetail.setLastUpdated(getLastUpdated(appointment.getMeta().getLastUpdated()));

        List<Extension> cnlExtension = appointment
                .getExtensionsByUrl(SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON);

        if (cnlExtension != null && !cnlExtension.isEmpty()) {
            IBaseDatatype value = cnlExtension.get(0).getValue();

            if (null == value) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Cancellation reason missing."), SystemCode.BAD_REQUEST,
                        IssueType.INVALID);
            }
            appointmentDetail.setCancellationReason(value.toString());
        }
        appointmentDetail.setStatus(appointment.getStatus().toString().toLowerCase(Locale.UK));

        appointmentDetail.setMinutesDuration(appointment.getMinutesDuration());
        appointmentDetail.setPriority(appointment.getPriority());
        appointmentDetail.setStartDateTime(appointment.getStart());
        appointmentDetail.setEndDateTime(appointment.getEnd());

        List<Long> slotIds = new ArrayList<>();

        for (Reference slotReference : appointment.getSlot()) {
            try {
                String here = slotReference.getReference().substring(5);
                Long slotId = new Long(here);
                slotIds.add(slotId);
            } catch (NumberFormatException ex) {
                throw OperationOutcomeFactory
                        .buildOperationOutcomeException(
                                new UnprocessableEntityException(
                                        String.format("The slot reference value data type for %s is not valid.",
                                                slotReference.getReference())),
                                SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }
        }

        appointmentDetail.setSlotIds(slotIds);
        if (appointmentValidation.appointmentCommentTooLong(appointment)) {
            throw new UnprocessableEntityException("Appointment comment cannot be greater than " + APPOINTMENT_COMMENT_LENGTH + " characters");
        }

        appointmentDetail.setComment(appointment.getComment());
        appointmentDetail.setDescription(appointment.getDescription());

        for (AppointmentParticipantComponent participant : appointment.getParticipant()) {
            if (participant.getActor() != null) {
                String participantReference = participant.getActor().getReference();
                String actorIdString = participantReference.substring(participantReference.lastIndexOf("/") + 1);
                Long actorId;
                if (actorIdString.equals("null")) {
                    actorId = null;
                } else {
                    actorId = Long.valueOf(actorIdString);
                }

                if (participantReference.contains("Patient/")) {
                    appointmentDetail.setPatientId(actorId);
                } else if (participantReference.contains("Practitioner/")) {
                    appointmentDetail.setPractitionerId(actorId);
                } else if (participantReference.contains("Location/")) {
                    appointmentDetail.setLocationId(actorId);
                }
            } else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException(String.format("Participant Actor cannot be null")),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }
        }

        if (appointment.getCreated() != null) {
            Date created = appointment.getCreated();
            appointmentDetail.setCreated(created);
        }

        List<Resource> contained = appointment.getContained();
        if (contained != null && !contained.isEmpty()) {
            Resource org = contained.get(0);
            Organization bookingOrgRes = (Organization) org;
            BookingOrgDetail bookingOrgDetail = new BookingOrgDetail();
            appointmentValidation.validateBookingOrganizationValuesArePresent(bookingOrgRes);
            bookingOrgDetail.setName(bookingOrgRes.getName());
            bookingOrgDetail.setTelephone(bookingOrgRes.getTelecomFirstRep().getValue());
            if (!bookingOrgRes.getIdentifier().isEmpty()) {
                bookingOrgDetail.setOrgCode(bookingOrgRes.getIdentifierFirstRep().getValue());
                String system = bookingOrgRes.getIdentifierFirstRep().getSystem();
                // check that organization identifier system is https://fhir.nhs.uk/Id/ods-organization-code
                if (system != null && !system.trim().isEmpty()) {
                    if (!system.equals(ID_ODS_ORGANIZATION_CODE)) {
                        throw OperationOutcomeFactory.buildOperationOutcomeException(
                                new UnprocessableEntityException("Appointment organisation identifier system must be an ODS code!"),
                                SystemCode.INVALID_RESOURCE, IssueType.INVALID);
                    }
                } else {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("Appointment organisation identifier system must be populated!"),
                            SystemCode.INVALID_RESOURCE, IssueType.INVALID);
                }
            }
            bookingOrgDetail.setAppointmentDetail(appointmentDetail);
            appointmentDetail.setBookingOrganization(bookingOrgDetail);
        }

        return appointmentDetail;
    } // appointmentResourceConverterToAppointmentDetail

    private boolean isInThePast(Date dateTime) {
        return dateTime.before(new Date());
    }

}
