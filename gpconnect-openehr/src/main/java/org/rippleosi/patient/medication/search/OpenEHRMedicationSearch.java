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
package org.rippleosi.patient.medication.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.model.MedicationSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRMedicationSearch extends AbstractOpenEhrService implements MedicationSearch {

    @Override
    public List<MedicationHeadline> findMedicationHeadlines(String patientId) {
        MedicationHeadlineQueryStrategy query = new MedicationHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public List<MedicationSummary> findAllMedication(String patientId) {
        MedicationSummaryQueryStrategy query = new MedicationSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public MedicationDetails findMedication(String patientId, String medicationId) {
        MedicationDetailsQueryStrategy query = new MedicationDetailsQueryStrategy(patientId, medicationId);

        return findData(query);
    }
}
