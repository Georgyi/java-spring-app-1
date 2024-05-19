package com.example.jobparser.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static Long extractFirstNumber(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Long.parseLong(matcher.group());
        }

        return null;
    }
}
