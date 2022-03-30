package io.rently.listingservice.utils;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {

    public static Timestamp getCurrentTs() {
        return new Timestamp(System.currentTimeMillis());
    }
}
