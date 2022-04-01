package io.rently.listingservice.utils;

import java.sql.Timestamp;

public class Utils {

    public static Timestamp getCurrentTs() {
        return new Timestamp(System.currentTimeMillis());
    }
}
