/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.medication.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class MedicationDetails implements Serializable {

    private String sourceId;
    private String source;
    private String name;
    private String doseAmount;
    private String doseDirections;
    private String doseTiming;
    private String route;
    private Date startDate;
    private Date startTime;
    private String medicationCode;
    private String medicationTerminology;
    private String author;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(String doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getDoseDirections() {
        return doseDirections;
    }

    public void setDoseDirections(String doseDirections) {
        this.doseDirections = doseDirections;
    }

    public String getDoseTiming() {
        return doseTiming;
    }

    public void setDoseTiming(String doseTiming) {
        this.doseTiming = doseTiming;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getMedicationCode() {
        return medicationCode;
    }

    public void setMedicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
    }

    public String getMedicationTerminology() {
        return medicationTerminology;
    }

    public void setMedicationTerminology(String medicationTerminology) {
        this.medicationTerminology = medicationTerminology;
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
