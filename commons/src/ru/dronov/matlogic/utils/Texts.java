package ru.dronov.matlogic.utils;

public class Texts {

    public static boolean isEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLower(int ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }
}
