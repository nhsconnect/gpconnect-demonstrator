package uk.gov.hscic.medication.requests;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Duration;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestDispenseRequestComponent;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestIntent;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestRequesterComponent;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestStatus;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.PositiveIntType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.SystemURL;
import uk.gov.hscic.medication.request.MedicationRequestEntityToDetailTransformer;
import uk.gov.hscic.medication.request.MedicationRequestRepository;
import uk.gov.hscic.model.medication.MedicationRequestDetail;

@Component
public class MedicationRequestResourceProvider {
	
	@Autowired
	private MedicationRequestRepository medicationRequestRepository;
	
	@Autowired
	private MedicationRequestEntityToDetailTransformer medicationRequestEntityToDetailTransformer;
	
	public MedicationRequest getMedicationRequestPlanResource(Long medicationRequestId) {
		MedicationRequestDetail requestDetail = medicationRequestEntityToDetailTransformer
				.transform(medicationRequestRepository.findOne(medicationRequestId));
		
		MedicationRequest medicationRequest = getMedicationRequestFromDetail(requestDetail);
		
		return medicationRequest;
	}
	
	public List<MedicationRequest> getMedicationRequestOrderResources(String groupIdentifier) {
		return medicationRequestRepository.findByIntentCodeAndGroupIdentifier("order", groupIdentifier)
					.stream().map(requestEntity -> medicationRequestEntityToDetailTransformer.transform(requestEntity))
					.sorted(Comparator.comparing(requestDetail -> ((MedicationRequestDetail) requestDetail).getDispenseRequestStartDate()))
					.map(requestDetail -> getMedicationRequestFromDetail(requestDetail))
					.collect(Collectors.toList());
	}

