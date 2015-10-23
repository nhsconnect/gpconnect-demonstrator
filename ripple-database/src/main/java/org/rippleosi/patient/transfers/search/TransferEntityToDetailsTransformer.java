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
package org.rippleosi.patient.transfers.search;

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
import org.springframework.stereotype.Component;

@Component
public class TransferEntityToDetailsTransformer implements Transformer<TransferOfCareEntity, TransferOfCareDetails> {

    @Override
    public TransferOfCareDetails transform(TransferOfCareEntity transferEntity) {
        List<ProblemHeadline> problems = CollectionUtils.collect(transferEntity.getProblems(),
                                                                 new ProblemHeadlineEntityTransformer(),
                                                                 new ArrayList<>());

        List<MedicationHeadline> medications = CollectionUtils.collect(transferEntity.getMedications(),
                                                                       new MedicationHeadlineEntityTransformer(),
                                                                       new ArrayList<>());

        List<AllergyHeadline> allergies = CollectionUtils.collect(transferEntity.getAllergies(),
                                                                  new AllergyHeadlineEntityTransformer(),
                                                                  new ArrayList<>());

        List<ContactHeadline> contacts = CollectionUtils.collect(transferEntity.getContacts(),
                                                                 new ContactHeadlineEntityTransformer(),
                                                                 new ArrayList<>());

        TransferOfCareDetails transferDetails = new TransferOfCareDetails();
        transferDetails.setSourceId(transferEntity.getId().toString());
        transferDetails.setProblems(problems);
        transferDetails.setMedications(medications);
        transferDetails.setAllergies(allergies);
        transferDetails.setContacts(contacts);
        transferDetails.setReasonForContact(transferEntity.getReasonForContact());
        transferDetails.setClinicalSummary(transferEntity.getClinicalSummary());
        transferDetails.setSiteFrom(transferEntity.getSiteFrom());
        transferDetails.setSiteTo(transferEntity.getSiteTo());
        transferDetails.setDateOfTransfer(transferEntity.getDateOfTransfer());
        transferDetails.setSource(transferEntity.getSource());

        return transferDetails;
    }

    private static class ProblemHeadlineEntityTransformer implements Transformer<ProblemHeadlineEntity, ProblemHeadline> {

        @Override
        public ProblemHeadline transform(ProblemHeadlineEntity problemHeadlineEntity) {
            ProblemHeadline problemHeadline = new ProblemHeadline();

            problemHeadline.setSourceId(problemHeadlineEntity.getSourceId());
            problemHeadline.setProblem(problemHeadlineEntity.getProblem());
            problemHeadline.setSource(problemHeadlineEntity.getSource());

            return problemHeadline;
        }
    }

    private static class MedicationHeadlineEntityTransformer implements Transformer<MedicationHeadlineEntity, MedicationHeadline> {

        @Override
        public MedicationHeadline transform(MedicationHeadlineEntity medicationHeadlineEntity) {
            MedicationHeadline medicationHeadline = new MedicationHeadline();

            medicationHeadline.setSourceId(medicationHeadlineEntity.getSourceId());
            medicationHeadline.setMedication(medicationHeadlineEntity.getMedication());
            medicationHeadline.setSource(medicationHeadlineEntity.getSource());

            return medicationHeadline;
        }
    }

    private static class AllergyHeadlineEntityTransformer implements Transformer<AllergyHeadlineEntity, AllergyHeadline> {

        @Override
        public AllergyHeadline transform(AllergyHeadlineEntity allergyHeadlineEntity) {
            AllergyHeadline allergyHeadline = new AllergyHeadline();

            allergyHeadline.setSourceId(allergyHeadlineEntity.getSourceId());
            allergyHeadline.setAllergy(allergyHeadlineEntity.getAllergy());
            allergyHeadline.setSource(allergyHeadlineEntity.getSource());

            return allergyHeadline;
        }
    }

    private static class ContactHeadlineEntityTransformer implements Transformer<ContactHeadlineEntity, ContactHeadline> {

        @Override
        public ContactHeadline transform(ContactHeadlineEntity contactHeadlineEntity) {
            ContactHeadline contactHeadline = new ContactHeadline();

            contactHeadline.setSourceId(contactHeadlineEntity.getSourceId());
            contactHeadline.setContactName(contactHeadlineEntity.getContactName());
            contactHeadline.setSource(contactHeadlineEntity.getSource());

            return contactHeadline;
        }
    }
}
