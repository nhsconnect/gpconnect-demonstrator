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

import uk.gov.hscic.common.util.DateFormatter;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 */
public class DateFormatterTest {

    @Test
    public void shouldReturnNullForNull() {
        Date date = DateFormatter.toDate(null);
        assertNull(date);
    }

    @Test
    public void shouldReturnNullForBogusDate() {
        Date date = DateFormatter.toDate("Bogus");
        assertNull(date);
    }

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

    @Test
    public void shouldReturnDateOnly() {
        Date date = DateFormatter.toDateOnly("2015-08-24T13:06:38.012");
        assertNotNull(date);
        assertEquals("Mon Aug 24 00:00:00 BST 2015", date.toString());
    }

    @Test
    public void shouldReturnNullForNullWhenParsingDateOnly() {
        Date date = DateFormatter.toDateOnly(null);
        assertNull(date);
    }

    @Test
    public void shouldReturnTimeOnly() {
        Date date = DateFormatter.toTimeOnly("2015-08-24T13:06:38.012");
        assertNotNull(date);
        assertEquals("Thu Jan 01 13:06:38 GMT 1970", date.toString());
    }

    @Test
    public void shouldReturnNullForNullWhenParsingTimeOnly() {
        Date date = DateFormatter.toTimeOnly(null);
        assertNull(date);
    }

    @Test
    public void shouldReturnDateInSpecificFormat() {
        Date sourceDate = DateFormatter.toDate("2015-08-24T13:06:38.012");
        String parsedDate = DateFormatter.toString(sourceDate);
        assertNotNull(parsedDate);
        assertEquals("2015-08-24T13:06:38.012+0100", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenConvertingDateToString() {
        String parsedDate = DateFormatter.toString(null);
        assertNull(parsedDate);
    }

    @Test
    public void shouldReturnDateAsJSONDateString() {
        Date sourceDate = DateFormatter.toDate("2015-08-24T13:06:38.012");
        String parsedDate = DateFormatter.toJSONDateString(sourceDate);
        assertNotNull(parsedDate);
        assertEquals("2015-08-24T13:06:38.012Z", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenConvertingDateToJSONDateString() {
        String parsedDate = DateFormatter.toJSONDateString(null);
        assertNull(parsedDate);
    }

    @Test
    public void shouldReturnCombinedDateAndTimeString() {
        Date sourceDate = DateFormatter.toDateOnly("2015-08-24T13:06:38.012");
        Date sourceTime= DateFormatter.toTimeOnly("2015-08-23T15:15:30.015");
        String parsedDate = DateFormatter.combineDateTime(sourceDate, sourceTime);
        assertNotNull(parsedDate);
        assertEquals("2015-08-24T15:15:30.015+0100", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenCombiningDateAndTimeString() {
        String parsedDate = DateFormatter.combineDateTime(null, null);
        assertNull(parsedDate);
    }
}
