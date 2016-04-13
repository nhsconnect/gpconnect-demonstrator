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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractEtherCISListQueryStrategy;
import org.rippleosi.patient.problems.model.ProblemHeadline;

/**
 */
public class EtherCISProblemHeadlineQueryStrategy extends AbstractEtherCISListQueryStrategy<ProblemHeadline> {

    EtherCISProblemHeadlineQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT " +
            "ehr.entry.composition_id as uid, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.problem_list.v1 and name/value=''Problem list''], " +
                    "/content[openEHR-EHR-SECTION.problems_issues_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.problem_diagnosis.v1],0,/data[at0001],/items[at0002 and name/value=''Problem/Diagnosis name''],/value,value" +
                "}' as problem, " +
            "ehr.event_context.start_time " +
            "FROM ehr.entry " +
            "INNER JOIN ehr.composition ON ehr.composition.id = ehr.entry.composition_id " +
            "INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id " +
            "WHERE ehr.composition.ehr_id = '" + ehrId + "' " +
            "AND ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.problem_list.v1' " +
            "ORDER BY ehr.event_context.start_time DESC;";
    }

    @Override
    public List<ProblemHeadline> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new EtherCISNullProblemHeadlinePredicate());

        return CollectionUtils.collect(filtered, new EtherCISProblemHeadlineTransformer(), new ArrayList<>());
    }
}
