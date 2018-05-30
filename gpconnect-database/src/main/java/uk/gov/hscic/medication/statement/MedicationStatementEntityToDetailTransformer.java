package uk.gov.hscic.medication.statement;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;
import uk.gov.hscic.model.medication.MedicationNote;
import uk.gov.hscic.model.medication.MedicationReasonCode;
import uk.gov.hscic.model.medication.MedicationReasonReference;
import uk.gov.hscic.model.medication.MedicationStatementDetail;

@Component
public class MedicationStatementEntityToDetailTransformer implements Transformer<MedicationStatementEntity, MedicationStatementDetail> {

	@Override
	public MedicationStatementDetail transform(MedicationStatementEntity statementEntity) {
		MedicationStatementDetail statementDetail = new MedicationStatementDetail();
		statementDetail.setId(statementEntity.getId());
		statementDetail.setLastIssueDate(statementEntity.getLastIssueDate());
		statementDetail.setMedicationRequestPlanId(statementEntity.getMedicationRequestId());
		statementDetail.setEncounterId(statementEntity.getEncounterId());
		statementDetail.setStatusCode(statementEntity.getStatusCode());
		statementDetail.setStatusDisplay(statementEntity.getStatusDisplay());
		statementDetail.setMedicationId(statementEntity.getMedicationId());
		statementDetail.setStartDate(statementEntity.getStartDate());
		statementDetail.setEndDate(statementEntity.getEndDate());
		statementDetail.setDateAsserted(statementEntity.getDateAsserted());
		statementDetail.setPatientId(statementEntity.getPatientId());
		statementDetail.setTakenCode(statementEntity.getTakenCode());
		statementDetail.setTakenDisplay(statementEntity.getTakenDisplay());
		
		statementEntity.getReasonCodes().forEach(rc -> {
			MedicationReasonCode reasonCode = new MedicationReasonCode();
			reasonCode.setId(rc.getId());
			reasonCode.setCode(rc.getCode());
			reasonCode.setDisplay(rc.getDisplay());
			statementDetail.addReasonCode(reasonCode);
		});
		
		statementEntity.getReasonReferences().forEach(rr -> {
			MedicationReasonReference reasonReference = new MedicationReasonReference();
			reasonReference.setId(rr.getId());
			reasonReference.setReferenceUrl(rr.getReferenceUrl());
			reasonReference.setReferenceId(reasonReference.getReferenceId()); 
			statementDetail.addReasonReferences(reasonReference);
		});
		
		statementEntity.getNotes().forEach(n -> {
			MedicationNote note = new MedicationNote();
			note.setId(n.getId());
			note.setAuthorReferenceUrl(n.getAuthorReferenceUrl());
			note.setAuthorId(n.getAuthorId());
			note.setNote(n.getNoteText());
			statementDetail.addNote(note);
		});
		
		statementDetail.setDosageText(statementEntity.getDosageText());
		statementDetail.setDosagePatientInstruction(statementEntity.getDosageInstruction());
		statementDetail.setLastUpdated(statementEntity.getLastUpdated());
        statementDetail.setPrescribingAgency(statementEntity.getPrescribingAgency());
        statementDetail.setGuid(statementEntity.getGuid());

        if(statementEntity.getWarningCode() !=null) {
            statementDetail.setWarningCode(statementEntity.getWarningCode());
        }

		return statementDetail;
	}

}
