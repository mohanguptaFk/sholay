package com.sholay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
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

    // Read all lines ina file in a list.
    public static List<String> readAllLines(String filePath) {
        List<String> list = new ArrayList<String>();
        // read from the file.
        try {
            String sCurrentLine = null;
            BufferedReader br = new BufferedReader(new java.io.FileReader(filePath));
            while ((sCurrentLine = br.readLine()) != null) {
                sCurrentLine = sCurrentLine.trim();
                if (!sCurrentLine.isEmpty())
                    list.add(sCurrentLine);
            }
        }
        catch (Exception e) {
            LOG.error(e.toString());
            e.printStackTrace();
            throw new NullPointerException();
        }

        return list;
    }
}
