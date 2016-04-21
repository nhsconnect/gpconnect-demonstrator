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
package org.rippleosi.patient.allergies.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.allergies.model.AllergyDetails;

/**
 */
public class AllergyDetailsQueryStrategy extends AbstractQueryStrategy<AllergyDetails> {

    private final String allergyId;

    AllergyDetailsQueryStrategy(String patientId, String allergyId) {
        super(patientId);
        this.allergyId = allergyId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "a_a/items/data[at0001]/items[at0002]/value/value as cause, " +
                "a_a/items/data[at0001]/items[at0002]/value/defining_code/terminology_id/value as cause_terminology, " +
                "a_a/items/data[at0001]/items[at0002]/value/defining_code/code_string as cause_code, " +
                "a_a/items/data[at0001]/items[at0025]/items[at0022]/value/value as reaction " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1] " +
                "where a/name/value='Allergies list' " +
                "and a/uid/value='" + allergyId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public AllergyDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String,Object> data = resultSet.get(0);

        return new AllergyDetailsTransformer().transform(data);
    }
}
