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

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.medication.model.MedicationDetails;
import uk.gov.hscic.patient.medication.model.MedicationHeadline;
import uk.gov.hscic.patient.medication.model.MedicationSummary;

/**
 */
public class NotConfiguredMedicationSearch implements MedicationSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<MedicationHeadline> findMedicationHeadlines(String patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationSearch.class);
    }

    @Override
    public List<MedicationSummary> findAllMedication(String patientId) {
        throw ConfigurationException.unimplementedTransaction(MedicationSearch.class);
    }

    @Override
    public MedicationDetails findMedication(String patientId, String medicationId) {
        throw ConfigurationException.unimplementedTransaction(MedicationSearch.class);
    }
}
