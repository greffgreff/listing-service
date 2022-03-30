package io.rently.listingservice.utils;

public class Broadcaster {
    private static final String PREFIX = "[LISTING ENDPOINT]";

    public static void debug(Object obj) {
        System.out.println(String.format("%-24s ", Utils.getCurrentTs()) + PREFIX + String.format(" %13s ", "[DEBUG]") + obj);
    }

    public static void info(Object obj) {
        System.out.println(String.format("%-24s ", Utils.getCurrentTs()) + PREFIX + String.format(" %13s ", "[INFO]") + obj);
    }

    public static void warn(Object obj) {
        System.out.println(String.format("%-24s ", Utils.getCurrentTs()) + PREFIX + String.format(" %13s ", "[WARN]") + obj);
    }

    public static void error(Object obj) {
        System.out.println(String.format("%-24s ", Utils.getCurrentTs()) + PREFIX + String.format(" %13s ", "[ERROR]") + obj);
    }
}
