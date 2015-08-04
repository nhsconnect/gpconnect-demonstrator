package net.nhs.esb.util;

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

    private DateFormatter() {
        // Prevent construction
    }

    public static Date toDate(String input) {
        try {
            return DateUtils.parseDate(input, "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSSX");
        } catch (ParseException ignore) {
            return null;
        }
    }

    public static Date toDateOnly(String input) {
        Date date = toDate(input);
        if (date != null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.setTime(date);

            calendar = DateUtils.truncate(calendar, Calendar.DATE);

            return calendar.getTime();
        }

        return null;
    }

    public static Date toTimeOnly(String input) {
        Date date = toDate(input);
        if (date != null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.setTime(date);

            calendar.set(Calendar.YEAR, 1970);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, 1);

            return calendar.getTime();
        }

        return null;
    }

    public static String toString(Date input) {
        return DateFormatUtils.format(input, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public static String combineDateTime(Date date, Date time) {

        String dateAsString = toString(date);
        String timeAsString = toString(time);

        return StringUtils.substringBefore(dateAsString, "T") + "T" + StringUtils.substringAfter(timeAsString, "T");
    }
}
