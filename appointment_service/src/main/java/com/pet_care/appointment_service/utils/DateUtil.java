package com.pet_care.appointment_service.utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * @param date
     * @return
     */
    @NotNull
    public static String getDateOnly(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * @param date
     * @return
     */
    @NotNull
    public static String getTimeOnly(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }
}