	private MedicationRequest getMedicationRequestFromDetail(MedicationRequestDetail requestDetail) {
		MedicationRequest medicationRequest = new MedicationRequest();
		
		medicationRequest.setId(new IdType(requestDetail.getId()));
		medicationRequest.setMeta(new Meta().addProfile(SystemURL.SD_GPC_MEDICATION_REQUEST));
		setBasedOnReferences(medicationRequest, requestDetail);			
		medicationRequest.setGroupIdentifier(new Identifier().setValue(requestDetail.getGroupIdentifier()));
		try {
			medicationRequest.setStatus(MedicationRequestStatus.fromCode(requestDetail.getStatusCode()));
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			medicationRequest.setIntent(MedicationRequestIntent.fromCode(requestDetail.getIntentCode()));
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(requestDetail.getMedicationId() != null)
			medicationRequest.setMedication(new Reference(new IdType("Medication", requestDetail.getMedicationId())));
		if(requestDetail.getPatientId() != null)
			medicationRequest.setSubject(new Reference(new IdType("Patient", requestDetail.getPatientId())));
		if(requestDetail.getEncounterId() != null)
			medicationRequest.setContext(new Reference(new IdType("Encounter", requestDetail.getEncounterId())));
		medicationRequest.setAuthoredOn(requestDetail.getAuthoredOn());
		medicationRequest.setRequester(getRequesterComponent(requestDetail));
		if(requestDetail.getAuthorisingPractitionerId() != null)
			medicationRequest.setRecorder(new Reference(new IdType("Practitioner", requestDetail.getAuthorisingPractitionerId())));
		setReasonCodes(medicationRequest, requestDetail);
		setReasonReferences(medicationRequest, requestDetail);
		setNotes(medicationRequest, requestDetail);
		medicationRequest.addDosageInstruction(new Dosage()
				.setText(requestDetail.getDosageText())
				.setPatientInstruction(requestDetail.getDosageInstructions()));
		medicationRequest.setDispenseRequest(getDispenseRequestComponent(requestDetail));
		if(requestDetail.getPriorMedicationRequestId() != null)
			medicationRequest.setPriorPrescription(new Reference(new IdType("MedicationRequest", requestDetail.getPriorMedicationRequestId())));
		setRepeatInformation(medicationRequest, requestDetail);
		setPrescriptionType(medicationRequest, requestDetail);
		setStatusReason(medicationRequest, requestDetail);
				
		return medicationRequest;
	}

	private void setPrescriptionType(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		Coding prescriptionTypeCoding = new Coding(SystemURL.VS_CC_PRESCRIPTION_TYPE, requestDetail.getPrescriptionTypeCode(), requestDetail.getPrescriptionTypeDisplay());
		Extension prescriptionTypeExtension = new Extension(SystemURL.SD_CC_EXT_MEDICATION_PRESCRIPTION_TYPE, new CodeableConcept().addCoding(prescriptionTypeCoding));
		medicationRequest.addExtension(prescriptionTypeExtension);
	}

	private void setStatusReason(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		if(medicationRequest.getStatus().equals(MedicationRequestStatus.STOPPED)) {
			Extension statusReasonExtension = new Extension(SystemURL.SD_CC_EXT_MEDICATION_STATUS_REASON);
			statusReasonExtension.addExtension(new Extension("statusReason", new StringType(requestDetail.getStatusReason())));
		}
	}

	private void setBasedOnReferences(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		if(requestDetail.getPrescriptionTypeCode().equals("repeat")) {
			requestDetail.getBasedOnReferences().forEach(reference -> {
				if(reference.getReferenceUrl().equals(SystemURL.SD_GPC_MEDICATION_REQUEST)) {
					medicationRequest.addBasedOn(new Reference(new IdType("MedicationRequest", reference.getReferenceId())));
				}
				// TODO reference type of care plan, procedure request or referral request;
			});
		}
	}

	private void setReasonCodes(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		requestDetail.getReasonCodes().forEach(rc -> {
			Coding coding = new Coding(SystemURL.VS_CONDITION_CODE, rc.getCode(), rc.getDisplay());
			medicationRequest.addReasonCode(new CodeableConcept().addCoding(coding));
		});
	}

	private void setReasonReferences(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		requestDetail.getReasonReferences().forEach(rr -> {
			if(rr.getReferenceUrl().equals(SystemURL.SD_GPC_OBSERVATION)) {
				medicationRequest.addReasonReference(new Reference(new IdType("Observation", rr.getReferenceId())));
			} else if (rr.getReferenceUrl().equals(SystemURL.SD_GPC_CONDITION)) {
				medicationRequest.addReasonReference(new Reference(new IdType("Condition", rr.getReferenceId())));
			}
		});
	}

	private void setNotes(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		requestDetail.getNotes().forEach(note -> {
			Annotation annotation = new Annotation();
			annotation.setId(String.valueOf(note.getId()));
			annotation.setText(note.getNote());
			annotation.setTime(note.getDateWritten());
			if(note.getAuthorReferenceUrl().equals(SystemURL.SD_GPC_PRACTITIONER)) {
				annotation.setAuthor(new Reference(new IdType("Practitioner", note.getAuthorId())));
			} else if (note.getAuthorReferenceUrl().equals(SystemURL.SD_GPC_PATIENT)) {
				annotation.setAuthor(new Reference(new IdType("Patient", note.getAuthorId())));
			} else {
				// TODO Related Person reference
			}
			medicationRequest.addNote(annotation);
		});
	}

	private void setRepeatInformation(MedicationRequest medicationRequest, MedicationRequestDetail requestDetail) {
		if(requestDetail.getPrescriptionTypeCode().equals("repeat")) {
			Extension repeatInformationExtension = new Extension(SystemURL.SD_CC_EXT_MEDICATION_REPEAT_INFORMATION);
			repeatInformationExtension.addExtension(new Extension("numberOfRepeatPrescriptionsAllowed", new PositiveIntType(requestDetail.getNumberOfRepeatPrescriptionsAllowed())));
			repeatInformationExtension.addExtension(new Extension("numberOfRepeatPrescriptionsIssued", new PositiveIntType(requestDetail.getNumberOfRepeatPrescriptionsIssued())));
			repeatInformationExtension.addExtension(new Extension("authorisationExpiryDate", new DateTimeType(requestDetail.getAuthorisationExpiryDate())));
			
			medicationRequest.addExtension(repeatInformationExtension);
		}
	}

	private MedicationRequestDispenseRequestComponent getDispenseRequestComponent(
			MedicationRequestDetail requestDetail) {
		MedicationRequestDispenseRequestComponent dispenseRequest = new MedicationRequestDispenseRequestComponent();
		
		Period period = new Period().setStart(requestDetail.getDispenseRequestStartDate());
		if(requestDetail.getDispenseRequestEndDate() != null) {
			period.setEnd(requestDetail.getDispenseRequestEndDate());
		}
		dispenseRequest.setValidityPeriod(period);
		
		setDispenseQuantity(dispenseRequest, requestDetail);
		
		Duration duration = new Duration();
		duration.setSystem(SystemURL.CS_UNITS_OF_MEASURE);
		duration.setCode("d");
		duration.setValue(requestDetail.getExpectedSupplyDuration());
		duration.setUnit("day");
		dispenseRequest.setExpectedSupplyDuration(duration);
		
		dispenseRequest.setPerformer(new Reference(new IdType("Organization",requestDetail.getDispenseRequestOrganizationId())));
	
		return dispenseRequest;
	}

	private void setDispenseQuantity(MedicationRequestDispenseRequestComponent dispenseRequest,
			MedicationRequestDetail requestDetail) {
		SimpleQuantity quantity = new SimpleQuantity();
		if(requestDetail.getDispenseQuantityText() == null) {
			quantity.setValue(requestDetail.getDispenseQuantityValue());
			quantity.setUnit(requestDetail.getDispenseQuantityUnit());
		} else {
			quantity.addExtension(new Extension(SystemURL.SD_CC_EXT_MEDICATION_QUANTITY_TEXT, new StringType(requestDetail.getDispenseQuantityText())));	
		}
		dispenseRequest.setQuantity(quantity);
	}

	private MedicationRequestRequesterComponent getRequesterComponent(MedicationRequestDetail requestDetail) {
		MedicationRequestRequesterComponent requesterComponent = new MedicationRequestRequesterComponent();
		switch(requestDetail.getRequesterUrl()) {
		case(SystemURL.SD_GPC_PATIENT):
			requesterComponent.setAgent(new Reference(new IdType("Patient", requestDetail.getRequesterId())));
			break;
		case(SystemURL.SD_EXTENSION_GPC_PRACTITIONER):
			requesterComponent.setAgent(new Reference(new IdType("Practitioner", requestDetail.getRequesterId())));
			break;
		case(SystemURL.SD_GPC_ORGANIZATION):
			requesterComponent.setAgent(new Reference(new IdType("Organization", requestDetail.getRequesterId())));
			break;
		// TODO case (related person/device)
		default:
			break;
		}
		requesterComponent.setOnBehalfOf(new Reference(new IdType("Organization", requestDetail.getDispenseRequestOrganizationId())));
		
		return requesterComponent;
	}
}
