package uk.gov.hscic.patient.structuredAllergyIntolerance;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "allInto")
public class StructuredAllergyIntoleranceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nhsNumber")
	private String nhsNumber;

	@Column(name = "endDate")
	private Date endDate;

	@Column(name = "endReason")
	private String endReason;

	@Column(name = "note")
	private String note;

	@Column(name = "reactionDescription")
	private String reactionDescription;
	
	@Column(name = "clinicalStatus")
	private String clinicalStatus;

	public String getNhsNumber() {
		return nhsNumber;
	}

	public void setNhsNumber(String nhsNumber) {
		this.nhsNumber = nhsNumber;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}
	
	
}
