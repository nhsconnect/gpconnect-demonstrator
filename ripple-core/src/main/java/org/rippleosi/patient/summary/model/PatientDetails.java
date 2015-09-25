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
package org.rippleosi.patient.summary.model;

import java.util.Date;
import java.util.List;

/**
 */
public class PatientDetails {

    private String id;
    private String name;
    private String gender;
    private Date dateOfBirth;
    private String nhsNumber;
    private String address;
    private String telephone;
    private String gpDetails;
    private String pasNumber;

    private List<PatientHeadline> problems;
    private List<PatientHeadline> allergies;
    private List<PatientHeadline> medications;
    private List<PatientHeadline> contacts;
    private List<TransferHeadline> transfers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGpDetails() {
        return gpDetails;
    }

    public void setGpDetails(String gpDetails) {
        this.gpDetails = gpDetails;
    }

    public String getPasNumber() {
        return pasNumber;
    }

    public void setPasNumber(String pasNumber) {
        this.pasNumber = pasNumber;
    }

    public List<PatientHeadline> getProblems() {
        return problems;
    }

    public void setProblems(List<PatientHeadline> problems) {
        this.problems = problems;
    }

    public List<PatientHeadline> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<PatientHeadline> allergies) {
        this.allergies = allergies;
    }

    public List<PatientHeadline> getMedications() {
        return medications;
    }

    public void setMedications(List<PatientHeadline> medications) {
        this.medications = medications;
    }

    public List<PatientHeadline> getContacts() {
        return contacts;
    }

    public void setContacts(List<PatientHeadline> contacts) {
        this.contacts = contacts;
    }

    public List<TransferHeadline> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<TransferHeadline> transfers) {
        this.transfers = transfers;
    }
}
