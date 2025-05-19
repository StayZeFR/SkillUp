package fr.skillup.core.utils;

import java.sql.Date;

public class DateUtils {

    /**
     * Convertie la date au format yyyy-MM-dd
     *
     * @param date : date au format dd/MM/yyyy
     * @return Date : date au format yyyy-MM-dd
     */
    public static Date toDate(String date) {
        if (date.contains("/")) {
            String[] parts = date.split("/");
            date = parts[2] + "-" + parts[1] + "-" + parts[0];
        }
        return Date.valueOf(date);
    }

}
