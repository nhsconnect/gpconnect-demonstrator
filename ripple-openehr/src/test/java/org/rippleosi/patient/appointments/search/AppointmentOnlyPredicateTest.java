package org.rippleosi.patient.appointments.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 */
public class AppointmentOnlyPredicateTest {

    private AppointmentOnlyPredicate predicate;

    @Before
    public void setUp() throws Exception {
        predicate = new AppointmentOnlyPredicate();
    }

    @Test
    public void shouldReturnFalseWhenAppointmentDateIsMissing() {

        Map<String, Object> input = Collections.singletonMap("service_team", "team");

        boolean result = predicate.evaluate(input);

        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenServiceTeamIsMissing() {

        Map<String, Object> input = Collections.singletonMap("appointment_date", "appointment");

        boolean result = predicate.evaluate(input);

        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenBothEntriesAreMissing() {

        Map<String, Object> input = Collections.emptyMap();

        boolean result = predicate.evaluate(input);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenBothEntriesArePresent() {

        Map<String, Object> input = new HashMap<>();
        input.put("service_team", "team");
        input.put("appointment_date", "appointment");

        boolean result = predicate.evaluate(input);

        assertTrue(result);
    }
}
