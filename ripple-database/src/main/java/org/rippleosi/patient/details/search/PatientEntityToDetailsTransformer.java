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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.types.RepoSourceType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientEntityToDetailsTransformer implements Transformer<PatientEntity, PatientDetails> {

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

        Collection<String> addressList = Arrays.asList(StringUtils.trimToNull(patientEntity.getAddress1()),
                                                       StringUtils.trimToNull(patientEntity.getAddress2()),
                                                       StringUtils.trimToNull(patientEntity.getAddress3()),
                                                       StringUtils.trimToNull(patientEntity.getPostcode()));

        addressList = CollectionUtils.removeAll(addressList, Collections.singletonList(null));

        String address = StringUtils.join(addressList, ", ");

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
        try {
            AllergySearch allergySearch = allergySearchFactory.select(null);

            List<AllergyHeadline> allergies = allergySearch.findAllergyHeadlines(patientId);

            return CollectionUtils.collect(allergies, new AllergyTransformer(), new ArrayList<>());
        } catch (DataNotFoundException ignore) {
            return Collections.emptyList();
        }
    }

    private List<PatientHeadline> findContacts(String patientId) {
        try {
            ContactSearch contactSearch = contactSearchFactory.select(null);

            List<ContactHeadline> contacts = contactSearch.findContactHeadlines(patientId);

            return CollectionUtils.collect(contacts, new ContactTransformer(), new ArrayList<>());
        } catch (DataNotFoundException ignore) {
            return Collections.emptyList();
        }
    }

    private List<PatientHeadline> findMedications(String patientId) {
        try {
            MedicationSearch medicationSearch = medicationSearchFactory.select(null);

            List<MedicationHeadline> medications = medicationSearch.findMedicationHeadlines(patientId);

            return CollectionUtils.collect(medications, new MedicationTransformer(), new ArrayList<>());
        } catch (DataNotFoundException ignore) {
            return Collections.emptyList();
        }
    }

    private List<PatientHeadline> findProblems(String patientId) {
        try {
            ProblemSearch problemSearch = problemSearchFactory.select(null);

            List<ProblemHeadline> problems = problemSearch.findProblemHeadlines(patientId);

            return CollectionUtils.collect(problems, new ProblemTransformer(), new ArrayList<>());
        } catch (DataNotFoundException ignore) {
            return Collections.emptyList();
        }
    }

    private List<TransferHeadline> findTransfers(String patientId) {
        try {
            TransferOfCareSearch transferOfCareSearch = transferOfCareSearchFactory.select(null);

            List<TransferOfCareSummary> transfers = transferOfCareSearch.findAllTransfers(patientId);

            return CollectionUtils.collect(transfers, new TransferOfCareTransformer(), new ArrayList<>());
        } catch (DataNotFoundException ignore) {
            return Collections.emptyList();
        }
    }
}
