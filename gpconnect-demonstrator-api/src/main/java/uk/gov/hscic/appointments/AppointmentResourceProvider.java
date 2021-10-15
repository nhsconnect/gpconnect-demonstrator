package uk.gov.hscic.appointments;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javax.servlet.http.HttpServletRequest;
import org.hl7.fhir.exceptions.FHIRException;
import static uk.gov.hscic.InteractionId.REST_CANCEL_APPOINTMENT;
import static uk.gov.hscic.InteractionId.REST_UPDATE_APPOINTMENT;
import static uk.gov.hscic.SystemHeader.SSP_INTERACTIONID;
import static uk.gov.hscic.SystemURL.ID_ODS_ORGANIZATION_CODE;
import static uk.gov.hscic.SystemURL.SD_CC_APPOINTMENT_BOOKINGORG;
import static uk.gov.hscic.SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON;
import static uk.gov.hscic.SystemURL.SD_EXTENSION_GPC_DELIVERY_CHANNEL;
import static uk.gov.hscic.SystemURL.SD_EXTENSION_GPC_PRACTITIONER_ROLE;
import static uk.gov.hscic.SystemURL.SD_GPC_APPOINTMENT;
import static uk.gov.hscic.SystemURL.SD_GPC_ORGANIZATION;
import uk.gov.hscic.appointment.schedule.ScheduleSearch;
import static uk.gov.hscic.appointments.AppointmentValidation.APPOINTMENT_COMMENT_LENGTH;
import static uk.gov.hscic.appointments.AppointmentValidation.APPOINTMENT_DESCRIPTION_LENGTH;
import uk.gov.hscic.common.validators.VC;
import uk.gov.hscic.model.appointment.ScheduleDetail;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwInvalidRequest400_BadRequestException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntityInvalid422_ParameterException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntity422_InvalidResourceException;

@Component
public class AppointmentResourceProvider implements IResourceProvider {

