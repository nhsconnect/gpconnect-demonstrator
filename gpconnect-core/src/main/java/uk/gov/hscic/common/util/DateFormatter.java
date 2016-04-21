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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 */
public final class DateFormatter {

    public static Date toDate(String input) {

        if (input == null) {
            return null;
        }

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));

        try {
            return DateUtils.parseDate(input, "yyyy-MM-dd",
                                       "dd-MM-yyyy",
                                       "yyyy-MM-dd'T'HH:mm:ss",
                                       "yyyy-MM-dd'T'HH:mm:ss'Z'",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSS",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSSX",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSSXX",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                       "HH:mm:ss",
                                       "yyyy.MM.dd",
                                       "yyyyMMdd");
        } catch (ParseException ignore) {
            return null;
        }
    }

    public static Date toDateOnly(final String input) {
        Date date = toDate(input);
        if (date != null) {
            date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

            return date;
        }

        return null;
    }

    public static Date toTimeOnly(String input) {
        Date date = toDate(input);
        if (date != null) {
            date = DateUtils.setYears(date,1970);
            date = DateUtils.setMonths(date,0);
            date = DateUtils.setDays(date,1);

            return date;
        }

        return null;
    }

    public static String toString(Date input) {
        if (input == null) {
            return null;
        }

        return DateFormatUtils.format(input, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public static String toJSONDateString(Date input) {
        if (input == null) {
            return null;
        }

        return DateFormatUtils.format(input, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public static String combineDateTime(Date date, Date time) {

        String dateAsString = toString(date);
        String timeAsString = toString(time);

        if (dateAsString == null || timeAsString == null) {
            return null;
        }

        return StringUtils.substringBefore(dateAsString, "T") + "T" + StringUtils.substringAfter(timeAsString, "T");
    }
}
