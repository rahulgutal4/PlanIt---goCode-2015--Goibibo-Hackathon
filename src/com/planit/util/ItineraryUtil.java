package com.planit.util;

public class ItineraryUtil {
    public static boolean isEmpty(String s) {
        if (s == null || s.trim().equals("")) {
            return true;
        }
        return false;
    }
}
