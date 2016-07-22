package com.sholay.app;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Utils {

    public static String cleanKeyword(String keyword) {
        return keyword.toLowerCase().trim();
    }

    public static Long tryParseLong(String str) {
        try {
            Long d = Long.parseLong(str);
            return d;
        }
        catch (Exception e){

        }
        return null;
    }
}
