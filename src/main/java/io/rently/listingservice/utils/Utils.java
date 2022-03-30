package io.rently.listingservice.utils;

import java.io.*;
import java.sql.Timestamp;
import java.util.Base64;

public class Utils {

    public static Timestamp getCurrentTs() {
        return new Timestamp(System.currentTimeMillis());
    }
}
