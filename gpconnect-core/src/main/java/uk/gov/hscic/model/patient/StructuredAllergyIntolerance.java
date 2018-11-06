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
    private String conceptCode;
    private String conceptDisplay;
    private String descCode;
    private String descDisplay;
    private String codeTranslationSystem;
    private String codeTranslationId;
    private String codeTranslationDisplay;
    private String manifestationCoding;
    private String manifestationDisplay;
    private String manifestationDescCoding;
    private String manifestationDescDisplay;
    private String manTranslationSystem;
    private String manTranslationId;
    private String manTranslationDisplay;
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

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
	
    public String getDescDisplay() {
		return descDisplay;
	}
	
    public void setDescDisplay(String descDisplay) {
		this.descDisplay = descDisplay;
	}
    
    public String getDescCode() {
		return descCode;
	}
	
    public void setDescCode(String descCode) {
		this.descCode = descCode;
	}

    public String getCodeTranslationSystem() {
		return codeTranslationSystem;
	}

	public void setCodeTranslationSystem(String codeTranslationSystem) {
		this.codeTranslationSystem = codeTranslationSystem;
	}

	public String getCodeTranslationId() {
		return codeTranslationId;
	}

	public void setCodeTranslationId(String codeTranslationId) {
		this.codeTranslationId = codeTranslationId;
	}

	public String getCodeTranslationDisplay() {
		return codeTranslationDisplay;
	}

	public void setCodeTranslationDisplay(String codeTranslationDisplay) {
		this.codeTranslationDisplay = codeTranslationDisplay;
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

	public String getManTranslationSystem() {
		return manTranslationSystem;
	}

	public void setManTranslationSystem(String manTranslationSystem) {
		this.manTranslationSystem = manTranslationSystem;
	}

	public String getManTranslationId() {
		return manTranslationId;
	}

	public void setManTranslationId(String manTranslationId) {
		this.manTranslationId = manTranslationId;
	}

	public String getManTranslationDisplay() {
		return manTranslationDisplay;
	}

	public void setManTranslationDisplay(String manTranslationDisplay) {
		this.manTranslationDisplay = manTranslationDisplay;
	}
}
