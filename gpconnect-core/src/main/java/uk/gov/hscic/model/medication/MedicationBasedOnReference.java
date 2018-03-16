package uk.gov.hscic.model.medication;

public class MedicationBasedOnReference {

	private Long id;
	private String referenceUrl;
	private Long referenceId;
	
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
	
	public Long getReferenceId() {
		return referenceId;
	}
	
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	
	
}
