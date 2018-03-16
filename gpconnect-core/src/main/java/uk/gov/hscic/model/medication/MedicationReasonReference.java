package uk.gov.hscic.model.medication;

public class MedicationReasonReference {
	
	private Long id;
	private String referenceUrl;
	private String referenceId;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getReferenceUrl() {
		return referenceUrl;
	}
	
	public void setReferenceUrl(String referenceUrl) {
		this.referenceUrl = referenceUrl;
	}
	
	public String getReferenceId() {
		return referenceId;
	}
	
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	
}
