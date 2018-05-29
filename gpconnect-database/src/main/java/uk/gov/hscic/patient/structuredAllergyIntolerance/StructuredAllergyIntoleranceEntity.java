package uk.gov.hscic.patient.structuredAllergyIntolerance;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "allergyIntolerance")
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
	
	@Column(name = "verificationStatus")
	private String verificationStatus;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "patientRef")
	private String patientRef;
	
	@Column(name = "onSetDateTime")
	private Date onSetDateTime;
	
	@Column(name = "assertedDate")
	private Date assertedDate;
	
	@Column(name = "coding")
	private String coding;
	
	@Column(name = "display")
	private String display;
	
	@Column(name = "manCoding")
	private String manifestationCoding;
	
	@Column(name = "manDisplay")
	private String manifestationDisplay;

	@Column(name = "recorder")
	private String recorder;

	@Column(name = "warningCode")
	private String warningCode;

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

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getManifestationCoding() {
		return manifestationCoding;
	}

	public void setManifestationCoding(String manifestationCoding) {
		this.manifestationCoding = manifestationCoding;
	}

	public String getManifestationDisplay() {
		return manifestationDisplay;
	}

	public void setManifestationDisplay(String manifestationDisplay) {
		this.manifestationDisplay = manifestationDisplay;
	}

	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	public String getWarningCode() {
		return warningCode;
	}

	public void setWarningCode(String warningCode) {
		this.warningCode = warningCode;
	}
}
