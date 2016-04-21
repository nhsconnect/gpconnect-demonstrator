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
package uk.gov.hscic.patient.medication.search;

import java.util.List;

import uk.gov.hscic.common.service.AbstractOpenEhrService;
import uk.gov.hscic.patient.medication.model.MedicationDetails;
import uk.gov.hscic.patient.medication.model.MedicationHeadline;
import uk.gov.hscic.patient.medication.model.MedicationSummary;
import uk.gov.hscic.patient.medication.search.MedicationSearch;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRMedicationSearch extends AbstractOpenEhrService {

    public List<MedicationHeadline> findMedicationHeadlines(String patientId) {
        MedicationHeadlineQueryStrategy query = new MedicationHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    public List<MedicationSummary> findAllMedication(String patientId) {
        MedicationSummaryQueryStrategy query = new MedicationSummaryQueryStrategy(patientId);

        return findData(query);
    }

    public MedicationDetails findMedication(String patientId, String medicationId) {
        MedicationDetailsQueryStrategy query = new MedicationDetailsQueryStrategy(patientId, medicationId);

        return findData(query);
    }
}
