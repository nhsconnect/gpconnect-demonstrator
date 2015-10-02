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
package org.rippleosi.patient.careplans.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.careplans.model.CarePlanDetails;

/**
 */
public class CarePlanDetailsQueryStrategy extends AbstractQueryStrategy<CarePlanDetails> {

    private final String carePlanId;

    CarePlanDetailsQueryStrategy(String patientId, String carePlanId) {
        super(patientId);
        this.carePlanId = carePlanId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "b_a/items[openEHR-EHR-EVALUATION.care_preference_uk.v1]/data/items[at0003]/value/value as priority_place_of_care, " +
                "b_a/items[openEHR-EHR-EVALUATION.care_preference_uk.v1]/data/items[at0015]/value/value as priority_place_of_death, " +
                "b_a/items[openEHR-EHR-EVALUATION.care_preference_uk.v1]/data/items[at0029]/value/value as priority_comment, " +
                "b_a/items[openEHR-EHR-EVALUATION.advance_decision_refuse_treatment_uk.v1]/data/items[at0003]/value/value as treatment_decision, " +
                "b_a/items[openEHR-EHR-EVALUATION.advance_decision_refuse_treatment_uk.v1]/data/items[at0002]/value/value as treatment_date_of_decision, " +
                "b_a/items[openEHR-EHR-EVALUATION.advance_decision_refuse_treatment_uk.v1]/data/items[at0021]/value/value as treatment_comment, " +
                "b_a/items[openEHR-EHR-EVALUATION.cpr_decision_uk.v1]/data/items[at0003]/value/value as cpr_decision, " +
                "b_a/items[openEHR-EHR-EVALUATION.cpr_decision_uk.v1]/data/items[at0002]/value/value as cpr_date_of_decision, " +
                "b_a/items[openEHR-EHR-EVALUATION.cpr_decision_uk.v1]/data/items[at0021]/value/value as cpr_comment " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_plan.v1] " +
                "contains SECTION b_a[openEHR-EHR-SECTION.legal_information_rcp.v1] " +
                "where a/name/value='End of Life Patient Preferences' " +
                "and a/uid/value='" + carePlanId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public CarePlanDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new CarePlanDetailsTransformer().transform(data);
    }
}
