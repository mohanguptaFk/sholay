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

    public static Double tryParseDouble(String str) {
        try {
            Double d = Double.parseDouble(str);
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

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
