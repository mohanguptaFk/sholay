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
    private ActivityTypes ac;
    public String id; //unique id for the activity.
    public ActivityTypes type;

    public String title; //user visible
    private  String starttime;
    private String endtime;
    public long startTime;
    public long endTime;
    public long duration; //duration 0 means its kinda a place which is open between start and end time

    public String lat;
    private String lang;

    @Override
    public String toString() {
        return "Activity{" +
                "count=" + count +
                ", ac=" + ac +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
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

    public String lng;

    public String address;
    private String imageP;
    private String image;
    private String eventlink;

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

    public Activity(int count, ActivityTypes ac, String title, String starttime, String endtime, long duration, String lat, String lang, String address, String imageP, String image, String eventlink, List<String> tags) {


        this.count = count;
        this.ac = ac;
        this.title = title;
        this.starttime = starttime;
        this.endtime = endtime;
        this.duration = duration;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.imageP = imageP;
        this.image = image;
        this.eventlink = eventlink;
        this.tags = new HashMap<String, Double>();
        for (String tag : tags) {
            this.tags.put(tag, 100.0d);
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
}
