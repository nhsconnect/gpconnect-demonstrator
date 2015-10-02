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
package org.rippleosi.patient.laborders.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.laborders.model.LabOrderDetails;

/**
 */
public class LabOrderDetailsQueryStrategy extends AbstractQueryStrategy<LabOrderDetails> {

    private final String labOrderId;

    LabOrderDetailsQueryStrategy(String patientId, String labOrderId) {
        super(patientId);
        this.labOrderId = labOrderId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "a_a/activities[at0001]/description/items[at0121]/value/value as name, " +
                "a_a/activities[at0001]/description/items[at0121]/value/defining_code/code_string as code, " +
                "a_a/activities[at0001]/description/items[at0121]/value/defining_code/terminology_id/value as terminology, " +
                "a_a/activities[at0001]/timing/value as order_date " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.referral.v0] " +
                "contains INSTRUCTION a_a[openEHR-EHR-INSTRUCTION.request-lab_test.v1] " +
                "where a/name/value='Laboratory order' " +
                "and a/uid/value='" + labOrderId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public LabOrderDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new LabOrderDetailsTransformer().transform(data);
    }
}
