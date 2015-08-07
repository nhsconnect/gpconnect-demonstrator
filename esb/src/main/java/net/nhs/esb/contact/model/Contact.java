package net.nhs.esb.contact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.nhs.esb.transfer.model.TransferOfCare;

import javax.persistence.*;


/**
 */
@Entity
@Table(name="contacts")
public class Contact {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
    private Long id;
	
	@Column(name="name")
    @JsonProperty("name")
    private String name;
	
	@Column(name="contact_information")
    @JsonProperty("contactInformation")
    private String contactInformation;
	
	@Column(name="relationship")
    @JsonProperty("relationship")
    private String relationship;
	
	@Column(name="relationship_type")
    @JsonProperty("relationshipType")
    private String relationshipType;
	
	@Column(name="relationship_code")
    @JsonProperty("relationshipCode")
    private String relationshipCode;
	
	@Column(name="relationship_terminology")
    @JsonProperty("relationshipTerminology")
    private String relationshipTerminology;
	
	@Column(name="next_of_kin")
    @JsonProperty("nextOfKin")
    private Boolean nextOfKin;
	
	@Column(name="note")
    @JsonProperty("note")
    private String note;
    
    @ManyToOne
    @JoinColumn(name="transfer_care_id")
    @JsonIgnore
    private TransferOfCare transferOfCare;

    @Transient
    private String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipCode() {
        return relationshipCode;
    }

    public void setRelationshipCode(String relationshipCode) {
        this.relationshipCode = relationshipCode;
    }

    public String getRelationshipTerminology() {
        return relationshipTerminology;
    }

    public void setRelationshipTerminology(String relationshipTerminology) {
        this.relationshipTerminology = relationshipTerminology;
    }

    public Boolean getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(Boolean nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
