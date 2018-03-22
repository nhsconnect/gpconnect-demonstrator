package uk.gov.hscic.model.patient;

import java.util.Date;

public class StructuredAllergyIntolerance {

	private Long Id;
	private Date endDate;
	private String endReason;
	private String note;
	private String reactionDescription;
	private String clinicalStatus;
	private String verificationStatus;
	private String category;
	private String patientRef;
	private Date onSetDateTime;
	private Date assertedDate;

	public void setId(Long id) {
		Id = id;
	}

	public Long getId() {
		return Id;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndReason() {
		return endReason;
	}

	public void setEndReason(String endReason) {
		this.endReason = endReason;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReactionDescription() {
		return reactionDescription;
	}

	public void setReactionDescription(String reactionDescription) {
		this.reactionDescription = reactionDescription;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPatientRef() {
		return patientRef;
	}

	public void setPatientRef(String patientRef) {
		this.patientRef = patientRef;
	}

	public Date getOnSetDateTime() {
		return onSetDateTime;
	}

	public void setOnSetDateTime(Date onSetDateTime) {
		this.onSetDateTime = onSetDateTime;
	}

	public Date getAssertedDate() {
		return assertedDate;
	}

	public void setAssertedDate(Date assertedDate) {
		this.assertedDate = assertedDate;
	}
	
	

}
