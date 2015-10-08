package org.rippleosi.search.common.model;

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
