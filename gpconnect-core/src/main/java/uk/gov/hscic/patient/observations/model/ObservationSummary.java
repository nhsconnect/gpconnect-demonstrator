package uk.gov.hscic.patient.observations.model;

import java.util.Date;

public class ObservationSummary {

    private String sourceId;
    private String source;
    private String testName;
    private Date sampleTaken;
    private Date dateCreated;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getSampleTaken() {
        return sampleTaken;
    }

    public void setSampleTaken(Date sampleTaken) {
        this.sampleTaken = sampleTaken;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
