package org.rippleosi.patient.dicom.model;

public class DicomInstanceSummary {

    private String sourceId;
    private String source;
    private String fileUuid;
    private String parentSeries;

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

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getParentSeries() {
        return parentSeries;
    }

    public void setParentSeries(String parentSeries) {
        this.parentSeries = parentSeries;
    }
}
