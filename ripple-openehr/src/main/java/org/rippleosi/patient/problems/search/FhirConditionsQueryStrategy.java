/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.patient.problems.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.Condition;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class FhirConditionsQueryStrategy extends AbstractListQueryStrategy<Condition> {

    private final String ehrId;
    private final String openEhrAddress;

    FhirConditionsQueryStrategy(String patientId, String ehrId, String openEhrAddress) {
        super(patientId);
        this.ehrId = ehrId;
        this.openEhrAddress = openEhrAddress;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
            "a/composer/name as author, " +
            "a/composer/external_ref/id/value as author_id," +
            "a/context/start_time/value as date_created," +
            "a_a/items/data[at0001]/items[at0002]/value/value as problem, " +
            "a_a/items/data[at0001]/items[at0002]/value/defining_code/code_string as terminology_code, " +
            "a_a/items/data[at0001]/items[at0002]/value/defining_code/terminology_id/value as terminology, " +
            "a_a/items/data[at0001]/items[at0003]/value/value as date_of_onset, " +
            "a_a/items/data[at0001]/items[at0069]/value as comment, " +
            "a_a/items/data[at0001]/items[at0073]/value as diagnostic_certainty " +
            "from EHR e " +
            "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
            "contains SECTION a_a[openEHR-EHR-SECTION.problems_issues_rcp.v1] " +
            "where a/name/value='Problem list' " +
            "and e/ehr_status/subject/external_ref/namespace='" + namespace + "' " +
            "and e/ehr_status/subject/external_ref/id/value='" + patientId + "'";
    }

    @Override
    public List<Condition> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new FhirConditionTransformer(ehrId, openEhrAddress), new ArrayList<>());
    }
}
