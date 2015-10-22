/*
 *   Copyright 2015 Ripple OSI
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

package org.rippleosi.search.patient.stats.model;

import java.util.Date;

public class SearchTablePatientDetails {

    private String source;
    private String sourceId;
    private String name;
    private String address;
    private Date dateOfBirth;
    private String gender;
    private String nhsNumber;

    private RecordHeadline vitalsHeadline;
    private RecordHeadline ordersHeadline;
    private RecordHeadline medsHeadline;
    private RecordHeadline resultsHeadline;
    private RecordHeadline treatmentsHeadline;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public RecordHeadline getVitalsHeadline() {
        return vitalsHeadline;
    }

    public void setVitalsHeadline(RecordHeadline vitalsHeadline) {
        this.vitalsHeadline = vitalsHeadline;
    }

    public RecordHeadline getOrdersHeadline() {
        return ordersHeadline;
    }

    public void setOrdersHeadline(RecordHeadline ordersHeadline) {
        this.ordersHeadline = ordersHeadline;
    }

    public RecordHeadline getMedsHeadline() {
        return medsHeadline;
    }

    public void setMedsHeadline(RecordHeadline medsHeadline) {
        this.medsHeadline = medsHeadline;
    }

    public RecordHeadline getResultsHeadline() {
        return resultsHeadline;
    }

    public void setResultsHeadline(RecordHeadline resultsHeadline) {
        this.resultsHeadline = resultsHeadline;
    }

    public RecordHeadline getTreatmentsHeadline() {
        return treatmentsHeadline;
    }

    public void setTreatmentsHeadline(RecordHeadline treatmentsHeadline) {
        this.treatmentsHeadline = treatmentsHeadline;
    }
}
