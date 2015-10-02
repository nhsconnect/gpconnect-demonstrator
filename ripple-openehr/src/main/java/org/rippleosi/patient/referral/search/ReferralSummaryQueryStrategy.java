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
package org.rippleosi.patient.referral.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public class ReferralSummaryQueryStrategy extends AbstractListQueryStrategy<ReferralSummary> {

    ReferralSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a_a/items/activities/timing/value as referral_date, " +
                "a_a/items/protocol/items/items/value/value as referral_from, " +
                "a_a/items/activities/description/items[at0121]/value/value as referral_to " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.referral_details_rcp.v1] " +
                "where a/name/value='Referral' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public List<ReferralSummary> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new ReferralOnlyPredicate());

        return CollectionUtils.collect(filtered, new ReferralSummaryTransformer(), new ArrayList<>());
    }
}
