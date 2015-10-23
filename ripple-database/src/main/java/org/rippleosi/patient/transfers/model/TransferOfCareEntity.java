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
package org.rippleosi.patient.transfers.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.rippleosi.patient.details.model.PatientEntity;

@Entity
@Table(name = "transfers_of_care")
public class TransferOfCareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ProblemHeadlineEntity> problems;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<MedicationHeadlineEntity> medications;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AllergyHeadlineEntity> allergies;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ContactHeadlineEntity> contacts;

    @Column(name = "reason_for_contact")
    private String reasonForContact;

    @Column(name = "clinical_summary")
    private String clinicalSummary;

    @Column(name = "site_from")
    private String siteFrom;

    @Column(name = "site_to")
    private String siteTo;

    @Column(name = "date_of_transfer")
    private Date dateOfTransfer;

    @Column(name = "source")
    private String source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public List<ProblemHeadlineEntity> getProblems() {
        return problems;
    }

    public void setProblems(List<ProblemHeadlineEntity> problems) {
        this.problems = problems;
    }

    public List<MedicationHeadlineEntity> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationHeadlineEntity> medications) {
        this.medications = medications;
    }

    public List<AllergyHeadlineEntity> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<AllergyHeadlineEntity> allergies) {
        this.allergies = allergies;
    }

    public List<ContactHeadlineEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactHeadlineEntity> contacts) {
        this.contacts = contacts;
    }

    public String getReasonForContact() {
        return reasonForContact;
    }

    public void setReasonForContact(String reasonForContact) {
        this.reasonForContact = reasonForContact;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getSiteFrom() {
        return siteFrom;
    }

    public void setSiteFrom(String siteFrom) {
        this.siteFrom = siteFrom;
    }

    public String getSiteTo() {
        return siteTo;
    }

    public void setSiteTo(String siteTo) {
        this.siteTo = siteTo;
    }

    public Date getDateOfTransfer() {
        return dateOfTransfer;
    }

    public void setDateOfTransfer(Date dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
