package uk.gov.hscic.model.patient;

import java.util.Date;

public class StructuredAllergyIntolerance {

	private Long Id;
	private Date endDate;
	private String endReason;
	private String note;
	private String reactionDescription;
	private String clinicalStatus;

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

}
