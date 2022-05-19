package io.rently.listingservice.utils;

import java.util.Date;
import java.util.UUID;

public class Validation {

    private Validation() { }

    public static boolean canParseToTs(String value) {
        try {
            new Date(Long.parseLong(value));
            return true;
        }
        catch (Exception ignore) {
            return false;
        }
    }

    public static UUID tryParseUUID(String value) {
        try {
            return UUID.fromString(value);
        }
        catch(Exception ignore) {
            return null;
        }
    }

    public static boolean canParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch(Exception ignore) {
            return false;
        }
    }

    public static boolean canParseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        }
        catch(Exception ignore) {
            return false;
        }
    }

    public static boolean canParseNumeric(String value) {
        return (canParseFloat(value) || canParseInt(value));
    }
}
