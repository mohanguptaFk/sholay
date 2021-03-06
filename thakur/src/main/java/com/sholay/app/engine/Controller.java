package com.sholay.app.engine;


import com.sholay.app.datamodel.OutputActivity;
import com.sholay.app.datamodel.SuperTags;
import com.sholay.app.datamodel.TagsKeywords;
import com.sholay.app.datamodel.UserInfo;

import com.sholay.app.datamodel.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.text.SimpleDateFormat;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static com.sholay.app.datamodel.ActivityStore.dateInMilis;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Controller {

    public static String today = "22-07-2016";
    public static String tomorow = "23-07-2016";

    public static  long oneHour = 60*60*1000;

    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static long twoHour = 2 * oneHour;

    public static void init() {
        TagsKeywords.getInstance().init();
        initActivities();
        ActivityStore.getInstance().buildIndex();
        //read all the events.
        // get the tagging done on each of those events.
        // and build activity store.
    }

    private static void initActivities() {
        try {
            BufferedReader br1 = new BufferedReader(new FileReader("scrapyData.csv"));
            String line1;
            int count = 0;
            while (true) {
                line1 = br1.readLine();
                if (line1 != null && line1.contains("\t")) {
                    line1 = line1.replace("\n","");
                    String[] line1Splits = line1.split("\t");
                    if (line1Splits.length > 5) {
                        String title ;
                        String type = line1Splits[5];
                        ActivityTypes ac = getActivityType(type);
                        Map<String,String> info = new HashMap<String, String>();
                        title = line1Splits[0];
                        String image = line1Splits[1];
                        List<String> tags = getTags(line1Splits[2]);
                        String location = line1Splits[3];
                        long time;
                        try {
                            time = getStartTime(ac, line1Splits[4]);
                        } catch (ParseException e) {
                            time = System.currentTimeMillis();
                        }
                        if (line1Splits.length > 6) {
                            String formattedLine = line1Splits[6].replaceAll("'", "\"");
                            info = objectMapper.readValue(formattedLine, Map.class);
                        }
                        count ++;
                        String eventlink = null;
                        String imageP = null;
                        String address= getdaddr(location);
                        String lat = getLat(location);
                        String lang = getLang(location);
                        long duration = getduration(ac, info);
                        long endtime = System.currentTimeMillis();
                        Activity activity = new Activity(count, ac, title, time, endtime, duration, lat, lang, address, imageP, image, eventlink, tags);

                        try {
                            ActivityStore.getInstance().addActivity(activity);
                            Tagger.getInstance().tag(activity);
                        } catch (ParseException e) {

                        }
                    }
                } else {
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("error");
        }


    }

    private static long getduration(ActivityTypes ac, Map<String, String> info) {
        String dur = info.get("duration");
        if (dur == null) {
            if (ac.equals(ActivityTypes.MOVIE)) {
                return 2 * 60 * 60 * 1000; // 3hours
            } else if (ac.equals(ActivityTypes.PLACE)) {
                return (long) (1.5 * 60 * 60 * 1000); // 1.5hours;
            }
             else {
                return 60 * 60 * 1000;
            }
        }
        return 60 * 60 * 1000;
    }

    private static long getStartTime(ActivityTypes ac, String line1Split) throws ParseException {
        if ( line1Split  != null) {
            try {
                line1Split = line1Split.replaceAll( "[^\\d/:]", " " );
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    Date date = sdf.parse(line1Split);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    return calendar.getTimeInMillis();
            } catch (ParseException e) {
                return System.currentTimeMillis();
            }
        } else {
            if (ac.equals(ActivityTypes.PLACE)) {
                return dateInMilis(today + " 10:00:00");
            }
        }

        return System.currentTimeMillis();
    }

    private static List<String> getTags(String line) {
        String[] splits;
        List<String> list = new ArrayList<String>();
        if (line.contains("[")) {
            line = line.replace("[","");
            line = line.replace("]","");
            line = line.replace("\"","");
            splits = line.trim().split(",");
        } else if (line.contains(",")) {
            splits = line.split(",");
        } else {
            list.add(line) ;
            return list;
        }
        for (String s : splits) {
            list.add(s.trim().toLowerCase());
        }
        return list;
    }

    private static String getdaddr(String location) {
        if (!location.contains("{") && location.contains("daddr")) {
            String[] re = location.split("daddr=");
            return re[1];
        } else {
            return location;
        }
    }

    private static String getLang(String location) {
        if (location.contains("{")) {
            try {
                Map<String,String> data = objectMapper.readValue(location, Map.class);
                return data.get("longitude");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getLat(String location) {
        if (location.contains("{")) {
            try {
                Map<String,String> data = objectMapper.readValue(location, Map.class);
                return data.get("latitude");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ActivityTypes getActivityType(String type) {
        if (type.equals("places")) {
            return ActivityTypes.PLACE;
        } else if (type.equals("restaurant")) {
            return ActivityTypes.EATING;
        } else if (type.equals("movie")) {
            return ActivityTypes.MOVIE;
        }
        return ActivityTypes.OTHERS;
    }


    public static void main(String[] args) {
        initActivities();
        ActivityStore.getInstance().buildIndex();
        System.out.println(ActivityStore.getInstance().getActivity("indian").toString());
    }

    //input all the user level params and the output is json.
    public static String getEventsResponse(long startTime, long endTime, String lat, String lng, String inout, String chillAdvent, String familyElse, String[] tags) {

        UserInfo userInfo = new UserInfo();
        userInfo.endTime = endTime;
        userInfo.startTime = startTime;

        userInfo.lat = lat;
        userInfo.lng = lng;

        SuperTags.SuperTagsTypes type = SuperTags.SuperTagsTypes.INDOOR;
        if (inout!= null && inout.equalsIgnoreCase("outdoor")) {
            type = SuperTags.SuperTagsTypes.OUTDOOR;
        }
        userInfo.addSuperTag(type);

        type = SuperTags.SuperTagsTypes.ADVENTURE;
        if (chillAdvent!= null && chillAdvent.equalsIgnoreCase("chill")) {
            type = SuperTags.SuperTagsTypes.CHILL;
        }
        userInfo.addSuperTag(type);


        type = SuperTags.SuperTagsTypes.FAMILY;
        if (familyElse!= null && familyElse.equalsIgnoreCase("friends")) {
            type = SuperTags.SuperTagsTypes.FRIENDS;
        }
        else if (familyElse!= null &&familyElse.equalsIgnoreCase("single")) {
            type = SuperTags.SuperTagsTypes.SINGLE;
        }
        else if (familyElse!= null &&familyElse.equalsIgnoreCase("couples")) {
            type = SuperTags.SuperTagsTypes.COUPLES;
        }
        userInfo.addSuperTag(type);


        if (tags != null) {
            for (String tag : tags) {
                userInfo.addTag(tag, 1.0);
            }
        }


        List<OutputActivity> outputActivityList = new Recommender(userInfo).recommend();

        //json serialize.
        String serialized = "";
        try {
            serialized = objectMapper.writeValueAsString(outputActivityList);
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }

        return serialized;
    }
}
