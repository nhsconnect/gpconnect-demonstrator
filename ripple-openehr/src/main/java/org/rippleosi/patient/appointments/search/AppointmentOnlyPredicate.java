package org.rippleosi.patient.appointments.search;

import java.util.Map;

import org.apache.commons.collections4.Predicate;

/**
 */
public class AppointmentOnlyPredicate implements Predicate<Map<String, Object>> {

    @Override
    public boolean evaluate(Map<String, Object> input) {
        return input.get("appointment_date") != null && input.get("service_team") != null;
    }
}
