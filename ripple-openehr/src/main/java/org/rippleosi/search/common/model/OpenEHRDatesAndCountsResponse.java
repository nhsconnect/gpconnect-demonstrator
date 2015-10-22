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

package org.rippleosi.search.common.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class OpenEHRDatesAndCountsResponse {

    private String ehrId;

    @JsonProperty("nhsNumber")
    private String nhsNumber;

    private String vitalsId;
    private String ordersId;
    private String labsId;
    private String medsId;
    private String proceduresId;

    private String vitalsCount;
    private String ordersCount;
    private String labsCount;
    private String medsCount;
    private String proceduresCount;

    private Date vitalsDate;
    private Date ordersDate;
    private Date labsDate;
    private Date medsDate;
    private Date proceduresDate;

    public String getEhrId() {
        return ehrId;
    }

    public void setEhrId(String ehrId) {
        this.ehrId = ehrId;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    @JsonSetter("nhsNumber")
    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getVitalsId() {
        return vitalsId;
    }

    public void setVitalsId(String vitalsId) {
        this.vitalsId = vitalsId;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getLabsId() {
        return labsId;
    }

    public void setLabsId(String labsId) {
        this.labsId = labsId;
    }

    public String getMedsId() {
        return medsId;
    }

    public void setMedsId(String medsId) {
        this.medsId = medsId;
    }

    public String getProceduresId() {
        return proceduresId;
    }

    public void setProceduresId(String proceduresId) {
        this.proceduresId = proceduresId;
    }

    public String getVitalsCount() {
        return vitalsCount;
    }

    public void setVitalsCount(String vitalsCount) {
        this.vitalsCount = vitalsCount;
    }

    public String getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(String ordersCount) {
        this.ordersCount = ordersCount;
    }

    public String getLabsCount() {
        return labsCount;
    }

    public void setLabsCount(String labsCount) {
        this.labsCount = labsCount;
    }

    public String getMedsCount() {
        return medsCount;
    }

    public void setMedsCount(String medsCount) {
        this.medsCount = medsCount;
    }

    public String getProceduresCount() {
        return proceduresCount;
    }

    public void setProceduresCount(String proceduresCount) {
        this.proceduresCount = proceduresCount;
    }

    public Date getVitalsDate() {
        return vitalsDate;
    }

    public void setVitalsDate(Date vitalsDate) {
        this.vitalsDate = vitalsDate;
    }

    public Date getOrdersDate() {
        return ordersDate;
    }

    public void setOrdersDate(Date ordersDate) {
        this.ordersDate = ordersDate;
    }

    public Date getLabsDate() {
        return labsDate;
    }

    public void setLabsDate(Date labsDate) {
        this.labsDate = labsDate;
    }

    public Date getMedsDate() {
        return medsDate;
    }

    public void setMedsDate(Date medsDate) {
        this.medsDate = medsDate;
    }

    public Date getProceduresDate() {
        return proceduresDate;
    }

    public void setProceduresDate(Date proceduresDate) {
        this.proceduresDate = proceduresDate;
    }
}