    // Z format does not include a colon ie it is +0000 not +00:00
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private enum AppointmentOperation {
        BOOK,
        AMEND,
        CANCEL
    }

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
    private ScheduleSearch scheduleSearch;

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
            throwUnprocessableEntityInvalid422_ParameterException("Appointment search must have both 'le' and 'ge' start date parameters.");
        }

        Pattern dateOnlyPattern = Pattern.compile("[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])))");

        boolean lePrefix = false;
        boolean gePrefix = false;

        for (DateOrListParam filter : startDateList) {
            DateParam token = filter.getValuesAsQueryTokens().get(0);

            if (!dateOnlyPattern.matcher(token.getValueAsString()).matches()) {
                throwUnprocessableEntityInvalid422_ParameterException("Search dates must be of the format: yyyy-MM-dd and should not include time or timezone.");
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
                        throwUnprocessableEntityInvalid422_ParameterException("Unknown prefix on start date parameter: only le and ge prefixes are allowed.");
                }
            }
        }

        if (!gePrefix || !lePrefix) {
            throwUnprocessableEntityInvalid422_ParameterException("Parameters must contain two start parameters, one with prefix 'ge' and one with prefix 'le'.");
        } else if (startLowerDate.before(getYesterday())) {
            throwUnprocessableEntityInvalid422_ParameterException("Search dates from the past: " + startLowerDate.toString() + " are not allowed in appointment search.");
        } else if (startUpperDate.before(getYesterday())) {
            throwUnprocessableEntityInvalid422_ParameterException("Search dates from the past: " + startUpperDate.toString() + " are not allowed in appointment search.");
        } else if (startUpperDate.before(startLowerDate)) {
            throwUnprocessableEntityInvalid422_ParameterException("Upper search date must be after the lower search date.");
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

    /**
     * Create a new appointment
     *
     * @param appointment Resource
     * @return MethodOutcome
     */
    @Create
    public MethodOutcome createAppointment(@ResourceParam Appointment appointment) {

        Meta meta = appointment.getMeta();
        final List<UriType> profiles = meta.getProfile();

        // #203 validations
        // Uses VC class and lamda functions for brevity and to avoid masses of duplicated code
        // first lamda takes no params and returns a Boolean representing the fail condition ie true for fail
        // second lamda takes no params and returns a String describing the error condition
        // VC.execute evaluates the first lamda and if true theows an InvalidResource exception containg the string from the second lambda
        VC.execute(new VC[]{
            new VC(() -> profiles.isEmpty(), () -> "Meta element must be present in Appointment"),
            new VC(() -> !profiles.get(0).getValue().equalsIgnoreCase(SD_GPC_APPOINTMENT),
            () -> "Meta.profile " + profiles.get(0).getValue() + " is not equal to " + SD_GPC_APPOINTMENT),
            // what to do if > 1 meta profile element?
            new VC(() -> appointment.getStatus() == null, () -> "No status supplied"),
            new VC(() -> appointment.getStatus() != AppointmentStatus.BOOKED, () -> "Status must be \"booked\""),
            new VC(() -> appointment.getStart() == null || appointment.getEnd() == null, () -> "Both start and end date are required"),
            new VC(() -> appointment.getSlot().isEmpty(), () -> "At least one slot is required"),
            new VC(() -> appointment.getCreated() == null, () -> "Created must be populated"),
            new VC(() -> appointment.getParticipant().isEmpty(), () -> "At least one participant is required"),
            new VC(() -> !appointment.getIdentifierFirstRep().isEmpty() && appointment.getIdentifierFirstRep().getValue() == null,
            () -> "Appointment identifier value is required"),
            new VC(() -> appointment.getId() != null, () -> "Appointment id shouldn't be provided!"),
            new VC(() -> !appointment.getReason().isEmpty(), () -> "Appointment reason shouldn't be provided!"),
            // #157 refers to typeCode but means specialty  (name was changed)
            new VC(() -> !appointment.getSpecialty().isEmpty(), () -> "Appointment speciality shouldn't be provided!"),
            // #203
            // 1.2.7 Accept these two now
            //new VC(() -> !appointment.getServiceCategory().isEmpty(), () -> "Appointment service category shouldn't be provided!"),
            //new VC(() -> !appointment.getServiceType().isEmpty(), () -> "Appointment service type shouldn't be provided!"),
            new VC(() -> !appointment.getAppointmentType().isEmpty(), () -> "Appointment type shouldn't be provided!"),
            new VC(() -> !appointment.getIndication().isEmpty(), () -> "Appointment indication shouldn't be provided!"),
            new VC(() -> !appointment.getSupportingInformation().isEmpty(), () -> "Appointment supporting information shouldn't be provided!"),
            new VC(() -> !appointment.getIncomingReferral().isEmpty(), () -> "Appointment incoming referral shouldn't be provided!"),}
        );

        boolean patientFound = false;
        boolean locationFound = false;
        for (AppointmentParticipantComponent participant : appointment.getParticipant()) {
            if (participant.getActor().getReference() != null) {
                String reference = participant.getActor().getReference();

                // check for absolute reference #200
                checkReferenceIsRelative(reference);

                if (reference.contains("Patient")) {
                    patientFound = true;
                } else if (reference.contains("Location")) {
                    locationFound = true;
                }
            } else {
                throwUnprocessableEntity422_InvalidResourceException("Appointment resource is not valid as it does not contain a Participant Actor reference");
            }
        }

        if (!patientFound || !locationFound) {
            throwUnprocessableEntity422_InvalidResourceException("Appointment resource is not a valid resource required valid Patient and Location");
        }

        // variable accessed in a lambda must be declared final
        final boolean fPatientFound = patientFound;
        final boolean fLocationFound = locationFound;
        VC.execute(new VC[]{
            new VC(() -> !fPatientFound, () -> "Appointment resource is not a valid resource required valid Patient"),
            new VC(() -> !fLocationFound, () -> "Appointment resource is not a valid resource required valid Location"),});

        String locationId = null;
        String practitionerId = null;
        for (AppointmentParticipantComponent participant : appointment.getParticipant()) {

            Reference participantActor = participant.getActor();
            final Boolean searchParticipant = (participantActor != null);

            if (searchParticipant) {
                appointmentValidation.validateParticipantActor(participantActor);
            }

            CodeableConcept participantType = participant.getTypeFirstRep();
            final Boolean validParticipantType = appointmentValidation.validateParticipantType(participantType);

            VC.execute(new VC[]{
                new VC(() -> !searchParticipant, () -> "Supplied Participant is not valid. Must have an Actor."),
                new VC(() -> !validParticipantType, () -> "Supplied Participant is not valid. Must have a Type."),});

            // gets the logical id as a string
            if (participantActor.getReference().startsWith("Location")) {
                locationId = appointmentValidation.getId();
            } else if (participantActor.getReference().startsWith("Practitioner")) {
                practitionerId = appointmentValidation.getId();
            }

            appointmentValidation.validateParticipantStatus(participant.getStatus(), participant.getStatusElement(),
                    participant.getStatusElement());
        }

        List<Extension> extensions = validateAppointmentExtensions(appointment, profiles, AppointmentOperation.BOOK);

        // Store New Appointment
        AppointmentDetail appointmentDetail = appointmentResourceConverterToAppointmentDetail(appointment);
        List<SlotDetail> slots = new ArrayList<>();

        // we'll get the delivery channel from the slot
        String deliveryChannel = null;
        String practitionerRoleCode = null;
        String practitionerRoleDisplay = null;
        ScheduleDetail schedule = null;
        String serviceType = null;
        for (Long slotId : appointmentDetail.getSlotIds()) {
            SlotDetail slotDetail = slotSearch.findSlotByID(slotId);

            new VC(() -> slotDetail == null, () -> String.format("Slot resource reference value %s is not a valid resource.", slotId)).execute();

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
                throwUnprocessableEntity422_InvalidResourceException(String.format("Subsequent slot (Slot/%s) delivery channel (%s) is not equal to initial slot delivery channel (%s).",
                        slotId, deliveryChannel, slotDetail.getDeliveryChannelCode()));
            }

            // 1.2.7 check service type is the same as initial service type
            if (serviceType == null) {
                serviceType = slotDetail.getTypeDisply();
            } else if (!serviceType.equals(slotDetail.getTypeDisply())) {
                // added at 1.2.2
                throwUnprocessableEntity422_InvalidResourceException(String.format("Subsequent slot (Slot/%s) service type (%s) is not equal to initial slot service type (%s).",
                        slotId, serviceType, slotDetail.getTypeDisply()));
            }

            if (schedule == null) {
                // load the schedule so we can get the Practitioner ID
                schedule = scheduleSearch.findScheduleByID(slotDetail.getScheduleReference());

                // add practitioner id so we can get the practitioner role
                // practitioner is derived but needs to be stored fgr comparison if provided
                appointmentDetail.setPractitionerId(schedule.getPractitionerId());
            }

            if (practitionerRoleDisplay == null) {
                practitionerRoleDisplay = schedule.getPractitionerRoleDisplay();
                practitionerRoleCode = schedule.getPractitionerRoleCode();
            }
            slots.add(slotDetail);
        } // for slot

        validateUpdateExtensions(deliveryChannel, practitionerRoleDisplay, practitionerRoleCode, extensions);

        // #203 check location id matches the one in the schedule
        if (locationId != null && !locationId.equals(schedule.getLocationId().toString())) {
            throwUnprocessableEntity422_InvalidResourceException(String.format("Provided location id (%s) is not equal to Schedule location id (%s)",
                    locationId, schedule.getLocationId().toString()));
        }

        // #203 check practitioner id matches the one in the schedule if there is one
        if (practitionerId != null) {
            if (!practitionerId.equals(schedule.getPractitionerId().toString())) {
                throwUnprocessableEntity422_InvalidResourceException(String.format("Provided practitioner id (%s) is not equal to Schedule practitioner id (%s)",
                        practitionerId, schedule.getPractitionerId().toString()));
            }
        }
        // #203
        Date firstSlotStart = slots.get(0).getStartDateTime();
        Date lastSlotEnd = slots.get(slots.size() - 1).getEndDateTime();

        // need to insert a colon in the timezone string
        String firstSlotStartStr = TIMESTAMP_FORMAT.format(firstSlotStart).replaceFirst("([0-9]{2})([0-9]{2})$", "$1:$2");
        String lastSlotEndStr = TIMESTAMP_FORMAT.format(lastSlotEnd).replaceFirst("([0-9]{2})([0-9]{2})$", "$1:$2");

        VC.execute(new VC[]{
            new VC(() -> appointment.getStart().compareTo(firstSlotStart) != 0,
            () -> String.format("Start date '%s' must match start date of first slot '%s'", appointment.getStart(), firstSlotStart)),
            new VC(() -> appointment.getEnd().compareTo(lastSlotEnd) != 0,
            () -> String.format("End date '%s' must match end date of last slot '%s'", appointment.getEnd(), lastSlotEnd)),
            // #218 strings should match exactly
            new VC(() -> !appointment.getStartElement().getValueAsString().equals(firstSlotStartStr),
            () -> String.format("Start date '%s' must lexically match start date of first slot '%s'", appointment.getStartElement().getValueAsString(), firstSlotStartStr)),
            new VC(() -> !appointment.getEndElement().getValueAsString().equals(lastSlotEndStr),
            () -> String.format("End date '%s' must lexically match end date of last slot '%s'", appointment.getEndElement().getValueAsString(), lastSlotEndStr)),
            // This probably can never happen under fhir
            new VC(() -> appointment.getSlot().isEmpty(), () -> "Slot reference must be populated")
        }
        );

        int durationFromSlots = (int) (lastSlotEnd.getTime() - firstSlotStart.getTime()) / (1000 * 60);
        // #203 validate optional minutes duration
        if (appointment.hasMinutesDuration()) {
            int providedDuration = appointment.getMinutesDuration();
            new VC(() -> durationFromSlots != providedDuration,
                    () -> String.format("Provided duration (%d minutes) is not equal to actual appointment duration (%d minutes)",
                            providedDuration, durationFromSlots)).execute();
        } else {
            // not provided so write the calculated value into the appointmentDetail object
            appointmentDetail.setMinutesDuration(durationFromSlots);
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

    /**
     *
     * @param deliveryChannel
     * @param practitionerRoleDisplay
     * @param practitionerRoleCode
     * @param extensions list of extensions to be validated
     */
    private void validateUpdateExtensions(String deliveryChannel, String practitionerRoleDisplay, String practitionerRoleCode, List<Extension> extensions) {
        final String fDeliveryChannel = deliveryChannel;
        final String fPractitionerRoleDisplay = practitionerRoleDisplay;
        final String fPractitionerRoleCode = practitionerRoleCode;

        for (Extension extension : extensions) {
            switch (extension.getUrl()) {
                case SD_EXTENSION_GPC_PRACTITIONER_ROLE:
                    // #203 if present value must match slot/schedule value
                    try {
                        final CodeableConcept codeableConcept = (CodeableConcept) extension.getValue();
                        VC.execute(new VC[]{
                            new VC(() -> codeableConcept.getCodingFirstRep().getDisplay() != null && !Objects.equals(codeableConcept.getCodingFirstRep().getDisplay(), fPractitionerRoleDisplay), ()
                            -> String.format("Practitioner role display provided (%s) doesn't match schedule value (%s)",
                            codeableConcept.getCodingFirstRep().getDisplay(), fPractitionerRoleDisplay)),
                            new VC(() -> codeableConcept.getCodingFirstRep().getCode() != null && !Objects.equals(codeableConcept.getCodingFirstRep().getCode(), fPractitionerRoleCode), ()
                            -> String.format("Practitioner role code provided (%s) doesn't match schedule value (%s)",
                            codeableConcept.getCodingFirstRep().getCode(), fPractitionerRoleCode))
                        });
                    } catch (ClassCastException ex) {
                        throwUnprocessableEntity422_InvalidResourceException("Practitioner Role Extension value is not a valueCodeableConcept");
                    }
                    break;

                case SD_EXTENSION_GPC_DELIVERY_CHANNEL:
                    // #203 if present value must match slot value
                    try {
                        final CodeType codeType = (CodeType) extension.getValue();
                        new VC(() -> !Objects.equals(codeType.getValue(), fDeliveryChannel), ()
                                -> String.format("Delivery Channel provided (%s) doesn't match slot value (%s)",
                                        codeType.getValue(), fDeliveryChannel)).execute();
                    } catch (ClassCastException ex) {
                        throwUnprocessableEntity422_InvalidResourceException("Delivery Channel Extension value is not a valueCode");
                    }
                    break;

                case SD_CC_APPOINTMENT_BOOKINGORG:
                    Reference reference = (Reference) extension.getValue();
                    // #242
                    if (!reference.getReference().startsWith("#")) {
                        throwUnprocessableEntity422_InvalidResourceException("Booking organization reference does not start with #");
                    }
                    break;
            } //switch
        } // for extensions
    }

    /**
     *
     * @param resource
     * @param profiles
     */
    private void validateBookingOrg(Resource resource, final List<UriType> profiles) {
        Meta meta;
        try {
            Organization bookingOrg = (Organization) resource;
            meta = bookingOrg.getMeta();
            final List<UriType> orgProfiles = meta.getProfile();

            // booking org meta
            VC.execute(new VC[]{
                new VC(() -> orgProfiles.isEmpty(), () -> "Meta element must be present in contained Organization"),
                new VC(() -> orgProfiles.get(0).getValue() == null, () -> "Meta URI element is null"),
                new VC(() -> !orgProfiles.get(0).getValue().equalsIgnoreCase(SD_GPC_ORGANIZATION),
                () -> "Meta.profile " + profiles.get(0).getValue() + " is not equal to " + SD_GPC_ORGANIZATION), // what to do if > 1 meta profile element?
            }
            );

            Identifier identifier = bookingOrg.getIdentifierFirstRep();
            boolean phoneFound = false;
            for (ContactPoint contactPoint : bookingOrg.getTelecom()) {
                if (contactPoint.getSystem() == ContactPointSystem.PHONE) {
                    phoneFound = true;
                    break;
                }
            }
            final boolean fPhoneFound = phoneFound;
            VC.execute(new VC[]{
                new VC(() -> !identifier.getSystem().equals(ID_ODS_ORGANIZATION_CODE),
                () -> String.format(
                "Contained Organization.identifier.system %s is not equal to %s", identifier.getSystem(), ID_ODS_ORGANIZATION_CODE)),
                new VC(() -> identifier.getValue() == null, () -> "Contained Organization.identifier.value is not populated"),
                new VC(() -> bookingOrg.getName() == null, () -> "Contained Organization.name is not populated"),
                new VC(() -> bookingOrg.getTelecom().isEmpty(), () -> "Contained Organization must contain at least one telecom with a system of phone"),
                new VC(() -> !fPhoneFound, () -> "Contained Organization must contain at least one telecom with a system of phone"),}
            );

        } catch (ClassCastException ex) {
            throwUnprocessableEntity422_InvalidResourceException(String.format("Contained object with url %s is not an Organization", SD_CC_APPOINTMENT_BOOKINGORG));
        }
    }

    /**
     * amend or cancel an existing appointment
     *
     * @param appointmentId
     * @param appointment Resource
     * @param theRequest required to access the interaction id
     * @return MethodOutcome
     */
    @Update
    public MethodOutcome updateAppointment(@IdParam IdType appointmentId, @ResourceParam Appointment appointment, HttpServletRequest theRequest) {
        MethodOutcome methodOutcome = new MethodOutcome();
        OperationOutcome operationalOutcome = new OperationOutcome();
        AppointmentDetail appointmentDetail = appointmentResourceConverterToAppointmentDetail(appointment);

        Meta meta = appointment.getMeta();
        final List<UriType> profiles = meta.getProfile();

        // #203 validations
        VC.execute(new VC[]{
            new VC(() -> profiles.isEmpty(), () -> "Meta element must be present in Appointment"),
            new VC(() -> !profiles.get(0).getValue().equalsIgnoreCase(SD_GPC_APPOINTMENT),
            () -> "Meta.profile " + profiles.get(0).getValue() + " is not equal to " + SD_GPC_APPOINTMENT), // what to do if > 1 meta profile element?
            // #203
            new VC(() -> !appointment.getReason().isEmpty(), () -> "Appointment reason shouldn't be provided!"),
            new VC(() -> !appointment.getSpecialty().isEmpty(), () -> "Appointment speciality shouldn't be provided!"),
            // 1.2.7 we now allow these two attributes
            // new VC(() -> !appointment.getServiceCategory().isEmpty(), () -> "Appointment service category shouldn't be provided!"),
            // new VC(() -> !appointment.getServiceType().isEmpty(), () -> "Appointment service type shouldn't be provided!"),
            new VC(() -> !appointment.getAppointmentType().isEmpty(), () -> "Appointment type shouldn't be provided!"),
            new VC(() -> !appointment.getIndication().isEmpty(), () -> "Appointment indication shouldn't be provided!"),
            new VC(() -> !appointment.getSupportingInformation().isEmpty(), () -> "Appointment supporting information shouldn't be provided!"),
            new VC(() -> !appointment.getIncomingReferral().isEmpty(), () -> "Appointment incoming referral shouldn't be provided!"),});

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

        // 1.2.7 set the old service type and service category for comparison with incoming update/cancel content values
        SlotDetail slotDetail1 = slotSearch.findSlotByID(oldAppointmentDetail.getSlotIds().get(0));
        oldAppointmentDetail.setServiceType(slotDetail1.getTypeDisply());
        ScheduleDetail scheduleDetail = scheduleSearch.findScheduleByID(slotDetail1.getScheduleReference());
        oldAppointmentDetail.setServiceCategory(scheduleDetail.getTypeDescription());

        String oldAppointmentVersionId = String.valueOf(oldAppointmentDetail.getLastUpdated().getTime());
        String newAppointmentVersionId = appointmentId.getVersionIdPart();
        if (newAppointmentVersionId != null && !newAppointmentVersionId.equalsIgnoreCase(oldAppointmentVersionId)) {
            throw new ResourceVersionConflictException("The specified version (" + newAppointmentVersionId
                    + ") did not match the current resource version (" + oldAppointmentVersionId + ")");
        }

        // check for absolute reference #200
        Iterator<AppointmentParticipantComponent> iter = appointment.getParticipant().iterator();
        while (iter.hasNext()) {
            AppointmentParticipantComponent participant = iter.next();
            if (participant.getActor() != null) {
                checkReferenceIsRelative(participant.getActor().getReference());
            }
        }

        String interactionId = theRequest.getHeader(SSP_INTERACTIONID);
        // Determine if it is a cancel or an amend. This was previously a check for the presence of a cancellation reason
        // but that is not sufficient. We can sefely assume that the interaction id is populated at this point.

        AppointmentOperation appointmentOperation = null;
        final AppointmentDetail fAppointmentDetail = appointmentDetail;
        switch (interactionId) {
            case REST_CANCEL_APPOINTMENT:
                appointmentOperation = AppointmentOperation.CANCEL;
                // added at 1.2.2
                VC.execute(new VC[]{
                    new VC(() -> appointment.getStatus() != AppointmentStatus.CANCELLED, () -> "Status must be \"cancelled\""),
                    // #203
                    new VC(() -> isInThePast(fAppointmentDetail.getStartDateTime()), () -> "The cancellation start date cannot be in the past"),
                    new VC(() -> fAppointmentDetail.getCancellationReason() == null, () -> "The cancellation reason must be provided"),
                    // no point in this since fhir forbids empty elements
                    new VC(() -> fAppointmentDetail.getCancellationReason().isEmpty(), () -> "The cancellation reason can not be blank"),}
                );

                validateAppointmentExtensions(appointment, profiles, appointmentOperation);

                // #172
                String appointmentType = appointment.getAppointmentType().getText();
                if (appointmentType != null) {
                    throwUnprocessableEntity422_InvalidResourceException("The appointment type cannot be updated on a cancellation");
                }

                // This is a Cancellation - so copy across fields which can be
                // altered
                List cancelComparisonResult = compareAppointmentsForInvalidProperty(AppointmentOperation.CANCEL, oldAppointmentDetail,
                        appointmentDetail);

                if (cancelComparisonResult.size() > 0) {
                    throwUnprocessableEntity422_InvalidResourceException("Invalid Appointment property has been amended (cancellation) " + cancelComparisonResult);
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
                break;

            case REST_UPDATE_APPOINTMENT:
                appointmentOperation = AppointmentOperation.AMEND;
                VC.execute(new VC[]{
                    new VC(() -> appointment.getStatus() != AppointmentStatus.BOOKED, () -> "Status must be \"booked\""),
                    // #199 (inhibit amend cancelled record) was checking incoming not existing record
                    // this subsumes #161 which only inhibited amendment of the cancellation reason in an amend
                    new VC(() -> fAppointmentDetail.getCancellationReason() != null, () -> "Cannot amend cancellation reason in appointment amend"),
                    // added at 1.2.2
                    new VC(() -> isInThePast(fAppointmentDetail.getStartDateTime()), () -> "The appointment amend start date cannot be in the past"),}
                );

                List amendComparisonResult = compareAppointmentsForInvalidProperty(AppointmentOperation.AMEND, oldAppointmentDetail,
                        appointmentDetail);

                if (amendComparisonResult.size() > 0) {
                    throwUnprocessableEntity422_InvalidResourceException("Invalid Appointment property has been amended " + amendComparisonResult);
                }

                validateAppointmentExtensions(appointment, profiles, appointmentOperation);

                // This is an Amend
                oldAppointmentDetail.setComment(appointmentDetail.getComment());
                oldAppointmentDetail.setDescription(appointmentDetail.getDescription());
                appointmentDetail = oldAppointmentDetail;
                break;

            default:
                System.err.println("AppointmentResourceProvider.updateAppointment Unhandled interaction id  " + interactionId);
        }

        // we'll get the delivery channel from the slot
        String deliveryChannel = null;
        String practitionerRoleCode = null;
        String practitionerRoleDisplay = null;
        ScheduleDetail schedule = null;

        // Common to both Update and cancel
        // slots valid?
        List<SlotDetail> slots = new ArrayList<>();
        for (Long slotId : appointmentDetail.getSlotIds()) {
            SlotDetail slotDetail = slotSearch.findSlotByID(slotId);

            if (slotDetail == null) {
                throwUnprocessableEntity422_InvalidResourceException("Slot resource reference is not a valid resource");
            }

            if (deliveryChannel == null) {
                deliveryChannel = slotDetail.getDeliveryChannelCode();
            }

            if (schedule == null) {
                // load the schedule so we can get the Practitioner ID
                schedule = scheduleSearch.findScheduleByID(slotDetail.getScheduleReference());
            }
            if (practitionerRoleDisplay == null) {
                practitionerRoleDisplay = schedule.getPractitionerRoleDisplay();
                practitionerRoleCode = schedule.getPractitionerRoleCode();
            }

            slots.add(slotDetail);
        }
        validateUpdateExtensions(deliveryChannel, practitionerRoleDisplay, practitionerRoleCode, appointment.getExtension());

        // dates valid?
        // #203
        Date firstSlotStart = slots.get(0).getStartDateTime();
        Date lastSlotEnd = slots.get(slots.size() - 1).getEndDateTime();

        // need to insert a colon in the timezone string
        String firstSlotStartStr = TIMESTAMP_FORMAT.format(firstSlotStart).replaceFirst("([0-9]{2})([0-9]{2})$", "$1:$2");
        String lastSlotEndStr = TIMESTAMP_FORMAT.format(lastSlotEnd).replaceFirst("([0-9]{2})([0-9]{2})$", "$1:$2");

        VC.execute(new VC[]{
            new VC(() -> appointment.getStart().compareTo(firstSlotStart) != 0,
            () -> String.format("Start date '%s' must match start date of first slot '%s'", appointment.getStart(), firstSlotStart)),
            new VC(() -> appointment.getEnd().compareTo(lastSlotEnd) != 0,
            () -> String.format("End date '%s' must match end date of last slot '%s'", appointment.getEnd(), lastSlotEnd)),
            // #218 strings should match exactly
            new VC(() -> !appointment.getStartElement().getValueAsString().equals(firstSlotStartStr),
            () -> String.format("Start date '%s' must lexically match start date of first slot '%s'", appointment.getStartElement().getValueAsString(), firstSlotStartStr)),
            new VC(() -> !appointment.getEndElement().getValueAsString().equals(lastSlotEndStr),
            () -> String.format("End date '%s' must lexically match end date of last slot '%s'", appointment.getEndElement().getValueAsString(), lastSlotEndStr)),
            new VC(() -> appointment.getSlot().size() != slots.size(),
            () -> String.format("Slot count mismatch %d provided appointment has %d", appointment.getSlot().size(), slots.size())),});

        // check the slots match
        HashSet<String> hs = new HashSet<>();
        for (SlotDetail slotDetail : slots) {
            hs.add("Slot/" + slotDetail.getId());
        }

        for (Reference reference : appointment.getSlot()) {
            if (!hs.contains(reference.getReference())) {
                throwUnprocessableEntity422_InvalidResourceException(String.format("Provided slot id %s does not exist in booked appointment", reference.getReference()));
            }
        }

        appointmentDetail.setLastUpdated(new Date()); // Update version and
        // lastUpdated timestamp
        appointmentDetail = appointmentStore.saveAppointment(appointmentDetail, slots);

        methodOutcome.setId(new IdDt("Appointment", appointmentDetail.getId()));
        methodOutcome.setResource(appointmentDetailToAppointmentResourceConverter(appointmentDetail));

        return methodOutcome;
    } // updateAppointment

    /**
     *
     * @param appointment
     * @param profiles
     * @return list of Extensions
     */
    private List<Extension> validateAppointmentExtensions(Appointment appointment, final List<UriType> profiles, AppointmentOperation appointmentOperation) {
        // #203 check extension
        List<Extension> extensions = appointment.getExtension();
        if (extensions.isEmpty()) {
            throwUnprocessableEntity422_InvalidResourceException("Booking organization extension must be present");
        }
        Reference bookingOrgRef = null;
        for (Extension extension : extensions) {
            switch (extension.getUrl()) {
                case SD_CC_APPOINTMENT_BOOKINGORG:
                    try {
                        bookingOrgRef = (Reference) extension.getValue();
                    } catch (ClassCastException ex) {
                        throwUnprocessableEntity422_InvalidResourceException("Booking Organisation Extension value is not a Reference");
                    }
                    break;
                case SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON:
                    if (appointmentOperation != AppointmentOperation.CANCEL) {
                        throwUnprocessableEntity422_InvalidResourceException("Cancellation Reason shouldn't be provided in " + appointmentOperation + " Appointment!");
                    }
                    break;
            }
        }
        if (bookingOrgRef == null) {
            throwUnprocessableEntity422_InvalidResourceException("Booking organization extension must be present");
        }
        // #203 check that the reference points to the contained booking org
        for (Resource resource : appointment.getContained()) {
            if (resource.getId().equals(bookingOrgRef.getReference())) {
                validateBookingOrg(resource, profiles);
                break;
            } // if booking org
        } // for contained resource
        return extensions;
    }

    /**
     * updates and cancels can only change certain fields trap changes that are
     * not allowed
     *
     * @param oldAppointmentDetail
     * @param appointmentDetail
     * @return ArrayList of failing attributes
     */
    private List<String> compareAppointmentsForInvalidProperty(AppointmentOperation operation, AppointmentDetail oldAppointmentDetail,
            AppointmentDetail appointmentDetail) {
        HashMap<String, Boolean> results = new HashMap<>();

        results.put("id", Objects.equals(oldAppointmentDetail.getId(), appointmentDetail.getId()));
        results.put("patientId", Objects.equals(oldAppointmentDetail.getPatientId(), appointmentDetail.getPatientId()));
        // this is an optional field so only compare if a value is supplied
        if (appointmentDetail.getPractitionerId() != null) {
            results.put("practitionerId", Objects.equals(oldAppointmentDetail.getPractitionerId(), appointmentDetail.getPractitionerId()));
        }
        results.put("locationId", Objects.equals(oldAppointmentDetail.getLocationId(), appointmentDetail.getLocationId()));
        // this is an optional field so only compare if a value is supplied (default is zero)
        if (appointmentDetail.getMinutesDuration() != 0) {
            results.put("duration", Objects.equals(oldAppointmentDetail.getMinutesDuration(), appointmentDetail.getMinutesDuration()));
        }
        results.put("priority", Objects.equals(oldAppointmentDetail.getPriority(), appointmentDetail.getPriority()));

        // 1.2.7 check no update to service category or service type that makes a change
        // this is an optional field so only compare if a value is supplied
        if (appointmentDetail.getServiceCategory() != null) {
            results.put("serviceCategory", Objects.equals(oldAppointmentDetail.getServiceCategory(), appointmentDetail.getServiceCategory()));
        }
        // this is an optional field so only compare if a value is supplied
        if (appointmentDetail.getServiceType() != null) {
            results.put("serviceType", Objects.equals(oldAppointmentDetail.getServiceType(), appointmentDetail.getServiceType()));
        }

        switch (operation) {
            case AMEND:
                // comment and description are allowed to change
                results.put("status", Objects.equals(oldAppointmentDetail.getStatus(), appointmentDetail.getStatus()));
                results.put("startDateTime", Objects.equals(oldAppointmentDetail.getStartDateTime(), appointmentDetail.getStartDateTime()));
                results.put("endDateTime", Objects.equals(oldAppointmentDetail.getEndDateTime(), appointmentDetail.getEndDateTime()));
                break;
            case CANCEL:
                if (appointmentDetail.getComment() != null) {
                    results.put("comment", Objects.equals(oldAppointmentDetail.getComment(), appointmentDetail.getComment()));
                }
                if (appointmentDetail.getDescription() != null) {
                    results.put("description", Objects.equals(oldAppointmentDetail.getDescription(), appointmentDetail.getDescription()));
                }
                break;
            default:
                System.err.println("AppointmentResourceProvider.compareAppointmentsForInvalidProperty Unhandled operation  " + operation);
        }

        ArrayList<String> result = new ArrayList<>();
        if (results.values().contains(false)) {
            for (String key : results.keySet()) {
                if (!results.get(key)) {
                    result.add(key);
                }
            }
        }

        // add in comaprison fails from the booking org
        List<String> bookingOrgResults = bookingOrganizationsEqual(oldAppointmentDetail.getBookingOrganization(),
                appointmentDetail.getBookingOrganization());

        for (String orgResult : bookingOrgResults) {
            result.add("bookingOrganization." + orgResult);
        }

        return result;
    }

    /**
     *
     * @param bookingOrg1
     * @param bookingOrg2
     * @return List of attributes failing comparison
     */
    private List<String> bookingOrganizationsEqual(BookingOrgDetail bookingOrg1, BookingOrgDetail bookingOrg2) {
        HashMap<String, Boolean> results = new HashMap<>();
        if (bookingOrg1 != null && bookingOrg2 != null) {
            results.put("name", bookingOrg1.getName().equals(bookingOrg2.getName()));
            // #203 added code
            results.put("orgcode", bookingOrg1.getOrgCode().equals(bookingOrg2.getOrgCode()));
            results.put("telecom", bookingOrg1.getTelephone().equals(bookingOrg2.getTelephone()));
        } else {
            // One of the booking orgs is null so both should be null
            if (bookingOrg1 != null && bookingOrg2 == null) {
                results.put("original is not null, provided is null", false);
            } else if (bookingOrg1 == null && bookingOrg2 != null) {
                results.put("original is null, provided is not null ", false);
            };
        }

        // compile a list of attributes failing to compare
        ArrayList<String> result = new ArrayList<>();
        if (results.values().contains(false)) {
            for (String key : results.keySet()) {
                if (!results.get(key)) {
                    result.add(key);
                }
            }
        }
        return result;
    }

    /**
     * AppointmentDetail to fhir resource Appointment
     *
     * @param appointmentDetail
     * @return Appointment Resource
     */
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

        // #157 derive delivery channel from slot
        List<Long> sids = appointmentDetail.getSlotIds();
        ScheduleDetail scheduleDetail = null;
        if (sids.size() > 0) {
            // get the first slot but it should not matter because for multi slot appts the deliveryChannel is always the same
            SlotDetail slotDetail = slotSearch.findSlotByID(sids.get(0));
            String deliveryChannel = slotDetail.getDeliveryChannelCode();
            if (deliveryChannel != null && !deliveryChannel.trim().isEmpty()) {
                Extension deliveryChannelExtension = new Extension(SystemURL.SD_EXTENSION_GPC_DELIVERY_CHANNEL, new CodeType(deliveryChannel));
                appointment.addExtension(deliveryChannelExtension);
            }
            scheduleDetail = scheduleSearch.findScheduleByID(slotDetail.getScheduleReference());
        }

        // practitioner role extension here
        // lookup the practitioner
        Long practitionerID = appointmentDetail.getPractitionerId();
        if (practitionerID != null) {
            // #195 we need to get this detail from the schedule not the practitioner table
            if (scheduleDetail != null) {
                Coding roleCoding = new Coding(SystemURL.VS_GPC_PRACTITIONER_ROLE, scheduleDetail.getPractitionerRoleCode(),
                        scheduleDetail.getPractitionerRoleDisplay());

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
        // #218 Date time formats
        appointment.getStartElement().setPrecision(TemporalPrecisionEnum.SECOND);
        appointment.getEndElement().setPrecision(TemporalPrecisionEnum.SECOND);

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

            // add extension with reference to contained item
            String reference = "#1";
            Reference orgRef = new Reference(reference);
            Extension bookingOrgExt = new Extension(SystemURL.SD_CC_APPOINTMENT_BOOKINGORG, orgRef);

            appointment.addExtension(bookingOrgExt);
            BookingOrgDetail bookingOrgDetail = appointmentDetail.getBookingOrganization();

            Organization bookingOrg = new Organization();
            bookingOrg.setId(reference);
            bookingOrg.getNameElement().setValue(bookingOrgDetail.getName());

            // #198 org phone now persists usetype and system
            ContactPoint orgTelecom = bookingOrg.getTelecomFirstRep();
            orgTelecom.setValue(bookingOrgDetail.getTelephone());
            try {
                orgTelecom.setSystem(ContactPointSystem.fromCode(bookingOrgDetail.getSystem().toLowerCase()));
                if (bookingOrgDetail.getUsetype() != null && !bookingOrgDetail.getUsetype().trim().isEmpty()) {
                    orgTelecom.setUse(ContactPointUse.fromCode(bookingOrgDetail.getUsetype().toLowerCase()));
                }
            } catch (FHIRException ex) {
                //Logger.getLogger(AppointmentResourceProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            bookingOrg.getMeta().addProfile(SystemURL.SD_GPC_ORGANIZATION);

            if (null != bookingOrgDetail.getOrgCode()) {
                bookingOrg.getIdentifierFirstRep().setSystem(SystemURL.ID_ODS_ORGANIZATION_CODE)
                        .setValue(bookingOrgDetail.getOrgCode());
            }

            // add contained booking organization resource
            appointment.getContained().add(bookingOrg);
        }  // if bookingOrganization

        appointment.setMinutesDuration(appointmentDetail.getMinutesDuration());

        // 1.2.7 set service category from schedule type description
        appointment.setServiceCategory(new CodeableConcept().setText(scheduleDetail.getTypeDescription()));

        // 1.2.7 set service type from slot type description
        appointment.addServiceType(new CodeableConcept().setText(slotSearch.findSlotByID(sids.get(0)).getTypeDisply()));
        
        // 1.2.8 add HealthcareService id if there's one in the schedule
        if ( scheduleDetail.getServiceId() != null) {
            AppointmentParticipantComponent participant = new AppointmentParticipantComponent();
            participant.setActor(new Reference("HealthcareService/"+scheduleDetail.getServiceId()));
            participant.setStatus(ParticipationStatus.ACCEPTED);
            appointment.addParticipant(participant);
        }

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

    /**
     * fhir resource Appointment to AppointmentDetail
     *
     * @param appointment Resource
     * @return populated AppointmentDetail
     */
    private AppointmentDetail appointmentResourceConverterToAppointmentDetail(Appointment appointment) {

        appointmentValidation.validateAppointmentExtensions(appointment.getExtension());

        if (appointmentValidation.appointmentDescriptionTooLong(appointment)) {
            throwUnprocessableEntity422_InvalidResourceException("Appointment description cannot be greater then " + APPOINTMENT_DESCRIPTION_LENGTH + " characters");
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
                throwInvalidRequest400_BadRequestException("Cancellation reason missing.");
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
            // #200 check slots for absolute references
            checkReferenceIsRelative(slotReference.getReference());
            try {
                String here = slotReference.getReference().substring("Slot/".length());
                Long slotId = new Long(here);
                slotIds.add(slotId);
            } catch (NumberFormatException ex) {
                throwUnprocessableEntity422_InvalidResourceException(String.format("The slot reference value data type for %s is not valid.", slotReference.getReference()));
            }
        }

        appointmentDetail.setSlotIds(slotIds);
        if (appointmentValidation.appointmentCommentTooLong(appointment)) {
            throwUnprocessableEntity422_InvalidResourceException("Appointment comment cannot be greater than " + APPOINTMENT_COMMENT_LENGTH + " characters");
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
                throwUnprocessableEntity422_InvalidResourceException("Participant Actor cannot be null");
            }
        }

        if (appointment.getCreated() != null) {
            Date created = appointment.getCreated();
            appointmentDetail.setCreated(created);
        }

        // #200 check extensions for absolute references
        List<Extension> extensions = appointment.getExtension();
        for (Extension extension : extensions) {
            try {
                Reference reference = (Reference) extension.getValue();
                if (reference != null) {
                    checkReferenceIsRelative(reference.getReference());
                }
            } catch (ClassCastException ex) {

            }
        }

        List<Resource> contained = appointment.getContained();
        if (contained != null && !contained.isEmpty()) {
            Resource org = contained.get(0);
            Organization bookingOrgRes = (Organization) org;
            BookingOrgDetail bookingOrgDetail = new BookingOrgDetail();
            appointmentValidation.validateBookingOrganizationValuesArePresent(bookingOrgRes);
            bookingOrgDetail.setName(bookingOrgRes.getName());

            // #198 add system and optional use type. Pick system phone if > 1 and available otherwise use the one supplied
            Iterator<ContactPoint> iter = bookingOrgRes.getTelecom().iterator();
            ContactPoint telecom = bookingOrgRes.getTelecomFirstRep();
            while (iter.hasNext()) {
                ContactPoint thisTelecom = iter.next();
                if (thisTelecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                    telecom = thisTelecom;
                    break;
                }
            }

            bookingOrgDetail.setTelephone(telecom.getValue());
            bookingOrgDetail.setSystem(telecom.getSystem().toString());
            if (telecom.getUse() != null && !telecom.getUse().toString().trim().isEmpty()) {
                bookingOrgDetail.setUsetype(telecom.getUse().toString());
            }

            if (!bookingOrgRes.getIdentifier().isEmpty()) {
                bookingOrgDetail.setOrgCode(bookingOrgRes.getIdentifierFirstRep().getValue());
                String system = bookingOrgRes.getIdentifierFirstRep().getSystem();
                // check that organization identifier system is https://fhir.nhs.uk/Id/ods-organization-code
                if (system != null && !system.trim().isEmpty()) {
                    if (!system.equals(ID_ODS_ORGANIZATION_CODE)) {
                        throwUnprocessableEntity422_InvalidResourceException("Appointment organisation identifier system must be an ODS code!");
                    }
                } else {
                    throwUnprocessableEntity422_InvalidResourceException("Appointment organisation identifier system must be populated!");
                }
            }
            bookingOrgDetail.setAppointmentDetail(appointmentDetail);
            appointmentDetail.setBookingOrganization(bookingOrgDetail);

            // 1.2.7
            appointmentDetail.setServiceCategory(appointment.getServiceCategory().getText());
            appointmentDetail.setServiceType(appointment.getServiceTypeFirstRep().getText());
        }
        
        return appointmentDetail;
    } // appointmentResourceConverterToAppointmentDetail

    private boolean isInThePast(Date dateTime) {
        return dateTime.before(new Date());
    }

    /**
     * absolute references look like valid URLs so..
     *
     * @param reference String
     * @throws an UnprocessableEntityException if the reference is absolute
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    private void checkReferenceIsRelative(String reference) {
        if (reference != null) {
            try {
                new URL(reference);
            } catch (MalformedURLException ex) {
                // if its not a valid URL its not absolute
                return;
            }
            // if we get here its a valid URL so its an absolute reference
            throwUnprocessableEntity422_InvalidResourceException("Reference " + reference + " must be relative not absolute");
        }
    }

}
