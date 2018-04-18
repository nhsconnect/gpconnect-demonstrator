package uk.gov.hscic.medication.statement;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementTaken;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.stereotype.Component;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.model.medication.MedicationStatementDetail;

@Component
public class MedicationStatementResourceProvider {

	public MedicationStatement getMedicationStatementResource(MedicationStatementDetail statementDetail) {
		MedicationStatement medicationStatement = new MedicationStatement();

		medicationStatement.setId(new IdType(statementDetail.getId()));
		medicationStatement.setMeta(new Meta().addProfile(SystemURL.SD_GPC_MEDICATION_STATEMENT)
				.setVersionId(String.valueOf(statementDetail.getLastUpdated().getTime())));
		medicationStatement.addExtension(new Extension(SystemURL.SD_CC_EXT_MEDICATION_STATEMENT_LAST_ISSUE, 
				new DateTimeType(statementDetail.getLastIssueDate(), TemporalPrecisionEnum.DAY)));
		if(statementDetail.getMedicationRequestPlanId() != null)
			medicationStatement.addBasedOn(new Reference(new IdType("MedicationRequest", statementDetail.getMedicationRequestPlanId())));
		if(statementDetail.getEncounterId() != null)
			medicationStatement.setContext(new Reference(new IdType("Encounter", statementDetail.getEncounterId())));
		try {
			medicationStatement.setStatus(MedicationStatementStatus.fromCode(statementDetail.getStatusCode()));
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(statementDetail.getMedicationId() != null)
			medicationStatement.setMedication(new Reference(new IdType("Medication", statementDetail.getMedicationId())));
		medicationStatement.setEffective(new Period().setStart(statementDetail.getStartDate()).setEnd(statementDetail.getEndDate()));
		medicationStatement.setDateAsserted(statementDetail.getDateAsserted());
		if(statementDetail.getPatientId() != null)
			medicationStatement.setSubject(new Reference(new IdType("Patient", statementDetail.getPatientId())));
		try {
			medicationStatement.setTaken(statementDetail.getTakenCode() != null ? 
					MedicationStatementTaken.fromCode(statementDetail.getTakenCode()) : MedicationStatementTaken.UNK);
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setReasonCodes(medicationStatement, statementDetail);
		setReasonReferences(medicationStatement, statementDetail);
		setNotes(medicationStatement, statementDetail);
		
		medicationStatement.addDosage(new Dosage()
				.setText(statementDetail.getDosageText())
				.setPatientInstruction(statementDetail.getDosagePatientInstruction()));
		
		return medicationStatement;
	}

	private void setReasonCodes(MedicationStatement medicationStatement, MedicationStatementDetail statementDetail) {
		statementDetail.getReasonCodes().forEach(rc -> {
			Coding coding = new Coding(SystemURL.VS_CONDITION_CODE, rc.getCode(), rc.getDisplay());
			medicationStatement.addReasonCode(new CodeableConcept().addCoding(coding));
		});			
	}

	private void setReasonReferences(MedicationStatement medicationStatement, MedicationStatementDetail statementDetail) {
		statementDetail.getReasonReferences().forEach(rr -> {
			if(rr.getReferenceUrl().equals(SystemURL.SD_GPC_OBSERVATION)) {
				medicationStatement.addReasonReference(new Reference(new IdType("Observation", rr.getReferenceId())));
			} else if (rr.getReferenceUrl().equals(SystemURL.SD_GPC_CONDITION)) {
				medicationStatement.addReasonReference(new Reference(new IdType("Condition", rr.getReferenceId())));
			}
		});			
	}
	
	private void setNotes(MedicationStatement medicationStatement, MedicationStatementDetail statementDetail) {
		statementDetail.getNotes().forEach(note -> {
			Annotation annotation = new Annotation();
			annotation.setId(String.valueOf(note.getId()));
			annotation.setText(note.getNote());
			annotation.setTime(note.getDateWritten());
			if(note.getAuthorReferenceUrl().equals(SystemURL.SD_GPC_PRACTITIONER)) {
				annotation.setAuthor(new Reference(new IdType("Practitioner", note.getAuthorId())));
			} else if (note.getAuthorReferenceUrl().equals(SystemURL.SD_GPC_PATIENT)) {
				annotation.setAuthor(new Reference(new IdType("Patient", note.getAuthorId())));
			}
			medicationStatement.addNote(annotation);
		});
	}
}
