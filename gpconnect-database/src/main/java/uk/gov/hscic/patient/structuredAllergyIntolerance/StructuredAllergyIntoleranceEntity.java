package uk.gov.hscic.patient.structuredAllergyIntolerance;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "allergyintolerance")
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
    
    @Column(name = "concept_code")
    private String conceptCode;
    
    @Column(name = "concept_display")
    private String conceptDisplay;

    @Column(name = "desc_code")
    private String descCode;
    
    @Column(name = "desc_display")
    private String descDisplay;
    
    @Column(name= "code_translation_ref")
    private String codeTranslationRef;
	
	@Column(name = "manCoding")
	private String manifestationCoding;
	
	@Column(name = "manDisplay")
	private String manifestationDisplay;
	
	@Column(name = "manDescCoding")
	private String manifestationDescCoding;
	
	@Column(name = "manDescDisplay")
	private String manifestationDescDisplay;
    
    @Column(name= "man_translation_ref")
    private String manTranslationRef;

	@Column(name = "recorder")
	private String recorder;

	@Column(name = "warningCode")
	private String warningCode;

    @Column(name = "guid")
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

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

    public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public String getConceptDisplay() {
		return conceptDisplay;
	}

	public void setConceptDisplay(String conceptDisplay) {
		this.conceptDisplay = conceptDisplay;
	}

    public String getDescCode() {
		return descCode;
	}

	public void setDescCode(String descCode) {
		this.descCode = descCode;
	}

	public String getDescDisplay() {
		return descDisplay;
	}

	public void setDescDisplay(String descDisplay) {
		this.descDisplay = descDisplay;
	}

	public String getCodeTranslationRef() {
		return codeTranslationRef;
	}

	public void setCodeTranslationRef(String codeTranslationRef) {
		this.codeTranslationRef = codeTranslationRef;
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

	public String getManifestationDescCoding() {
		return manifestationDescCoding;
	}

	public void setManifestationDescCoding(String manifestationDescCoding) {
		this.manifestationDescCoding = manifestationDescCoding;
	}

	public String getManifestationDescDisplay() {
		return manifestationDescDisplay;
	}

	public void setManifestationDescDisplay(String manifestationDescDisplay) {
		this.manifestationDescDisplay = manifestationDescDisplay;
	}

	public String getManTranslationRef() {
		return manTranslationRef;
	}

	public void setManTranslationRef(String manTranslationRef) {
		this.manTranslationRef = manTranslationRef;
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
