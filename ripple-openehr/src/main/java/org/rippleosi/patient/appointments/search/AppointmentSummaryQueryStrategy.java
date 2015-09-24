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
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "b_a/description/items[at0011]/value/value as service_team, " +
                "b_a/description/items[at0026]/value/lower/value as appointment_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains ACTION b_a[openEHR-EHR-ACTION.referral_uk.v1] " +
                "where a/name/value='Referral' ";
    }

    @Override
    public List<AppointmentSummary> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new AppointmentOnlyPredicate());

        return CollectionUtils.collect(filtered, new AppointmentSummaryTransformer(), new ArrayList<>());
    }
}
