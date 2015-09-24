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
