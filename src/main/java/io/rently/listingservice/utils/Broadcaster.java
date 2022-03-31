package io.rently.listingservice.utils;

import org.springframework.web.server.ResponseStatusException;

public class Broadcaster {
    private static final String PREFIX = "[LISTING ENDPOINT]";

    public static void debug(Object obj) {
        System.out.println(defaultFormat("[DEBUG]") + obj);
    }

    public static void info(Object obj) {
        System.out.println(defaultFormat("[INFO]") + obj);
    }

    public static void warn(Object obj) {
        System.out.println(defaultFormat("[WARN]") + obj);
    }

    public static void error(Object obj) {
        System.out.println(defaultFormat("[ERROR]") + obj);
    }

    public static void httpError(ResponseStatusException ex) {
        System.out.println(defaultFormat("[ERROR]") + "[" + ex.getStatus() + "] " + ex.getReason());
    }

    private static String defaultFormat(String type) {
        return String.format("%-24s ", Utils.getCurrentTs()) + PREFIX + String.format(" %13s ", type);
    }
}
