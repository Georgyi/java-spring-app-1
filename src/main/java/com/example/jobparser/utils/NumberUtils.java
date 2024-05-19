package com.example.jobparser.utils;

public class NumberUtils {
    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        return Math.min(value, max);
    }
}
