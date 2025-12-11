package com.stockia.stockia.utils;

public class NotificationMessageUtils {

    public static String extractTitle(String fullMessage) {
        if (fullMessage == null || fullMessage.isBlank()) return null;

        String[] parts = fullMessage.split(" - ", 2);
        return parts[0].trim();
    }

    public static String extractMessage(String fullMessage) {
        if (fullMessage == null || fullMessage.isBlank()) return null;

        String[] parts = fullMessage.split(" - ", 2);
        return parts.length > 1 ? parts[1].trim() : fullMessage.trim();
    }
}
