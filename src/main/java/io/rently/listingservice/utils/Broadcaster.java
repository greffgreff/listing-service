package io.rently.listingservice.utils;

public class Broadcaster {
    private static final String PREFIX = "[LISTING ENDPOINT] ";

    public static void debug(Object obj) {
        System.out.println(PREFIX + Utils.getCurrentTs() + " [DEBUG] " + obj);
    }

    public static void info(Object obj) {
        System.out.println(PREFIX + Utils.getCurrentTs() + " [INFO] " + obj);
    }

    public static void warn(Object obj) {
        System.out.println(PREFIX + Utils.getCurrentTs() + " [WARN] " + obj);
    }

    public static void error(Object obj) {
        System.out.println(PREFIX + Utils.getCurrentTs() + " [ERROR] " + obj);
    }
}
