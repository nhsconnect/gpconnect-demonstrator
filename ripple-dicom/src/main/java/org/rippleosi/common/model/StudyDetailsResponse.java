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
    private MainDicomTags mainDicomTags;

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

    public MainDicomTags getMainDicomTags() {
        return mainDicomTags;
    }

    public void setMainDicomTags(MainDicomTags mainDicomTags) {
        this.mainDicomTags = mainDicomTags;
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


    public class MainDicomTags {

        @JsonProperty("InstitutionName")
        private String institutionName;

        @JsonProperty("ReferringPhysicianName")
        private String referringPhysicianName;

        @JsonProperty("StudyDate")
        private String studyDate;

        @JsonProperty("StudyDescription")
        private String studyDescription;

        @JsonProperty("StudyInstanceUID")
        private String studyInstanceUID;

        @JsonProperty("StudyTime")
        private String studyTime;

        public String getInstitutionName() {
            return institutionName;
        }

        public void setInstitutionName(String institutionName) {
            this.institutionName = institutionName;
        }

        public String getReferringPhysicianName() {
            return referringPhysicianName;
        }

        public void setReferringPhysicianName(String referringPhysicianName) {
            this.referringPhysicianName = referringPhysicianName;
        }

        public String getStudyDate() {
            return studyDate;
        }

        public void setStudyDate(String studyDate) {
            this.studyDate = studyDate;
        }

        public String getStudyDescription() {
            return studyDescription;
        }

        public void setStudyDescription(String studyDescription) {
            this.studyDescription = studyDescription;
        }

        public String getStudyInstanceUID() {
            return studyInstanceUID;
        }

        public void setStudyInstanceUID(String studyInstanceUID) {
            this.studyInstanceUID = studyInstanceUID;
        }

        public String getStudyTime() {
            return studyTime;
        }

        public void setStudyTime(String studyTime) {
            this.studyTime = studyTime;
        }
    }


    public class PatientMainDicomTags {

        @JsonProperty("PatientName")
        private String patientName;

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }
    }
}
