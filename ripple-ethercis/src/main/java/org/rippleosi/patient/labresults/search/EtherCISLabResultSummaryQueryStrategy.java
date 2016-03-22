/*
 *  Copyright 2015 Ripple OSI
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
 *
 */
package org.rippleosi.patient.labresults.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractEtherCISListQueryStrategy;
import org.rippleosi.patient.labresults.model.LabResultSummary;

/**
 */
public class EtherCISLabResultSummaryQueryStrategy extends AbstractEtherCISListQueryStrategy<LabResultSummary> {

    EtherCISLabResultSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT ehr.entry.composition_id as uid, " +
            "ehr.event_context.start_time as date_created,  " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /time,/value,value" +
            "}' as sample_taken, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, /data[at0001],/events,/events[at0002],0, " +
                "/data[at0003],/items[at0005 and name/value=''Test name''],/value,value" +
            "}' as test_name " +
            "FROM ehr.entry " +
            "INNER JOIN ehr.composition ON ehr.composition.id = ehr.entry.composition_id " +
            "INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id " +
            "INNER JOIN ehr.party_identified ON ehr.composition.composer = ehr.party_identified.id " +
            "WHERE (ehr.composition.ehr_id = '" + ehrId + "') " +
            "AND (ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.report-result.v1') " +
            "ORDER BY ehr.event_context.start_time DESC;";
    }

    @Override
    public List<LabResultSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new EtherCISLabResultSummaryTransformer(), new ArrayList<>());
    }
}
