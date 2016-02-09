package org.rippleosi.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstanceDetailsResponse {

    @JsonProperty("FileSize")
    private Long fileSize;

    @JsonProperty("FileUuid")
    private String fileUuid;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("IndexInSeries")
    private Integer indexInSeries;

    @JsonProperty("MainDicomTags")
    private InstanceMainDicomTags mainDicomTags;

    @JsonProperty("ParentSeries")
    private String parentSeries;

    @JsonProperty("Type")
    private String type;

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIndexInSeries() {
        return indexInSeries;
    }

    public void setIndexInSeries(Integer indexInSeries) {
        this.indexInSeries = indexInSeries;
    }

    public InstanceMainDicomTags getMainDicomTags() {
        return mainDicomTags;
    }

    public void setMainDicomTags(InstanceMainDicomTags mainDicomTags) {
        this.mainDicomTags = mainDicomTags;
    }

    public String getParentSeries() {
        return parentSeries;
    }

    public void setParentSeries(String parentSeries) {
        this.parentSeries = parentSeries;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
