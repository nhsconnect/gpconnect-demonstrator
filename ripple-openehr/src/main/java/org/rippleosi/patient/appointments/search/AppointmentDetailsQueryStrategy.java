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

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.appointments.model.AppointmentDetails;

/**
 */
public class AppointmentDetailsQueryStrategy extends AbstractQueryStrategy<AppointmentDetails> {

    private final String appointmentId;

    AppointmentDetailsQueryStrategy(String patientId, String appointmentId) {
        super(patientId);
        this.appointmentId = appointmentId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "b_a/description/items[at0011]/value/value as service_team, " +
                "b_a/description/items[at0026]/value/lower/value as appointment_date, " +
                "b_a/protocol/items/items/items[at0002]/value/value as location, " +
                "b_a/ism_transition/current_state/value as status " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.referral_uk.v1] " +
                "where a/name/value='Referral' " +
                "and a/uid/value = '" + appointmentId + "' " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
    }

    @Override
    public AppointmentDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new AppointmentDetailsTransformer().transform(data);
    }
}
