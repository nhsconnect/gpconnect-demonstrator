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

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractEtherCISQueryStrategy;
import org.rippleosi.patient.labresults.model.LabResultDetails;

/**
 */
public class EtherCISLabResultDetailsQueryStrategy extends AbstractEtherCISQueryStrategy<LabResultDetails> {

    private final String labResultId;

    protected EtherCISLabResultDetailsQueryStrategy(String patientId, String labResultId) {
        super(patientId);
        this.labResultId = labResultId;
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT ehr.entry.composition_id as uid, " +
            "ehr.party_identified.name as author, " +
            "ehr.event_context.start_time as date_created, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /time,/value,value" +
            "}' as sample_taken, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[at0005 and name/value=''Test name''],/value,value" +
            "}' as test_name, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[at0057 and name/value=''Conclusion''],/value,value" +
            "}' as conclusion, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[at0073 and name/value=''Test status''],/value,value" +
            "}' as status, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0 and name/value=''Laboratory test panel''], " +
                "/items[at0002],0,/items[at0001],0,/value,/name" +
            "}' as result_name, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0 and name/value=''Laboratory test panel''], " +
                "/items[at0002],0,/items[at0001],0,/value,/value,magnitude" +
            "}' as result_value, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0 and name/value=''Laboratory test panel''], " +
                "/items[at0002],0,/items[at0001],0,/value,/value,units" +
            "}' as result_unit, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0 and name/value=''Laboratory test panel''], " +
                "/items[at0002],0,/items[at0001],0,/value,/value,normalRange,interval,lower,magnitude" +
            "}' as normal_range_lower, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0 and name/value=''Laboratory test panel''], " +
                "/items[at0002],0,/items[at0001],0,/value,/value,normalRange,interval,upper,magnitude" +
            "}' as normal_range_upper, " +
            "ehr.entry.entry #>> " +
            "'{" +
                "/composition[openEHR-EHR-COMPOSITION.report-result.v1 and name/value=''Laboratory test report''], " +
                "/content[openEHR-EHR-OBSERVATION.laboratory_test.v0],0, " +
                "/data[at0001],/events,/events[at0002],0, /data[at0003],/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0 and name/value=''Laboratory test panel''], " +
                "/items[at0002],0,/items[at0003],0,/value,/value,value" +
            "}' as result_comment " +
            "FROM ehr.entry " +
            "INNER JOIN ehr.composition ON ehr.composition.id = ehr.entry.composition_id " +
            "INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id " +
            "INNER JOIN ehr.party_identified ON ehr.composition.composer = ehr.party_identified.id " +
            "WHERE (ehr.composition.ehr_id = '" + ehrId +"') " +
            "AND (ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.report-result.v1') " +
            "AND ehr.entry.composition_id = '" + labResultId + "';";
    }

    @Override
    public LabResultDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new EtherCISLabResultDetailsTransformer().transform(data);
    }
}
