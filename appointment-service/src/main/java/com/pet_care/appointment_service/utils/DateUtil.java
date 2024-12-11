package com.pet_care.appointment_service.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    /**
     * Converts a Date object to a string representing the date in "yyyy-MM-dd" format.
     * @param date The Date object to be formatted.
     * @return A string representing the date in "yyyy-MM-dd" format.
     */
    public static String getDateOnly(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Format to 'yyyy-MM-dd'
        return formatter.format(date); // Return formatted date as string
    }

    /**
     * Converts a Date object to a string representing the time in "HH:mm" format.
     * @param date The Date object to be formatted.
     * @return A string representing the time in "HH:mm" format.
     */
    public static String getTimeOnly(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm"); // Format to 'HH:mm'
        return formatter.format(date); // Return formatted time as string
    }

    /**
     * Adds a specified number of days to the given Date and returns the resulting Date.
     * @param date The original Date object to which days will be added.
     * @param number The number of days to add to the date.
     * @return A new Date object with the days added.
     */
    public static Date plusDate(Date date, int number) {
        // Convert the Date to LocalDate and add the specified number of days
        LocalDate localDate = LocalDate.now().plusDays(number);

        // Convert the LocalDate back to Date and return it
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
