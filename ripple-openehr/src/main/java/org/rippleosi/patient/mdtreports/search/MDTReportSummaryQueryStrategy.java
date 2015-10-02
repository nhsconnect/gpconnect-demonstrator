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
package org.rippleosi.patient.mdtreports.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;

/**
 */
public class MDTReportSummaryQueryStrategy extends AbstractListQueryStrategy<MDTReportSummary> {

    MDTReportSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/context/start_time/value as date_created, " +
                "b_a/items/protocol/items[at0011]/value/value as service_team, " +
                "b_a/items/activities[at0001]/timing/value as meeting_date " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report.v1] " +
                "contains ( " +
                "    SECTION b_a[openEHR-EHR-SECTION.referral_details_rcp.v1] and " +
                "    SECTION b_b[openEHR-EHR-SECTION.history_rcp.v1] and " +
                "    SECTION b_c[openEHR-EHR-SECTION.plan_requested_actions_rcp.v1]) " +
                "where a/name/value='MDT Output Report' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public List<MDTReportSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new MDTReportSummaryTransformer(), new ArrayList<>());
    }
}
