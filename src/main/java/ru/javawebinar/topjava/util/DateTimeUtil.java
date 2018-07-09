package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetween(T tested, T start, T end) {
        return tested.compareTo(start) >= 0 && tested.compareTo(end) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate toLocalDate(String localDate) {
        try {
            return LocalDate.parse(localDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalTime toLocalTime(String localTime) {
        try {
            return LocalTime.parse(localTime);
        } catch (Exception e) {
            return null;
        }
    }
}
