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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class DateFormatterTest {

    @Test
    public void shouldReturnNullForNull() {
        assertNull(DateFormatter.toDate(null));
    }

    @Test
    public void shouldReturnNullForBogusDate() {
        assertNull(DateFormatter.toDate("Bogus"));
    }

    @Test
    public void shouldParseISO8601DateWithNoTimeZone() {
        assertNotNull(DateFormatter.toDate("2015-08-24T13:06:38.012"));
    }

    @Test
    public void shouldParseISO8601DateWithTwoDigitTimeZone() {
        assertNotNull(DateFormatter.toDate("2015-08-24T13:06:38.012+02"));
    }

    @Test
    public void shouldParseISO8601DateWithFourDigitTimeZone() {
        assertNotNull(DateFormatter.toDate("2015-08-24T13:06:38.012+0200"));
    }

    @Test
    public void shouldParseISO8601DateWithColonSeparatedFourDigitTimeZone() {
        assertNotNull(DateFormatter.toDate("2015-08-24T13:06:38.012+02:00"));
    }

    @Test
    public void shouldReturnDateOnly() {
        final Date date = DateFormatter.toDateOnly("2015-08-24T13:06:38.012");
        assertNotNull(date);
        assertEquals("Mon Aug 24 00:00:00 BST 2015", date.toString());
    }

    @Test
    public void shouldReturnNullForNullWhenParsingDateOnly() {
        assertNull(DateFormatter.toDateOnly(null));
    }

    @Test
    public void shouldReturnTimeOnly() {
        final Date date = DateFormatter.toTimeOnly("2015-08-24T13:06:38.012");
        assertNotNull(date);
        assertEquals("Thu Jan 01 13:06:38 GMT 1970", date.toString());
    }

    @Test
    public void shouldReturnNullForNullWhenParsingTimeOnly() {
        assertNull(DateFormatter.toTimeOnly(null));
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
        assertNull(DateFormatter.toString(null));
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
        assertNull(DateFormatter.toJSONDateString(null));
    }

    @Test
    public void shouldReturnCombinedDateAndTimeString() {
        final Date sourceDate = DateFormatter.toDateOnly("2015-08-24T13:06:38.012");
        final Date sourceTime = DateFormatter.toTimeOnly("2015-08-23T15:15:30.015");
        final String parsedDate = DateFormatter.combineDateTime(sourceDate, sourceTime);

        assertNotNull(parsedDate);
        assertEquals("2015-08-24T15:15:30.015+0100", parsedDate);
    }

    @Test
    public void shouldReturnNullForNullWhenCombiningDateAndTimeString() {
        assertNull(DateFormatter.combineDateTime(null, null));
    }
}