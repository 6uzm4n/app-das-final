package com.example.appdasfinal.utils;

import android.util.Base64;

public class Utils {

    public static String getBasicAuth(String email, String password) {
        String authString = email + ":" + password;
        byte[] authStringEnc = Base64.encode(authString.getBytes(), Base64.DEFAULT);
        String result = "Basic " + new String(authStringEnc);
        while (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static boolean requireNotNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }
}
