package com.pet_care.appointment_service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getDateOnly(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String getTimeOnly(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }
}
