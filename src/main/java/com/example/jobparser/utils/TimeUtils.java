package com.example.jobparser.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static LocalDateTime parseDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter).atStartOfDay();
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime getDateSubtractTimeMessage(String timeMessage) {
        LocalDateTime time = LocalDateTime.now();

        var timeMessageLowerCase = timeMessage.toLowerCase();
        var count = TextUtils.extractFirstNumber(timeMessage);

        if (count == null) return time;

        if (timeMessageLowerCase.contains("minute")) {
            time = time.minusMinutes(count);
        }

        if (timeMessageLowerCase.contains("hour")) {
            time = time.minusHours(count);
        }

        if (timeMessageLowerCase.contains("day")) {
            time = time.minusDays(count);
        }

        if (timeMessageLowerCase.contains("week")) {
            time = time.minusWeeks(count);
        }

        if (timeMessageLowerCase.contains("month")) {
            time = time.minusMonths(count);
        }

        if (timeMessageLowerCase.contains("year")) {
            time = time.minusYears(count);
        }

        return time;
    }
}
