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

public class StudyDetailsResponse {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("IsStable")
    private boolean isStable;

    @JsonProperty("LastUpdate")
    private String lastUpdate;

    @JsonProperty("MainDicomTags")
    private StudyMainDicomTags studyMainDicomTags;

    @JsonProperty("ParentPatient")
    private String parentPatient;

    @JsonProperty("PatientMainDicomTags")
    private PatientMainDicomTags patientMainDicomTags;

    @JsonProperty("Series")
    private List<String> series;

    @JsonProperty("Type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public StudyMainDicomTags getStudyMainDicomTags() {
        return studyMainDicomTags;
    }

    public void setStudyMainDicomTags(StudyMainDicomTags studyMainDicomTags) {
        this.studyMainDicomTags = studyMainDicomTags;
    }

    public String getParentPatient() {
        return parentPatient;
    }

    public void setParentPatient(String parentPatient) {
        this.parentPatient = parentPatient;
    }

    public PatientMainDicomTags getPatientMainDicomTags() {
        return patientMainDicomTags;
    }

    public void setPatientMainDicomTags(PatientMainDicomTags patientMainDicomTags) {
        this.patientMainDicomTags = patientMainDicomTags;
    }

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
