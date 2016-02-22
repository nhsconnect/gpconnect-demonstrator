/*
 *   Copyright 2016 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeriesDetailsResponse {

    @JsonProperty("ExpectedNumberOfInstances")
    private String expectedNumberOfInstances;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Instances")
    private List<String> instances;

    @JsonProperty("IsStable")
    private boolean isStable;

    @JsonProperty("LastUpdate")
    private String lastUpdate;

    @JsonProperty("MainDicomTags")
    private SeriesMainDicomTags seriesMainDicomTags;

    @JsonProperty("ParentStudy")
    private String parentStudy;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Type")
    private String type;

    public String getExpectedNumberOfInstances() {
        return expectedNumberOfInstances;
    }

    public void setExpectedNumberOfInstances(String expectedNumberOfInstances) {
        this.expectedNumberOfInstances = expectedNumberOfInstances;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getInstances() {
        return instances;
    }

    public void setInstances(List<String> instances) {
        this.instances = instances;
    }

    public boolean isStable() {
        return isStable;
    }

    public void setStable(boolean stable) {
        isStable = stable;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SeriesMainDicomTags getSeriesMainDicomTags() {
        return seriesMainDicomTags;
    }

    public void setSeriesMainDicomTags(SeriesMainDicomTags seriesMainDicomTags) {
        this.seriesMainDicomTags = seriesMainDicomTags;
    }

    public String getParentStudy() {
        return parentStudy;
    }

    public void setParentStudy(String parentStudy) {
        this.parentStudy = parentStudy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
