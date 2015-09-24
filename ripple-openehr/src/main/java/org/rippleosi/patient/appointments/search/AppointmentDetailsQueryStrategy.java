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
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "b_a/description/items[at0011]/value/value as service_team, " +
                "b_a/description/items[at0026]/value/lower/value as appointment_date, " +
                "b_a/protocol/items/items/items[at0002]/value/value as location, " +
                "b_a/ism_transition/current_state/value as status " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.referral_uk.v1] " +
                "where a/name/value='Referral' " +
                "and a/uid/value = '" + appointmentId + "'";
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
