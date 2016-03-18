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
package org.rippleosi.patient.problems.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractEtherCISQueryStrategy;
import org.rippleosi.patient.problems.model.ProblemDetails;

/**
 */
public class EtherCISProblemDetailsQueryStrategy extends AbstractEtherCISQueryStrategy<ProblemDetails> {

    private final String problemId;

    EtherCISProblemDetailsQueryStrategy(String patientId, String problemId) {
        super(patientId);
        this.problemId = problemId;
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT ehr.entry.composition_id as uid, " +
            "ehr.event_context.start_time as date_created, " +
            "ehr.party_identified.name as author, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.problem_list.v1 and name/value=''Problem list''], " +
                    "/content[openEHR-EHR-SECTION.problems_issues_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.problem_diagnosis.v1],0,/data[at0001],/items[at0002 and name/value=''Problem/Diagnosis name''],/value,value" +
                "}' as problem, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.problem_list.v1 and name/value=''Problem list''], " +
                    "/content[openEHR-EHR-SECTION.problems_issues_rcp.v1],0, /items[openEHR-EHR-EVALUATION.problem_diagnosis.v1],0, " +
                    "/data[at0001],/items[at0002 and name/value=''Problem/Diagnosis name''],/value,definingCode,codeString" +
                "}' as problem_code, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.problem_list.v1 and name/value=''Problem list''], " +
                    "/content[openEHR-EHR-SECTION.problems_issues_rcp.v1],0, /items[openEHR-EHR-EVALUATION.problem_diagnosis.v1],0, " +
                    "/data[at0001],/items[at0002 and name/value=''Problem/Diagnosis name''],/value,definingCode,terminologyId,value" +
                "}' as problem_terminology, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.problem_list.v1 and name/value=''Problem list''], " +
                    "/content[openEHR-EHR-SECTION.problems_issues_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.problem_diagnosis.v1],0,/data[at0001],/items[at0077 and name/value=''Date/time of onset''],/value,value" +
                "}' as onset_date, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.problem_list.v1 and name/value=''Problem list''], " +
                    "/content[openEHR-EHR-SECTION.problems_issues_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.problem_diagnosis.v1],0,/data[at0001],/items[at0009 and name/value=''Clinical description''],/value,value" +
                "}' as description " +
            "FROM ehr.entry " +
            "INNER JOIN ehr.composition ON ehr.composition.id = ehr.entry.composition_id " +
            "INNER JOIN ehr.party_identified ON ehr.composition.composer = ehr.party_identified.id " +
            "INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id " +
            "WHERE (ehr.composition.ehr_id = '" + ehrId + "') " +
            "AND (ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.problem_list.v1') " +
            "AND (ehr.entry.composition_id = '" + problemId + "');";
    }

    @Override
    public ProblemDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new EtherCISProblemDetailsTransformer().transform(data);
    }
}
