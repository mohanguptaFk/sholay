package com.sholay.app.datamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mohan.gupta on 22/07/16.
 * Describes the important info reg an activity.
 */
public class Activity implements ITaggable {
    public String id; //unique id for the activity.
    public ActivityTypes type;

    public String title; //user visible
    public long startTime;
    public long endTime;
    public long duration; //duration 0 means its kinda a place which is open between start and end time

    public String lat;
    public String lng;

    public String imagePath; //local disk path
    public String imageUrl; //actual web url.
    public String eventLink; //external link.

    //price range.
    public long budgetMin;
    public long budgetMax;


    private Map<String, Double> keywords;

    public Map<String, Double> tags; //with confidence

    public Activity() {
        tags = new HashMap<String, Double>();
        keywords = new HashMap<String, Double>();
    }

    public Map<String, Double> getKeywordWeight() {
        return keywords;
    }

    public void addTag(String tag, Double conf) {
        tags.put(tag, conf);
    }

    public Map<String, Double> getTags() {
        return tags;
    }
}
