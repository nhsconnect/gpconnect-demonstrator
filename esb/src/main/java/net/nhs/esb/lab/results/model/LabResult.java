package net.nhs.esb.lab.results.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 */
public class LabResult {

    private String compositionId;
    private String code;
    private String name;
    private Date sampleTaken;
    private Date dateReported;
    private String statusCode;
    private String status;
    private String conclusion;
    private String author;
    private String source;
    private List<LabResultDetail> resultDetails;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSampleTaken() {
        return sampleTaken;
    }

    public void setSampleTaken(Date sampleTaken) {
        this.sampleTaken = sampleTaken;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<LabResultDetail> getResultDetails() {
        return Collections.unmodifiableList(resultDetails);
    }

    public void setResultDetails(List<LabResultDetail> resultDetails) {
        this.resultDetails = resultDetails;
    }

    public static class LabResultDetail {

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
