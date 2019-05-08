package com.example.appdasfinal.utils;

public class Utils {

    public static boolean requireNotNull(Object... objects){
        for (Object o : objects){
            if (o == null){
                return false;
            }
        }
        return true;
    }
}
