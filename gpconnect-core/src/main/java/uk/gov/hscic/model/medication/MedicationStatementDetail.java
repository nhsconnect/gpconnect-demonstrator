package uk.gov.hscic.model.medication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicationStatementDetail {

	private Long id;
	private Date lastIssueDate;
	private Long medicationRequestPlanId;
	private Long encounterId;
	private String statusCode;
	private String statusDisplay;
	private Long medicationId;
	private Date startDate;
	private Date endDate;
	private Date dateAsserted;
	private Long patientId;
	private String takenCode;
	private String takenDisplay;
	private List<MedicationReasonCode> reasonCodes;
	private List<MedicationReasonReference> reasonReferences;
	private List<MedicationNote> notes;
	private String dosageText;
	private String dosagePatientInstruction;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getLastIssueDate() {
		return lastIssueDate;
	}
	
	public void setLastIssueDate(Date lastIssueDate) {
		this.lastIssueDate = lastIssueDate;
	}
	
	public Long getMedicationRequestPlanId() {
		return medicationRequestPlanId;
	}
	
	public void setMedicationRequestPlanId(Long medicationRequestPlanId) {
		this.medicationRequestPlanId = medicationRequestPlanId;
	}
	
	public Long getEncounterId() {
		return encounterId;
	}
	
	public void setEncounterId(Long encounterId) {
		this.encounterId = encounterId;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getStatusDisplay() {
		return statusDisplay;
	}
	
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	
	public Long getMedicationId() {
		return medicationId;
	}
	
	public void setMedicationId(Long medicationId) {
		this.medicationId = medicationId;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getDateAsserted() {
		return dateAsserted;
	}
	
	public void setDateAsserted(Date dateAsserted) {
		this.dateAsserted = dateAsserted;
	}
	
	public Long getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	
	public String getTakenCode() {
		return takenCode;
	}
	
	public void setTakenCode(String takenCode) {
		this.takenCode = takenCode;
	}
	
	public String getTakenDisplay() {
		return takenDisplay;
	}
	
	public void setTakenDisplay(String takenDisplay) {
		this.takenDisplay = takenDisplay;
	}
	
	public List<MedicationReasonCode> getReasonCodes() {
		return reasonCodes;
	}
	
	public void setReasonCodes(List<MedicationReasonCode> reasonCodes) {
		this.reasonCodes = reasonCodes;
	}
	
	public void addReasonCode(MedicationReasonCode reasonCode) {
		if(this.reasonCodes == null) {
			this.reasonCodes = new ArrayList<>();
		}
		this.reasonCodes.add(reasonCode);
	}
	
	public List<MedicationReasonReference> getReasonReferences() {
		return reasonReferences;
	}
	
	public void setReasonReferences(List<MedicationReasonReference> reasonReferences) {
		this.reasonReferences = reasonReferences;
	}
	
	public void addReasonReferences(MedicationReasonReference reasonReference) {
		if(this.reasonReferences == null) {
			this.reasonReferences = new ArrayList<>();
		}
		this.reasonReferences.add(reasonReference);
	}
	
	public List<MedicationNote> getNotes() {
		return notes;
	}
	
	public void setNotes(List<MedicationNote> notes) {
		this.notes = notes;
	}
	
	public void addNote(MedicationNote note) {
		if(this.notes == null) {
			this.notes = new ArrayList<>();
		}
		this.notes.add(note);
	}
	
	public String getDosageText() {
		return dosageText;
	}
	
	public void setDosageText(String dosageText) {
		this.dosageText = dosageText;
	}
	
	public String getDosagePatientInstruction() {
		return dosagePatientInstruction;
	}
	
	public void setDosagePatientInstruction(String dosagePatientInstruction) {
		this.dosagePatientInstruction = dosagePatientInstruction;
	}

	
}
