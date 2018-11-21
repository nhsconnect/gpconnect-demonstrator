package uk.gov.hscic.medication.request;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;
import uk.gov.hscic.model.medication.*;

@Component
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
			note.setDateWritten(n.getDateWritten());
			note.setAuthorReferenceUrl(n.getAuthorReferenceUrl());
			note.setAuthorId(n.getAuthorId());
			note.setNote(n.getNoteText());
			requestDetail.addNote(note);
		});
		
		requestDetail.setDosageText(requestEntity.getDosageText());
		requestDetail.setDosageInstructions(requestEntity.getDosageInstruction());
		requestDetail.setDispenseRequestStartDate(requestEntity.getDispenseRequestStartDate());
		requestDetail.setDispenseRequestEndDate(requestEntity.getDispenseRequestEndDate());
		if(requestEntity.getDispenseQuantityValue() != null) {
			requestDetail.setDispenseQuantityValue(Double.valueOf(requestEntity.getDispenseQuantityValue()));
		}
		requestDetail.setDispenseQuantityUnit(requestEntity.getDispenseQuantityUnit());
		requestDetail.setDispenseQuantityText(requestEntity.getDispenseQuantityText());
		if(requestEntity.getExpectedSupplyDuration() != null) {
			requestDetail.setExpectedSupplyDuration(Integer.valueOf(requestEntity.getExpectedSupplyDuration()));
		}
		requestDetail.setDispenseRequestOrganizationId(requestEntity.getDispenseRequestOrganizationId());
		requestDetail.setPriorMedicationRequestId(requestEntity.getPriorMedicationRequestId());
		requestDetail.setNumberOfRepeatPrescriptionsAllowed(requestEntity.getNumberOfRepeatPrescriptionsAllowed());
		requestDetail.setNumberOfRepeatPrescriptionsIssued(requestEntity.getNumberOfRepeatPrescriptionsIssued());
		requestDetail.setAuthorisationExpiryDate(requestEntity.getAuthorisationExpiryDate());
		requestDetail.setPrescriptionTypeCode(requestEntity.getPrescriptionTypeCode());
		requestDetail.setPrescriptionTypeDisplay(requestEntity.getPrescriptionTypeDisplay());
		requestDetail.setStatusReason(requestEntity.getStatusReason());
		requestDetail.setStatusReasonDate(requestEntity.getStatusReasonDate());
		requestDetail.setLastUpdated(requestEntity.getLastUpdated());
        requestDetail.setGuid(requestEntity.getGuid());
		
		return requestDetail;
	}

	
}
