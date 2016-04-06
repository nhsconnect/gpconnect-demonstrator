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
package org.rippleosi.patient.procedures.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractEtherCISListQueryStrategy;
import org.rippleosi.patient.procedures.model.ProcedureSummary;

/**
 */
public class EtherCISProcedureSummaryQueryStrategy extends AbstractEtherCISListQueryStrategy<ProcedureSummary> {

    EtherCISProcedureSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return new StringBuilder()
            .append("SELECT ")
            .append("ehr.entry.composition_id as uid, ")

            .append("ehr.entry.entry #>> ")
            .append("'{")
                .append("/composition[openEHR-EHR-COMPOSITION.care_summary.v0 and name/value=''Procedures list''], ")
                .append("/content[openEHR-EHR-SECTION.procedures_rcp.v1],0, ")
                .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/description[at0001],/items[at0002 and name/value=''Procedure name''],/value,value")
            .append("}' as procedure_name, ")

            .append("ehr.entry.entry #>> ")
            .append("'{")
                .append("/composition[openEHR-EHR-COMPOSITION.care_summary.v0 and name/value=''Procedures list''], ")
                .append("/content[openEHR-EHR-SECTION.procedures_rcp.v1],0, ")
                .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/time,/value,value")
            .append("}' as procedure_datetime, ")

            .append("ehr.event_context.start_time ")

            .append("FROM ehr.entry ")
            .append("INNER JOIN ehr.composition ON ehr.composition.id = ehr.entry.composition_id ")
            .append("INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id ")

            .append("WHERE ehr.composition.ehr_id = '").append(ehrId).append("' ")
            .append("AND ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.care_summary.v0' ")

            .append("ORDER BY ehr.event_context.start_time DESC;")
            .toString();
    }

    @Override
    public List<ProcedureSummary> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new EtherCISProcedureOnlyPredicate());

        return CollectionUtils.collect(filtered, new EtherCISProcedureSummaryTransformer(), new ArrayList<>());
    }
}
