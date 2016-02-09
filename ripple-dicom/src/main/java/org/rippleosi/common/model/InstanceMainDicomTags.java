package org.rippleosi.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstanceMainDicomTags {

    @JsonProperty("InstanceNumber")
    private Integer instanceNumber;

    @JsonProperty("SOPInstanceUID")
    private String sopInstanceUid;

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(Integer instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public String getSopInstanceUid() {
        return sopInstanceUid;
    }

    public void setSopInstanceUid(String sopInstanceUid) {
        this.sopInstanceUid = sopInstanceUid;
    }
}
