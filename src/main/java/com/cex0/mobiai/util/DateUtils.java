package com.cex0.mobiai.util;

import org.springframework.lang.NonNull;

import java.util.Date;

public class DateUtils {

    private DateUtils() {}

    @NonNull
    public static Date now() {
        return new Date();
    }
}
