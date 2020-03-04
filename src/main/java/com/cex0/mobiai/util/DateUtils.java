package com.cex0.mobiai.util;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Date utilities
 *
 * @author wodenvyoujiaoshaxiong
 * @date 2020/01/11
 */
public class DateUtils {

    private DateUtils() {}

    @NonNull
    public static Date now() {
        return new Date();
    }

    public static Calendar converTo(@NonNull Date date) {
        Assert.notNull(date, "Date must not be null");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date add(@NonNull Date date,long time, @NonNull TimeUnit timeUnit) {
        Assert.notNull(date, "Date must not be null");
        Assert.isTrue(time >= 0, "Addition time must not be less than 1");
        Assert.notNull(timeUnit, "Time Unit must not be null");

        Date result;

        int timeIntvalue;

        if (time > Integer.MAX_VALUE) {
            timeIntvalue = Integer.MAX_VALUE;
        }
        else {
            timeIntvalue = Long.valueOf(time).intValue();
        }

        // 计算到期时间
        switch (timeUnit) {
            case DAYS:
                result = org.apache.commons.lang3.time.DateUtils.addDays(date, timeIntvalue);
                break;
            case HOURS:
                result = org.apache.commons.lang3.time.DateUtils.addHours(date, timeIntvalue);
                break;
            case MINUTES:
                result = org.apache.commons.lang3.time.DateUtils.addMinutes(date, timeIntvalue);
                break;
            case SECONDS:
                result = org.apache.commons.lang3.time.DateUtils.addSeconds(date, timeIntvalue);
                break;
            case MILLISECONDS:
                result = org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, timeIntvalue);
                break;
            default:
                result = date;
        }
        return result;
    }
}
