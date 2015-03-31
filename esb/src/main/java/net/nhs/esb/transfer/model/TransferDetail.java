package net.nhs.esb.transfer.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="transfer_details", schema="poc_legacy")
public class TransferDetail {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
	private Long id;
	
    @Column(name="reason_for_contact")
    @JsonProperty("reasonForContact")
    private String reasonForContact;
	
    @Column(name="clinical_summary")
    @JsonProperty("clinicalSummary")
    private String clinicalSummary;
    
    @OneToOne(mappedBy="transferDetail", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    private Site site;
    
    @OneToOne
    @JoinColumn(name="transfer_care_id")
    @JsonIgnore
    private TransferOfCare transferOfCare;

    public String getReasonForContact() {
        return reasonForContact;
    }

    public void setReasonForContact(String reasonForContact) {
        this.reasonForContact = reasonForContact;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

//	public String getSite() {
//		return site;
//	}
//
//	public void setSite(String site) {
//		this.site = site;
//	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransferOfCare getTransferOfCare() {
		return transferOfCare;
	}

	public void setTransferOfCare(TransferOfCare transferOfCare) {
		this.transferOfCare = transferOfCare;
	}
}
