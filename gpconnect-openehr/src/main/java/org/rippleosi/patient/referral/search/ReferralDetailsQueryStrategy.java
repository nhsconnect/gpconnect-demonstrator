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

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.InvalidDataException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.referral.model.ReferralDetails;

/**
 */
public class ReferralDetailsQueryStrategy extends AbstractQueryStrategy<ReferralDetails> {

    private final String referralId;

    ReferralDetailsQueryStrategy(String patientId, String referralId) {
        super(patientId);
        this.referralId = referralId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_submitted, " +
                "a_a/items/activities/timing/value as referral_date, " +
                "a_a/items/protocol/items/items/value/value as referral_from, " +
                "a_a/items/activities/description/items[at0121]/value/value as referral_to, " +
                "a_a/items/activities/description/items[at0062]/value/value as referral_reason, " +
                "a_a/items/activities/description/items[at0064]/value/value as clinical_summary " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.referral_details_rcp.v1] " +
                "where a/name/value='Referral' " +
                "and a/uid/value='" + referralId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public ReferralDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        if (resultSet.size() > 1) {
            throw new InvalidDataException("Too many results found");
        }

        Map<String,Object> data = resultSet.get(0);

        return new ReferralDetailsTransformer().transform(data);
    }
}
