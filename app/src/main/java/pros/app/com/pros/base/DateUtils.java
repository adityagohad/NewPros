package pros.app.com.pros.base;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String getDateDifference(String createdAt, boolean getFullText) {

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date createdAtDate = new Date();
        Date currentDate = new Date();

        try {
            createdAtDate = format.parse(createdAt);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        DateTime dt1 = new DateTime(createdAtDate);
        DateTime dt2 = new DateTime(currentDate);

        int yearsAgo = Years.yearsBetween(dt1, dt2).getYears();
        int monthsAgo = Months.monthsBetween(dt1, dt2).getMonths();
        int weeksAgo = Weeks.weeksBetween(dt1, dt2).getWeeks();
        int daysAgo = Days.daysBetween(dt1, dt2).getDays();
        int hoursAgo = Hours.hoursBetween(dt1, dt2).getHours() % 24;
        int minutesAgo = Minutes.minutesBetween(dt1, dt2).getMinutes() % 60;
        int secondsAgo = Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;

        if (yearsAgo > 0) {
            return getFullText ? yearsAgo + " YEAR AGO" : yearsAgo + "Y";
        } else if (weeksAgo > 0) {
            return getFullText ? weeksAgo + (weeksAgo > 1 ? " WEEKS AGO" : " WEEK AGO") : weeksAgo + "W";
        } else if (daysAgo > 0) {
            return getFullText ? daysAgo + (daysAgo > 1 ? " DAYS AGO" : " DAY AGO") : daysAgo + "D";
        } else if (hoursAgo > 0) {
            return getFullText ? hoursAgo + (hoursAgo > 1 ? " HOURS AGO" : " HOUR AGO") : hoursAgo + "H";
        } else if (minutesAgo > 0) {
            return getFullText ? minutesAgo + (minutesAgo > 1 ? " MINUTES AGO" : " MINUTE AGO") : minutesAgo + "M";
        } else if (secondsAgo > 0) {
            return getFullText ? secondsAgo + (secondsAgo > 1 ? " SECONDS AGO" : "SECOND AGO") : secondsAgo + "S";
        }

        return null;
    }
}
