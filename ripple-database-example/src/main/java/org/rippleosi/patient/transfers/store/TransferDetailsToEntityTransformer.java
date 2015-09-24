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
package org.rippleosi.patient.transfers.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.transfers.model.AllergyHeadline;
import org.rippleosi.patient.transfers.model.AllergyHeadlineEntity;
import org.rippleosi.patient.transfers.model.ContactHeadline;
import org.rippleosi.patient.transfers.model.ContactHeadlineEntity;
import org.rippleosi.patient.transfers.model.MedicationHeadline;
import org.rippleosi.patient.transfers.model.MedicationHeadlineEntity;
import org.rippleosi.patient.transfers.model.ProblemHeadline;
import org.rippleosi.patient.transfers.model.ProblemHeadlineEntity;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareEntity;

public class TransferDetailsToEntityTransformer implements Transformer<TransferOfCareDetails, TransferOfCareEntity> {

    private TransferOfCareEntity transferEntity;

    @Override
    public TransferOfCareEntity transform(TransferOfCareDetails transferDetails) {
        transferEntity = new TransferOfCareEntity();

        List<ProblemHeadlineEntity> problems = CollectionUtils.collect(transferDetails.getProblems(),
                                                                       new ProblemHeadlineToEntityTransformer(),
                                                                       new ArrayList<>());

        List<MedicationHeadlineEntity> medications = CollectionUtils.collect(transferDetails.getMedications(),
                                                                             new MedicationHeadlineToEntityTransformer(),
                                                                             new ArrayList<>());

        List<AllergyHeadlineEntity> allergies = CollectionUtils.collect(transferDetails.getAllergies(),
                                                                        new AllergyHeadlineToEntityTransformer(),
                                                                        new ArrayList<>());

        List<ContactHeadlineEntity> contacts = CollectionUtils.collect(transferDetails.getContacts(),
                                                                       new ContactHeadlineToEntityTransformer(),
                                                                       new ArrayList<>());

        transferEntity.setProblems(problems);
        transferEntity.setMedications(medications);
        transferEntity.setAllergies(allergies);
        transferEntity.setContacts(contacts);
        transferEntity.setReasonForContact(transferDetails.getReasonForContact());
        transferEntity.setClinicalSummary(transferDetails.getClinicalSummary());
        transferEntity.setSiteFrom(transferDetails.getSiteFrom());
        transferEntity.setSiteTo(transferDetails.getSiteTo());
        transferEntity.setDateOfTransfer(transferDetails.getDateOfTransfer());
        transferEntity.setSource(transferDetails.getSource());

        return transferEntity;
    }

    private class ProblemHeadlineToEntityTransformer implements Transformer<ProblemHeadline, ProblemHeadlineEntity> {

        @Override
        public ProblemHeadlineEntity transform(ProblemHeadline problemHeadline) {
            ProblemHeadlineEntity problemHeadlineEntity = new ProblemHeadlineEntity();

            problemHeadlineEntity.setSourceId(problemHeadline.getSourceId());
            problemHeadlineEntity.setTransferOfCare(transferEntity);
            problemHeadlineEntity.setProblem(problemHeadline.getProblem());
            problemHeadlineEntity.setSource(problemHeadline.getSource());

            return problemHeadlineEntity;
        }
    }

    private class MedicationHeadlineToEntityTransformer implements Transformer<MedicationHeadline, MedicationHeadlineEntity> {

        @Override
        public MedicationHeadlineEntity transform(MedicationHeadline medicationHeadline) {
            MedicationHeadlineEntity medicationHeadlineEntity = new MedicationHeadlineEntity();

            medicationHeadlineEntity.setSourceId(medicationHeadline.getSourceId());
            medicationHeadlineEntity.setTransferOfCare(transferEntity);
            medicationHeadlineEntity.setMedication(medicationHeadline.getMedication());
            medicationHeadlineEntity.setSource(medicationHeadline.getSource());

            return medicationHeadlineEntity;
        }
    }

    private class AllergyHeadlineToEntityTransformer implements Transformer<AllergyHeadline, AllergyHeadlineEntity> {

        @Override
        public AllergyHeadlineEntity transform(AllergyHeadline allergyHeadline) {
            AllergyHeadlineEntity allergyHeadlineEntity = new AllergyHeadlineEntity();

            allergyHeadlineEntity.setSourceId(allergyHeadline.getSourceId());
            allergyHeadlineEntity.setTransferOfCare(transferEntity);
            allergyHeadlineEntity.setAllergy(allergyHeadline.getAllergy());
            allergyHeadlineEntity.setSource(allergyHeadline.getSource());

            return allergyHeadlineEntity;
        }
    }

    private class ContactHeadlineToEntityTransformer implements Transformer<ContactHeadline, ContactHeadlineEntity> {

        @Override
        public ContactHeadlineEntity transform(ContactHeadline contactHeadline) {
            ContactHeadlineEntity contactHeadlineEntity = new ContactHeadlineEntity();

            contactHeadlineEntity.setSourceId(contactHeadline.getSourceId());
            contactHeadlineEntity.setTransferOfCare(transferEntity);
            contactHeadlineEntity.setContactName(contactHeadline.getContactName());
            contactHeadlineEntity.setSource(contactHeadline.getSource());

            return contactHeadlineEntity;
        }
    }
}
