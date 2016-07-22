package com.sholay.app.engine;

import com.sholay.app.datamodel.OutputActivity;
import com.sholay.app.datamodel.SuperTags;
import com.sholay.app.datamodel.UserInfo;
import com.sun.tools.javac.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Controller {

    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static void init() {
        //read all the events.
        // get the tagging done on each of those events.
        // and build activity store.
    }

    //input all the user level params and the output is json.
    public static String getEventsResponse(long startTime, long endTime, String lat, String lng, String inout, String chillAdvent, String familyElse, String[] keywords) {

        UserInfo userInfo = new UserInfo();
        userInfo.endTime = endTime;
        userInfo.startTime = startTime;

        userInfo.lat = lat;
        userInfo.lng = lng;

        SuperTags.SuperTagsTypes type = SuperTags.SuperTagsTypes.INDOOR;
        if (inout.equalsIgnoreCase("outdoor")) {
            type = SuperTags.SuperTagsTypes.OUTDOOR;
        }
        userInfo.addSuperTag(type);

        type = SuperTags.SuperTagsTypes.ADVENTURE;
        if (inout.equalsIgnoreCase("chill")) {
            type = SuperTags.SuperTagsTypes.CHILL;
        }
        userInfo.addSuperTag(type);


        type = SuperTags.SuperTagsTypes.FAMILY;
        if (inout.equalsIgnoreCase("friends")) {
            type = SuperTags.SuperTagsTypes.FRIENDS;
        }
        else if (inout.equalsIgnoreCase("single")) {
            type = SuperTags.SuperTagsTypes.SINGLE;
        }
        else if (inout.equalsIgnoreCase("couples")) {
            type = SuperTags.SuperTagsTypes.COUPLES;
        }
        userInfo.addSuperTag(type);

        Map<String, Double> keywordMap = new HashMap<String, Double>();
        for (String keyword : keywords) {
            keywordMap.put(keyword, 1.0);
        }

        userInfo.keywordWeight = keywordMap;

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
