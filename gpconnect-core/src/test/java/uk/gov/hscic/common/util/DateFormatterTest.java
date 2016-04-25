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
package uk.gov.hscic.common.util;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 */
public class DateFormatterTest {

    @Test
    public void shouldReturnNullForNull() {
        final Date date = DateFormatter.toDate(null);
        assertNull(date);
    }

    @Test
    public void shouldReturnNullForBogusDate() {
        final Date date = DateFormatter.toDate("Bogus");
        assertNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithNoTimeZone() {
        final Date date = DateFormatter.toDate("2015-08-24T13:06:38.012");
        assertNotNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithTwoDigitTimeZone() {
        final Date date = DateFormatter.toDate("2015-08-24T13:06:38.012+02");
        assertNotNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithFourDigitTimeZone() {
        final Date date = DateFormatter.toDate("2015-08-24T13:06:38.012+0200");
        assertNotNull(date);
    }

    @Test
    public void shouldParseISO8601DateWithColonSeparatedFourDigitTimeZone() {
        final Date date = DateFormatter.toDate("2015-08-24T13:06:38.012+02:00");
        assertNotNull(date);
    }

    @Test
    public void shouldReturnDateOnly() {
        final Date date = DateFormatter.toDateOnly("2015-08-24T13:06:38.012");
        assertNotNull(date);
        assertEquals("Mon Aug 24 00:00:00 BST 2015", date.toString());
    }

    @Test
    public void shouldReturnNullForNullWhenParsingDateOnly() {
        final Date date = DateFormatter.toDateOnly(null);
        assertNull(date);
    }

    @Test
    public void shouldReturnTimeOnly() {
        final Date date = DateFormatter.toTimeOnly("2015-08-24T13:06:38.012");
        assertNotNull(date);
        assertEquals("Thu Jan 01 13:06:38 GMT 1970", date.toString());
    }

    @Test
    public void shouldReturnNullForNullWhenParsingTimeOnly() {
        final Date date = DateFormatter.toTimeOnly(null);
        assertNull(date);
    }

    @Test
    public void shouldReturnDateInSpecificFormat() {
        final Date sourceDate = DateFormatter.toDate("2015-08-24T13:06:38.012");
        final String parsedDate = DateFormatter.toString(sourceDate);

        assertNotNull(parsedDate);
        assertEquals("2015-08-24T13:06:38.012+0100", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenConvertingDateToString() {
        final String parsedDate = DateFormatter.toString(null);
        assertNull(parsedDate);
    }

    @Test
    public void shouldReturnDateAsJSONDateString() {
        final Date sourceDate = DateFormatter.toDate("2015-08-24T13:06:38.012");
        final String parsedDate = DateFormatter.toJSONDateString(sourceDate);

        assertNotNull(parsedDate);
        assertEquals("2015-08-24T13:06:38.012Z", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenConvertingDateToJSONDateString() {
        final String parsedDate = DateFormatter.toJSONDateString(null);
        assertNull(parsedDate);
    }

    @Test
    public void shouldReturnCombinedDateAndTimeString() {
        final Date sourceDate = DateFormatter.toDateOnly("2015-08-24T13:06:38.012");
        final Date sourceTime= DateFormatter.toTimeOnly("2015-08-23T15:15:30.015");
        final String parsedDate = DateFormatter.combineDateTime(sourceDate, sourceTime);

        assertNotNull(parsedDate);
        assertEquals("2015-08-24T15:15:30.015+0100", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenCombiningDateAndTimeString() {
        final String parsedDate = DateFormatter.combineDateTime(null, null);
        assertNull(parsedDate);
    }
}
