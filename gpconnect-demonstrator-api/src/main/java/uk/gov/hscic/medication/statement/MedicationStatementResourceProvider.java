package uk.gov.hscic.medication.statement;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.util.ArrayList;
import java.util.Date;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus;
import org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementTaken;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.stereotype.Component;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.model.medication.MedicationStatementDetail;

import java.util.List;
import static uk.gov.hscic.SystemConstants.NO_INFORMATION_AVAILABLE;

@Component
public class MedicationStatementResourceProvider {
    
    public MedicationStatement getMedicationStatementResource(MedicationStatementDetail statementDetail) {
        MedicationStatement medicationStatement = new MedicationStatement();

        medicationStatement.setId(statementDetail.getId().toString());
        List<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier()
                .setSystem("https://fhir.nhs.uk/Id/cross-care-setting-identifier")
                .setValue(statementDetail.getGuid());
        identifiers.add(identifier);
        medicationStatement.setIdentifier(identifiers);

        medicationStatement.setMeta(new Meta().addProfile(SystemURL.SD_GPC_MEDICATION_STATEMENT));

        medicationStatement.addExtension(new Extension(SystemURL.SD_CC_EXT_MEDICATION_STATEMENT_LAST_ISSUE,
                new DateTimeType(statementDetail.getLastIssueDate(), TemporalPrecisionEnum.DAY)));

        if (statementDetail.getMedicationRequestPlanId() != null) {
            medicationStatement.addBasedOn(new Reference(new IdType("MedicationRequest", statementDetail.getMedicationRequestPlanId())));
        }

        try {
            medicationStatement.setStatus(MedicationStatementStatus.fromCode(statementDetail.getStatusCode()));
        } catch (FHIRException e) {
            throw new UnprocessableEntityException(e.getMessage());
        }

        if (statementDetail.getMedicationId() != null) {
            medicationStatement.setMedication(new Reference(new IdType("Medication", statementDetail.getMedicationId())));
        }

        medicationStatement.setEffective(new Period().setStart(statementDetail.getStartDate()).setEnd(statementDetail.getEndDate()));

        medicationStatement.setDateAsserted(statementDetail.getDateAsserted());

        if (statementDetail.getPatientId() != null)
            medicationStatement.setSubject(new Reference(new IdType("Patient", statementDetail.getPatientId())));
        try {
            medicationStatement.setTaken(statementDetail.getTakenCode() != null ?
                    MedicationStatementTaken.fromCode(statementDetail.getTakenCode()) : MedicationStatementTaken.UNK);
        } catch (FHIRException e) {
            throw new UnprocessableEntityException(e.getMessage());
        }

        setReasonCodes(medicationStatement, statementDetail);
        setNotes(medicationStatement, statementDetail);

        String dosageText = statementDetail.getDosageText();
        medicationStatement.addDosage(new Dosage()
                .setText(dosageText == null || dosageText.trim().isEmpty() ? NO_INFORMATION_AVAILABLE : dosageText)
                .setPatientInstruction(statementDetail.getDosagePatientInstruction()));

        String prescribingAgency = statementDetail.getPrescribingAgency();
        if (prescribingAgency != null && !prescribingAgency.trim().isEmpty()) {

        	String prescribingAgencyDisplay = "";
        	if (prescribingAgency.equalsIgnoreCase("prescribed-at-gp-practice")) {
        		prescribingAgencyDisplay = "Prescribed at GP practice";
        	} else if (prescribingAgency.equalsIgnoreCase("prescribed-by-another-organisation")) {
        		prescribingAgencyDisplay = "Prescribed by another organisation";
        	}
            Coding coding = new Coding(SystemURL.CS_CC_PRESCRIBING_AGENCY_STU3, prescribingAgency, prescribingAgencyDisplay);

            CodeableConcept codeableConcept = new CodeableConcept().addCoding(coding);
            medicationStatement.addExtension(new Extension(SystemURL.SD_EXTENSION_CC_PRESCRIBING_AGENCY, codeableConcept));
        }
        
        // #281 1.2.5 add dosageLastChanged
        Date dosageLastChanged = statementDetail.getDosageLastChanged();
        if (dosageLastChanged != null) {
            medicationStatement.addExtension(new Extension(SystemURL.SD_EXTENSION_CC_DOSAGE_LAST_CHANGED, new DateTimeType(dosageLastChanged)));
        }
        
        return medicationStatement;
    }

    private void setReasonCodes(MedicationStatement medicationStatement, MedicationStatementDetail statementDetail) {
        statementDetail.getReasonCodes().forEach(rc -> {
            Coding coding = new Coding(SystemURL.VS_CONDITION_CODE, rc.getCode(), rc.getDisplay());
            medicationStatement.addReasonCode(new CodeableConcept().addCoding(coding));
        });
    }

    private void setNotes(MedicationStatement medicationStatement, MedicationStatementDetail statementDetail) {
        statementDetail.getNotes().forEach(note -> {
            Annotation annotation = new Annotation();
            annotation.setId(String.valueOf(note.getId()));
            annotation.setText(note.getNote());
            annotation.setTime(note.getDateWritten());
            if (note.getAuthorReferenceUrl().equals(SystemURL.SD_GPC_PRACTITIONER)) {
                annotation.setAuthor(new Reference(new IdType("Practitioner", note.getAuthorId())));
            } else if (note.getAuthorReferenceUrl().equals(SystemURL.SD_GPC_PATIENT)) {
                annotation.setAuthor(new Reference(new IdType("Patient", note.getAuthorId())));
            }
            medicationStatement.addNote(annotation);
        });
    }
}
