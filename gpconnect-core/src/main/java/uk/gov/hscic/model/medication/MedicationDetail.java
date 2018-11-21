package uk.gov.hscic.model.medication;

import java.util.Date;

public class MedicationDetail {

    private Long id;
    private String conceptCode;
    private String conceptDisplay;
    private String descCode;
    private String descDisplay;
    private String codeTranslationRef;
    private String text;
    private String batchNumber;
    private Date expiryDate;
	private Date lastUpdated;
	
    public Long getId() {
		return id;
	}
	
    public void setId(Long id) {
		this.id = id;
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
	
    public String getCodeTranslationRef() {
		return codeTranslationRef;
	}

	public void setCodeTranslationRef(String codeTranslationRef) {
		this.codeTranslationRef = codeTranslationRef;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
    public String getBatchNumber() {
		return batchNumber;
	}
	
    public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
    public Date getExpiryDate() {
		return expiryDate;
	}
	
    public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
    
}
