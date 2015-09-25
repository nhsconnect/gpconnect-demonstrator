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
package org.rippleosi.patient.details.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.patient.allergies.model.AllergyHeadline;
import org.rippleosi.patient.allergies.search.AllergySearch;
import org.rippleosi.patient.allergies.search.AllergySearchFactory;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.search.ContactSearch;
import org.rippleosi.patient.contacts.search.ContactSearchFactory;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.search.MedicationSearch;
import org.rippleosi.patient.medication.search.MedicationSearchFactory;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.search.ProblemSearch;
import org.rippleosi.patient.problems.search.ProblemSearchFactory;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientHeadline;
import org.rippleosi.patient.summary.model.TransferHeadline;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;
import org.rippleosi.patient.transfers.search.TransferOfCareSearch;
import org.rippleosi.patient.transfers.search.TransferOfCareSearchFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientEntityToDetailsTransformer implements Transformer<PatientEntity, PatientDetails> {

    private static final Logger logger = LoggerFactory.getLogger(PatientEntityToDetailsTransformer.class);

    @Autowired
    private AllergySearchFactory allergySearchFactory;

    @Autowired
    private ContactSearchFactory contactSearchFactory;

    @Autowired
    private MedicationSearchFactory medicationSearchFactory;

    @Autowired
    private ProblemSearchFactory problemSearchFactory;

    @Autowired
    private TransferOfCareSearchFactory transferOfCareSearchFactory;


    @Override
    public PatientDetails transform(PatientEntity patientEntity) {
        PatientDetails patient = new PatientDetails();

        String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();
        String address = patientEntity.getAddress1() + ", " +
                         patientEntity.getAddress2() + ", " +
                         patientEntity.getAddress3() + ", " +
                         patientEntity.getPostcode();

        String patientId = patientEntity.getNhsNumber();

        patient.setId(patientId);
        patient.setName(name);
        patient.setGender(patientEntity.getGender());
        patient.setDateOfBirth(patientEntity.getDateOfBirth());
        patient.setNhsNumber(patientId);
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setAddress(address);
        patient.setTelephone(patientEntity.getPhone());
        patient.setGpDetails(patientEntity.getGp().getName());
        patient.setPasNumber(patientEntity.getPasNumber());

        patient.setAllergies(findAllergies(patientId));
        patient.setContacts(findContacts(patientId));
        patient.setMedications(findMedications(patientId));
        patient.setProblems(findProblems(patientId));
        patient.setTransfers(findTransfers(patientId));

        return patient;
    }

    private List<PatientHeadline> findAllergies(String patientId) {
        AllergySearch allergySearch = allergySearchFactory.select(null);
        List<AllergyHeadline> allergies;

        try {
            allergies = allergySearch.findAllergyHeadlines(patientId);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);
            allergies = new ArrayList<>();
        }

        return CollectionUtils.collect(allergies, new AllergyTransformer(), new ArrayList<>());
    }

    private List<PatientHeadline> findContacts(String patientId) {
        ContactSearch contactSearch = contactSearchFactory.select(null);
        List<ContactHeadline> contacts;

        try {
            contacts = contactSearch.findContactHeadlines(patientId);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);
            contacts = new ArrayList<>();
        }

        return CollectionUtils.collect(contacts, new ContactTransformer(), new ArrayList<>());
    }

    private List<PatientHeadline> findMedications(String patientId) {
        MedicationSearch medicationSearch = medicationSearchFactory.select(null);
        List<MedicationHeadline> medications;

        try {
            medications = medicationSearch.findMedicationHeadlines(patientId);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);
            medications = new ArrayList<>();
        }

        return CollectionUtils.collect(medications, new MedicationTransformer(), new ArrayList<>());
    }

    private List<PatientHeadline> findProblems(String patientId) {
        ProblemSearch problemSearch = problemSearchFactory.select(null);
        List<ProblemHeadline> problems;

        try {
            problems = problemSearch.findProblemHeadlines(patientId);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);
            problems = new ArrayList<>();
        }

        return CollectionUtils.collect(problems, new ProblemTransformer(), new ArrayList<>());
    }

    private List<TransferHeadline> findTransfers(String patientId) {
        TransferOfCareSearch transferOfCareSearch = transferOfCareSearchFactory.select(null);
        List<TransferOfCareSummary> transfers;

        try {
            transfers = transferOfCareSearch.findAllTransfers(patientId);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);
            transfers = new ArrayList<>();
        }

        return CollectionUtils.collect(transfers, new TransferOfCareTransformer(), new ArrayList<>());
    }
}
