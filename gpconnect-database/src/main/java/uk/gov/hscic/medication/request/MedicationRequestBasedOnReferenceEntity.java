package uk.gov.hscic.medication.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "medication_request_based_on")
public class MedicationRequestBasedOnReferenceEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "basedOnReferenceUrl")
	private String referenceReferenceUrl;
	
	@Column(name = "referenceId")
	private Long referenceId;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getBasedOnReferenceReferenceUrl() {
		return referenceReferenceUrl;
	}

	public void setBasedOnReferenceReferenceUrl(String referenceReferenceUrl) {
		this.referenceReferenceUrl = referenceReferenceUrl;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
}
