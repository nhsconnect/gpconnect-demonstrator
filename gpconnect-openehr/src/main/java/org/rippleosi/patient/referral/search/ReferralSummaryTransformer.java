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

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public class ReferralSummaryTransformer implements Transformer<Map<String, Object>, ReferralSummary> {

    @Override
    public ReferralSummary transform(Map<String, Object> input) {

        Date dateOfReferral = DateFormatter.toDate(MapUtils.getString(input, "referral_date"));

        ReferralSummary referral = new ReferralSummary();
        referral.setSource("Marand");
        referral.setSourceId(MapUtils.getString(input, "uid"));
        referral.setReferralFrom(MapUtils.getString(input, "referral_from"));
        referral.setReferralTo(MapUtils.getString(input, "referral_to"));
        referral.setDateOfReferral(dateOfReferral);

        return referral;
    }
}
