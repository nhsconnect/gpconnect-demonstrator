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

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.InvalidDataException;
import org.rippleosi.common.service.AbstractEtherCISQueryStrategy;
import org.rippleosi.patient.procedures.model.ProcedureDetails;

/**
 */
public class EtherCISProcedureDetailsQueryStrategy extends AbstractEtherCISQueryStrategy<ProcedureDetails> {

    private final String procedureId;

    EtherCISProcedureDetailsQueryStrategy(String patientId, String procedureId) {
        super(patientId);
        this.procedureId = procedureId;
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return new StringBuilder()
            .append("SELECT ")
            .append("ehr.entry.composition_id as uid, ")
            .append("ehr.event_context.start_time as date_submitted, ")
            .append("ehr.party_identified.name as author, ")

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
                .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/description[at0001],/items[at0002 and name/value=''Procedure name''],/value,definingCode,terminologyId,value")
            .append("}' as procedure_terminology, ")

            .append("ehr.entry.entry #>> ")
            .append("'{")
                .append("/composition[openEHR-EHR-COMPOSITION.care_summary.v0 and name/value=''Procedures list''], ")
                .append("/content[openEHR-EHR-SECTION.procedures_rcp.v1],0, ")
                .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/description[at0001],/items[at0002 and name/value=''Procedure name''],/value,definingCode,codeString")
            .append("}' as procedure_code, ")

            .append("ehr.entry.entry #>> ")
            .append("'{")
                 .append("/composition[openEHR-EHR-COMPOSITION.care_summary.v0 and name/value=''Procedures list''], ")
                 .append("/content[openEHR-EHR-SECTION.procedures_rcp.v1],0, ")
                 .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/description[at0001],/items[at0049 and name/value=''Procedure notes''],/value,value")
            .append("}' as procedure_notes, ")

            .append("ehr.entry.entry #>> ")
            .append("'{")
                .append("/composition[openEHR-EHR-COMPOSITION.care_summary.v0 and name/value=''Procedures list''], ")
                .append("/content[openEHR-EHR-SECTION.procedures_rcp.v1],0, ")
                .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/time,/value,value")
            .append("}' as procedure_datetime, ")

            .append("ehr.entry.entry #>> ")
            .append("'{")
                .append("/composition[openEHR-EHR-COMPOSITION.care_summary.v0 and name/value=''Procedures list''], ")
                .append("/content[openEHR-EHR-SECTION.procedures_rcp.v1],0, ")
                .append("/items[openEHR-EHR-ACTION.procedure.v1],0,/ism_transition/current_state,/value,value")
            .append("}' as procedure_status, ")

            .append("ehr.event_context.start_time ")

            .append("FROM ehr.entry ")
            .append("INNER JOIN ehr.composition ON ehr.composition.id = ehr.entry.composition_id ")
            .append("INNER JOIN ehr.party_identified ON ehr.composition.composer = ehr.party_identified.id ")
            .append("INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id ")

            .append("WHERE ehr.composition.ehr_id = '").append(ehrId).append("' ")
            .append("AND ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.care_summary.v0' ")
            .append("AND ehr.entry.composition_id = '").append(procedureId).append("';")

            .toString();
    }

    @Override
    public ProcedureDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        if (resultSet.size() > 1) {
            throw new InvalidDataException("Too many results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new EtherCISProcedureDetailsTransformer().transform(data);
    }
}
