package org.rippleosi.common.util;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

/**
 */
public class DateFormatterTest {

    @Test
    public void shouldParseISO8601DateWithNoTimeZone() {
        Date date = DateFormatter.toDate("2015-08-24T13:06:38.012");
        assertNotNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithTwoDigitTimeZone() {
        Date date = DateFormatter.toDate("2015-08-24T13:06:38.012+02");
        assertNotNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithFourDigitTimeZone() {
        Date date = DateFormatter.toDate("2015-08-24T13:06:38.012+0200");
        assertNotNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithColonSeparatedFourDigitTimeZone() {
        Date date = DateFormatter.toDate("2015-08-24T13:06:38.012+02:00");
        assertNotNull(date);
    }
}
