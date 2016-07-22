package com.sholay.app.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mohan.gupta on 22/07/16.
 * Describes the important info reg an activity.
 */
public class Activity implements ITaggable {
    private int count;
    public ActivityTypes ac;
    public String id; //unique id for the activity.
    public ActivityTypes type;

    public String title; //user visible
    public long startTime;
    public long endTime;
    public long duration; //duration 0 means its kinda a place which is open between start and end time

    public String lat;
    private String lang;

    public String lng;

    public String address;
    private String imageP;
    public String image;
    private String eventlink;

    public String imagePath; //local disk path
    public String imageUrl; //actual web url.
    public String eventLink; //external link.

    //price range.
    public long budgetMin;
    public long budgetMax;

    private Map<String, Double> keywords = new HashMap<String, Double>();

    public Map<String, Double> tags = new HashMap<String, Double>(); //with confidence


    public Activity(Activity activity, long startTime, long duration) {
        Activity newActivity = new Activity(activity);
        newActivity.startTime = startTime;
        newActivity.duration = duration;
    }

    public Activity(Activity activity) {
        this.ac = activity.ac;
        this.id = activity.id;
        this.type = activity.type;
        this.title = activity.title;
        this.startTime = activity.startTime;
        this.endTime = activity.endTime;
        this.duration = activity.duration;
        this.lang = activity.lang;
        this.lat = activity.lat;
        this.address = activity.address;
        this.image = activity.image;
        this.imageP = activity.imageP;
        this.imagePath = activity.imagePath;
        this.imageUrl = activity.imageUrl;


    }

    public Activity(Activity activity, long startTime, long endTime, long duration) {
        Activity newActivity = new Activity(activity);
        newActivity.startTime = startTime;
        newActivity.duration = duration;
        newActivity.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "count=" + count +
                ", ac=" + ac +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration='" + duration + '\'' +
                ", lat='" + lat + '\'' +
                ", lang='" + lang + '\'' +
                ", lng='" + lng + '\'' +
                ", address='" + address + '\'' +
                ", imageP='" + imageP + '\'' +
                ", image='" + image + '\'' +
                ", eventlink='" + eventlink + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", eventLink='" + eventLink + '\'' +
                ", budgetMin=" + budgetMin +
                ", budgetMax=" + budgetMax +
                ", keywords=" + keywords +
                ", tags=" + tags +
                '}';
    }



    public Activity() {
        tags = new HashMap<String, Double>();
        keywords = new HashMap<String, Double>();
    }

    public Activity(int count, ActivityTypes ac, String title, long starttime, long endtime, long duration, String lat, String lang, String address, String imageP, String image, String eventlink, List<String> keywords) {

        this.count = count;
        this.ac = ac;
        this.title = title;
        this.startTime = starttime;
        this.endTime = endtime;
        this.duration = duration;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.imageP = imageP;
        this.image = image;
        this.eventlink = eventlink;
        this.keywords = new HashMap<String, Double>();
        for (String keyword : keywords) {
            this.keywords.put(keyword, 100.0d);
        }
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

    public String getDescription() {
        return "";
    }
    public Activity setStartTime(long s, long duration) {
        return new Activity(this, s, duration);
    }

    public Activity setTimes(long startTime, long endTime, long duration) {
        return new Activity(this, startTime, endTime, duration);
    }
}
