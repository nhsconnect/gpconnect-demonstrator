package net.nhs.esb.allergy.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.nhs.esb.transfer.model.TransferOfCare;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
@Entity
@Table(name="allergies", schema="poc_legacy")
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
    private Long id;
    
    @Column(name="reaction")
    @JsonProperty("reaction")
    private String reaction;
    
    @Column(name="cause")
    @JsonProperty("cause")
    private String cause;
    
    @Column(name="causeCode")
    @JsonProperty("causeCode")
    private String causeCode;
    
    @Column(name="cause_terminology")
    @JsonProperty("causeTerminology")
    private String causeTerminology;
    
    @ManyToOne
    @JoinColumn(name="transfer_care_id")
    @JsonIgnore
    private TransferOfCare transferOfCare;

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(String causeCode) {
        this.causeCode = causeCode;
    }

    public String getCauseTerminology() {
        return causeTerminology;
    }

    public void setCauseTerminology(String causeTerminology) {
        this.causeTerminology = causeTerminology;
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
