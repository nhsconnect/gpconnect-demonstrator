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

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.model.MedicationSummary;

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
