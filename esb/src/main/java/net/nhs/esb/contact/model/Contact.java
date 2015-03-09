package net.nhs.esb.contact.model;

/**
 */
public class Contact {

    private String name;
    private String contactInformation;
    private String relationship;
    private String relationshipType;
    private String relationshipCode;
    private String relationshipTerminology;
    private Boolean nextOfKin;
    private String note;

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
}
