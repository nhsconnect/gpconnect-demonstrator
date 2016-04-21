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
package org.rippleosi.patient.labresults.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.labresults.model.LabResultDetails;

/**
 */
public class LabResultDetailsQueryStrategy extends AbstractQueryStrategy<LabResultDetails> {

    private final String labResultId;

    protected LabResultDetailsQueryStrategy(String patientId, String labResultId) {
        super(patientId);
        this.labResultId = labResultId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0005]/value/value as test_name, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0057]/value/value as conclusion, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0073]/value/value as status, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0075]/value/value as sample_taken, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0] as test_panel " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report-result.v1] " +
                "contains OBSERVATION a_a[openEHR-EHR-OBSERVATION.laboratory_test.v0] " +
                "where a/name/value='Laboratory test report' " +
                "and a/uid/value='" + labResultId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public LabResultDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new LabResultDetailsTransformer().transform(data);
    }
}
