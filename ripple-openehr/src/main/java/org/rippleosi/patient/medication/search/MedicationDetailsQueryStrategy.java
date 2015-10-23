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
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.medication.model.MedicationDetails;

/**
 */
public class MedicationDetailsQueryStrategy extends AbstractQueryStrategy<MedicationDetails> {

    private final String medicationId;

    MedicationDetailsQueryStrategy(String patientId, String medicationId) {
        super(patientId);
        this.medicationId = medicationId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "a_a/items/items/data[at0001]/items/items[at0001]/value/value as name, " +
                "a_a/items/items/data[at0001]/items/items[at0001]/value/defining_code/code_string as medication_code, " +
                "a_a/items/items/data[at0001]/items/items[at0001]/value/defining_code/terminology_id/value as medication_terminology, " +
                "a_a/items/items/data[at0001]/items/items[at0002]/value/defining_code/code_string as route, " +
                "a_a/items/items/data[at0001]/items/items[at0003]/value/value as dose_directions, " +
                "a_a/items/items/data[at0001]/items/items[at0020]/value/value as dose_amount, " +
                "a_a/items/items/data[at0001]/items/items[at0021]/value/value as dose_timing, " +
                "a_a/items/items/data[at0001]/items/items[at0046]/items/value/value as start_date " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.medication_medical_devices_rcp.v1] " +
                "where a/name/value='Current medication list' " +
                "and a/uid/value='" + medicationId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public MedicationDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new MedicationDetailsTransformer().transform(data);
    }
}
