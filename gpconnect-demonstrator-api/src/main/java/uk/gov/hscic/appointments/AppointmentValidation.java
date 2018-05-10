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
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.validators.ValueSetValidator;
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

	@Autowired
	private ValueSetValidator valueSetValidator;
	
	public boolean appointmentDescriptionTooLong(Appointment appointment) {
		if (appointment.getDescription().length() >= 600) {
			return true;
		} else {
			return false;
		}
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

	public void validateAppointmentExtensions(List<Extension> undeclaredExtensions) {
		List<String> extensionURLs = undeclaredExtensions.stream().map(Extension::getUrl).collect(Collectors.toList());

		extensionURLs.remove(SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON);
		extensionURLs.remove(SystemURL.SD_CC_APPOINTMENT_CREATED);
		extensionURLs.remove(SystemURL.SD_CC_APPOINTMENT_BOOKINGORG);

		if (!extensionURLs.isEmpty()) {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new UnprocessableEntityException(
							"Invalid/multiple appointment extensions found. The following are in excess or invalid: "
									+ extensionURLs.stream().collect(Collectors.joining(", "))),
					SystemCode.INVALID_RESOURCE, IssueType.INVALID);
		}

		List<String> invalidCodes = new ArrayList<>();
		for (Extension ue : undeclaredExtensions) {

			if (ue.getUrl().equals(SystemURL.SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON)) {

				IBaseDatatype cancellationReason = ue.getValue();
				if (cancellationReason.isEmpty()) {
					invalidCodes.add("Cancellation Reason is missing.");
				}
				continue;
			}

			IBaseDatatype ueValue = ue.getValue();
			if (null != ueValue && ueValue.getClass().getSimpleName().equals("CodeableConcept")) {
				CodeableConcept codeConc = (CodeableConcept) ueValue;
				Coding code = codeConc.getCodingFirstRep();

				Boolean isValid = valueSetValidator.validateCode(code);

				if (isValid == false) {
					invalidCodes.add(MessageFormat.format("Code: {0} [Display: {1}, System: {2}]", code.getCode(),
							code.getDisplay(), code.getSystem()));
				}
			}
		}

		if (!invalidCodes.isEmpty()) {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new UnprocessableEntityException("Invalid appointment extension codes: "
							+ invalidCodes.stream().collect(Collectors.joining(", "))),
					SystemCode.INVALID_RESOURCE, IssueType.INVALID);
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
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new UnprocessableEntityException(
							String.format("Appointment Participant %s Status %s", enumeration2, participantStatusErr)),
					SystemCode.INVALID_RESOURCE, IssueType.INVALID);
		}
	}

	public Boolean validateParticipantType(CodeableConcept participantType) {

		Boolean hasCode = !participantType.isEmpty();
		Coding code = participantType.getCodingFirstRep();
		Boolean isValid = !hasCode;

		if (hasCode) {

			// isValid = valueSetValidator.validateCode(code);
			isValid = true;

			if (!isValid) {
				throw OperationOutcomeFactory
						.buildOperationOutcomeException(
								new UnprocessableEntityException(MessageFormat.format(
										"Invalid Participant Type Code. Code: {0} [Display: {1}, System:{2}]",
										code.getCode(), code.getDisplay(), code.getSystem())),
								SystemCode.INVALID_RESOURCE, IssueType.INVALID);
			}
		}

		return isValid;
	}
	
	public void validateParticipantActor(Reference participantActor) {

		Reference actorRef = participantActor;
		String resourcePart = actorRef.getReference().toString();
		String idPart = actorRef.getId();

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
		}

		if (participantFailedSearch) {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new UnprocessableEntityException(
							String.format("%s resource reference is not a valid resource", resourcePart)),
					SystemCode.INVALID_RESOURCE, IssueType.INVALID);
		}

	}
	
	public void validateBookingOrganizationValuesArePresent(Organization bookingOrgRes) {
		if (bookingOrgRes.getName() == null || bookingOrgRes.getName().isEmpty()) {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new UnprocessableEntityException(String.format("Booking organization Name is null/empty")),
					SystemCode.INVALID_RESOURCE, IssueType.INVALID);

		}

		if (bookingOrgRes.getTelecomFirstRep().getValue() == null
				|| bookingOrgRes.getTelecomFirstRep().getValue().isEmpty()) {
			throw OperationOutcomeFactory.buildOperationOutcomeException(
					new UnprocessableEntityException(String.format("Booking organization Telecom is null/empty")),
					SystemCode.INVALID_RESOURCE, IssueType.INVALID);

		}

	}


}
