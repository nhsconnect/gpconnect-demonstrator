package net.nhs.esb.allergy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.nhs.esb.transfer.model.TransferOfCare;

import javax.persistence.*;

/**
 */
@Entity
@Table(name="allergies")
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

    @Transient
    private String source;
    
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public TransferOfCare getTransferOfCare() {
		return transferOfCare;
	}

	public void setTransferOfCare(TransferOfCare transferOfCare) {
		this.transferOfCare = transferOfCare;
	}
}
