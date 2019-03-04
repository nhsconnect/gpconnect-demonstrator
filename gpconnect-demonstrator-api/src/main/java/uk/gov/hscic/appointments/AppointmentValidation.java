package uk.gov.hscic.appointments;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.SystemURL;
import static uk.gov.hscic.appointments.AppointmentResourceProvider.throwInvalidResource;
import uk.gov.hscic.common.validators.VC;
import uk.gov.hscic.location.LocationSearch;
import uk.gov.hscic.model.location.LocationDetails;
import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.model.practitioner.PractitionerDetails;
import uk.gov.hscic.patient.details.PatientSearch;
import uk.gov.hscic.practitioner.PractitionerSearch;

@Component
public class AppointmentValidation {

    @Autowired
    private PatientSearch patientSearch;

    @Autowired
    private PractitionerSearch practitionerSearch;

    @Autowired
    private LocationSearch locationSearch;

    public static final int APPOINTMENT_DESCRIPTION_LENGTH = 100;
    public static final int APPOINTMENT_COMMENT_LENGTH = 500;
    private String idPart = null; // remember this for validation against location/practitioner etc in schedule should be an integer

    public boolean appointmentDescriptionTooLong(Appointment appointment) {
        return appointment.getDescription() != null ? appointment.getDescription().length() > APPOINTMENT_DESCRIPTION_LENGTH : false;
    }

    public boolean appointmentCommentTooLong(Appointment appointment) {
        return appointment.getComment() != null ? appointment.getComment().length() > APPOINTMENT_COMMENT_LENGTH : false;
    }

    public List<Appointment> filterToReturnFutureAppointments(List<Appointment> appointment) {
        LocalDate localDate = LocalDate.now();
        List<Appointment> appointmentsFiltered = new ArrayList<>();

        //convert it to Date
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (int i = 0; i < appointment.size(); i++) {
            if (appointment.get(i).getStart().after(date)) {
                appointmentsFiltered.add(appointment.get(i));
            }

        }
        return appointmentsFiltered;

    }

    /**
     * 
     * @param undeclaredExtensions 
     */
    public void validateAppointmentExtensions(List<Extension> undeclaredExtensions) {
        List<String> extensionURLs = undeclaredExtensions.stream().map(Extension::getUrl).collect(Collectors.toList());

        extensionURLs.remove(SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON);
        extensionURLs.remove(SystemURL.SD_CC_APPOINTMENT_CREATED);
        extensionURLs.remove(SystemURL.SD_CC_APPOINTMENT_BOOKINGORG);
        extensionURLs.remove(SystemURL.SD_EXTENSION_GPC_DELIVERY_CHANNEL);
        extensionURLs.remove(SystemURL.SD_EXTENSION_GPC_PRACTITIONER_ROLE);

        if (!extensionURLs.isEmpty()) {
            throwInvalidResource("Invalid/multiple appointment extensions found. The following are in excess or invalid: "
                    + extensionURLs.stream().collect(Collectors.joining(", ")));
        }

        List<String> invalidCodes = new ArrayList<>();
        for (Extension ue : undeclaredExtensions) {

            if (ue.getUrl().equals(SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON)) {

                IBaseDatatype cancellationReason = ue.getValue();
                if (cancellationReason.isEmpty()) {
                    invalidCodes.add("Cancellation Reason is missing.");
                }
            }
        }

        if (!invalidCodes.isEmpty()) {
            throwInvalidResource("Invalid appointment extension codes: "
                    + invalidCodes.stream().collect(Collectors.joining(", ")));
        }
    }

    public void validateParticipantStatus(ParticipationStatus participationStatus,
            Enumeration<ParticipationStatus> enumeration, Enumeration<ParticipationStatus> enumeration2) {

        Boolean validStatus = false;
        String participantStatusErr = "is a requirement but is missing.";

        if (participationStatus != null) {
            participantStatusErr = String.format("%s is not a valid ParticipationStatus code", participationStatus);
            EnumSet<ParticipationStatus> statusList = EnumSet.allOf(ParticipationStatus.class);
            validStatus = statusList.contains(enumeration.getValue());
        }

        if (!validStatus) {
            throwInvalidResource(String.format("Appointment Participant %s Status %s", enumeration2, participantStatusErr));
        }
    }

    /**
     * This method is effectively disabled since we dont check against value sets 
     * This was experimental code that never worked correctly.
     * @param participantType
     * @return 
     */
    public Boolean validateParticipantType(CodeableConcept participantType) {

        Boolean hasCode = !participantType.isEmpty();
        Coding code = participantType.getCodingFirstRep();
        Boolean isValid = !hasCode;

        if (hasCode) {

            // isValid = valueSetValidator.validateCode(code);
            isValid = true;

            if (!isValid) {
                throwInvalidResource(MessageFormat.format(
                        "Invalid Participant Type Code. Code: {0} [Display: {1}, System:{2}]",
                        code.getCode(), code.getDisplay(), code.getSystem()));
            }
        }

        return isValid;
    }

    /**
     * check the resource exists 
     * @param participantActor
     */
    public void validateParticipantActor(Reference participantActor) {

        String resourcePart = participantActor.getReference().replaceFirst("^(.*)/.*$", "$1");
        idPart = participantActor.getReference().replaceFirst("^.*/(.*)$", "$1");

        Boolean participantFailedSearch = false;

        switch (resourcePart) {
            case "Patient":
                PatientDetails patient = patientSearch.findPatientByInternalID(idPart);
                participantFailedSearch = (patient == null);
                break;
            case "Practitioner":
                PractitionerDetails practitioner = practitionerSearch.findPractitionerDetails(idPart);
                participantFailedSearch = (practitioner == null);
                break;
            case "Location":
                LocationDetails location = locationSearch.findLocationById(idPart);
                participantFailedSearch = (location == null);
                break;
            default:
                System.err.println("Unhandled resourcePart " + resourcePart);
        }

        if (participantFailedSearch) {
            throwInvalidResource(String.format("%s resource reference %s is not a valid resource", resourcePart, idPart));
        }

    }

    public void validateBookingOrganizationValuesArePresent(Organization bookingOrgRes) {
        VC.execute(new VC[]{
            new VC(() -> bookingOrgRes.getName() == null, () -> "Booking organization Name is null"),
            // can't happen for valid fhir
            new VC(() -> bookingOrgRes.getName().isEmpty(), () -> "Booking organization Name is empty"),
            new VC(() -> bookingOrgRes.getTelecomFirstRep().getValue() == null, () -> "Booking organization Telecom is null"),
            // can't happen for valid fhir
            new VC(() -> bookingOrgRes.getTelecomFirstRep().getValue().isEmpty(), () -> "Booking organization Telecom is empty"),}
        );
    }

    /**
     * @return the extracted id part of the reference
     */
    public String getId() {
        return idPart;
    }
}
