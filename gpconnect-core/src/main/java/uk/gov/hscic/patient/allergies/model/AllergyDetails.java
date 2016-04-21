package uk.gov.hscic.patient.allergies.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class AllergyDetails implements Serializable {

    private String sourceId;
    private String cause;
    private String causeCode;
    private String causeTerminology;
    private String terminologyCode;
    private String reaction;
    private String source;
    private String author;
    private Date dateCreated;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public String getTerminologyCode() {
        return terminologyCode;
    }

    public void setTerminologyCode(String terminologyCode) {
        this.terminologyCode = terminologyCode;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
