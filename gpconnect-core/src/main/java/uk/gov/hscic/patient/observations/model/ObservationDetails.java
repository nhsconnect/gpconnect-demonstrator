package uk.gov.hscic.patient.observations.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 */
public class ObservationDetails implements Serializable {

    private String sourceId;
    private String source;
    private String testName;
    private Date sampleTaken;
    private Date dateCreated;
    private String status;
    private String conclusion;
    private String author;
    private List<TestResult> testResults;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    public static class TestResult implements Serializable {

        private String result;
        private String value;
        private String unit;
        private String normalRange;
        private String comment;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getNormalRange() {
            return normalRange;
        }

        public void setNormalRange(String normalRange) {
            this.normalRange = normalRange;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

}
