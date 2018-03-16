package uk.gov.hscic.model.medication;

import java.util.Date;

public class MedicationDetail {

    private Long id;
    private String code;
    private String display;
    private String text;
    private String batchNumber;
    private Date expiryDate;
	
    public Long getId() {
		return id;
	}
	
    public void setId(Long id) {
		this.id = id;
	}
	
    public String getCode() {
		return code;
	}
	
    public void setCode(String code) {
		this.code = code;
	}
	
    public String getDisplay() {
		return display;
	}
	
    public void setDisplay(String display) {
		this.display = display;
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
}
