package com.sholay.app.datamodel;

import com.sholay.app.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class UserInfo implements ITaggable{
    public Set<SuperTags.SuperTagsTypes> superTags;
    public Map<String, Double> tagsConf;
    public long startTime;
    public long endTime;

    public String lat;
    public String lng;

    // all the keywords sentences in the user profile with their weights.
    public Map<String, Double> keywordWeight;

    public UserInfo() {
        this.keywordWeight = new HashMap<String, Double>();
        this.superTags = new HashSet<SuperTags.SuperTagsTypes>();
        this.tagsConf = new HashMap<String, Double>();
    }

    public void addKeyword(String keyword, double weight) {
        keyword = Utils.cleanKeyword(keyword);

        if (!keywordWeight.containsKey(keyword)) {
            keywordWeight.put(keyword, 0.0);
        }

        keywordWeight.put(keyword, keywordWeight.get(keyword) + weight);
    }

    public Map<String, Double> getKeywordWeight() {
        return keywordWeight;
    }

    public void addTag(String tag, Double conf) {
        // some of the tags can be prefilled via the super tags, do not overwrite their weight.
        if (!tagsConf.containsKey(tag)) {
            this.tagsConf.put(tag, conf);
        }
    }

    public void addSuperTag(SuperTags.SuperTagsTypes type) {
        superTags.add(type);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
