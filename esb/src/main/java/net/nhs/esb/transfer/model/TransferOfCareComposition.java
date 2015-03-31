package net.nhs.esb.transfer.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="transfer_care_compositions", schema="poc_legacy")
public class TransferOfCareComposition {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
	private Long id;
	
	@Column(name="composition_id", unique=true, nullable=false)
    @JsonProperty("compositionId")
    private String compositionId;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="transferOfCareComposition", cascade=CascadeType.ALL)
    private List<TransferOfCare> transfers;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<TransferOfCare> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<TransferOfCare> transfers) {
        this.transfers = transfers;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
