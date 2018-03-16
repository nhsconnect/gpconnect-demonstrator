package uk.gov.hscic.medication.request;

import org.apache.commons.collections4.Transformer;
import org.hl7.fhir.exceptions.UcumException;
import org.hl7.fhir.utilities.ucum.Decimal;

import uk.gov.hscic.model.medication.MedicationBasedOnReference;
import uk.gov.hscic.model.medication.MedicationNote;
import uk.gov.hscic.model.medication.MedicationReasonCode;
import uk.gov.hscic.model.medication.MedicationReasonReference;
import uk.gov.hscic.model.medication.MedicationRequestDetail;

public class MedicationRequestEntityToDetailTransformer implements Transformer<MedicationRequestEntity, MedicationRequestDetail>{

	@Override
	public MedicationRequestDetail transform(MedicationRequestEntity requestEntity) {
		MedicationRequestDetail requestDetail = new MedicationRequestDetail();
		requestDetail.setId(requestEntity.getId());
		
		requestEntity.getBasedOnReferences().forEach(ref -> {
			MedicationBasedOnReference basedOnReference = new MedicationBasedOnReference();
			
			basedOnReference.setId(ref.getId());
			basedOnReference.setReferenceUrl(ref.getReferenceUrl());
			basedOnReference.setReferenceId(ref.getReferenceId());
			
			requestDetail.addBasedOnReference(basedOnReference);
		});
		
		requestDetail.setGroupIdentifier(requestEntity.getGroupIdentifier());
		requestDetail.setStatusCode(requestEntity.getStatusCode());
		requestDetail.setStatusDisplay(requestEntity.getStatusDisplay());
		requestDetail.setIntentCode(requestEntity.getIntentCode());
		requestDetail.setIntentDisplay(requestEntity.getIntentDisplay());
		requestDetail.setMedicationId(requestEntity.getMedicationId());
		requestDetail.setPatientId(requestEntity.getPatientId());
		requestDetail.setEncounterId(requestEntity.getEncounterId());
		requestDetail.setAuthoredOn(requestEntity.getAuthoredOn());
		requestDetail.setRequesterUrl(requestEntity.getRequesterUrl());
		requestDetail.setRequesterId(requestEntity.getRequesterId());
		requestDetail.setAuthorisingPractitionerId(requestEntity.getAuthorisingPractitionerId());
		
		requestEntity.getReasonCodes().forEach(rc -> {
			MedicationReasonCode reasonCode = new MedicationReasonCode();
			reasonCode.setId(rc.getId());
			reasonCode.setCode(rc.getCode());
			reasonCode.setDisplay(rc.getDisplay());
			requestDetail.addReasonCode(reasonCode);
		});
		
		requestEntity.getReasonReferences().forEach(rr -> {
			MedicationReasonReference reasonReference = new MedicationReasonReference();
			reasonReference.setId(rr.getId());
			reasonReference.setReferenceUrl(rr.getReferenceUrl());
			reasonReference.setReferenceId(reasonReference.getReferenceId()); 
			requestDetail.addReasonReference(reasonReference);
		});
		
		requestEntity.getNotes().forEach(n -> {
			MedicationNote note = new MedicationNote();
			note.setId(n.getId());
			note.setNote(n.getNote());
			requestDetail.addNote(note);
		});
		
		requestDetail.setDosageText(requestEntity.getDosageText());
		requestDetail.setDosageInstructions(requestEntity.getDosageInstruction());
		requestDetail.setDispenseRequestStartDate(requestEntity.getDispenseRequestStartDate());
		requestDetail.setDispenseRequestEndDate(requestEntity.getDispenseRequestEndDate());
		try {
			requestDetail.setDispenseQuantityValue(new Decimal(requestEntity.getDispenseQuantityValue()));
		} catch (UcumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestDetail.setDispenseQuantityUnit(requestEntity.getDispenseQuantityUnit());
		requestDetail.setDispenseQuantityText(requestEntity.getDispenseQuantityText());
		try {
			requestDetail.setExpectedSupplyDuration(new Decimal(requestEntity.getExpectedSupplyDurationValue()));
		} catch (UcumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestDetail.setDispenseRequestOrganizationId(requestEntity.getDispenseRequestOrganizationId());
		requestDetail.setPriorMedicationRequestId(requestEntity.getPriorMedicationRequestId());
		requestDetail.setNumberOfRepeatPrescriptionsAllowed(requestEntity.getNumberOfRepeatPrescriptionsAllowed());
		requestDetail.setNumberOfRepeatPrescriptionsIssued(requestEntity.getNumberOfRepeatPrescriptionsIssued());
		requestDetail.setAuthorisationExpiryDate(requestEntity.getAuthorisationExpiryDate());
		requestDetail.setPrescriptionTypeCode(requestEntity.getPrescriptionTypeCode());
		requestDetail.setPrescriptionTypeDisplay(requestEntity.getPrescriptionTypeDisplay());
		requestDetail.setStatusReasonCode(requestEntity.getStatusReasonCode());
		requestDetail.setStatusReasonValue(requestEntity.getStatusReasonValue());
		requestDetail.setStatusReasonDate(requestEntity.getStatusReasonDate());
		
		return requestDetail;
	}

	
}
