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
package org.rippleosi.patient.appointments.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.appointments.model.AppointmentSummary;

/**
 */
public class AppointmentSummaryQueryStrategy extends AbstractListQueryStrategy<AppointmentSummary> {

    AppointmentSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "b_a/description/items[at0011]/value/value as service_team, " +
                "b_a/description/items[at0026]/value/lower/value as appointment_date " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.referral_uk.v1] " +
                "where a/name/value='Referral' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public List<AppointmentSummary> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new AppointmentOnlyPredicate());

        return CollectionUtils.collect(filtered, new AppointmentSummaryTransformer(), new ArrayList<>());
    }
}
