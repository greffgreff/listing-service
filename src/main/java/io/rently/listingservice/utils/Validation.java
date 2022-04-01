package io.rently.listingservice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

public class Validation {
    public static boolean canParseToReg(String value, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(value)
                .matches();
    }

    public static boolean canParseToTs(String value) {
        try {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1333125342 * 1000L));
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
