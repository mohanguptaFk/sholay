package com.sholay.app.datamodel;

import java.util.Map;

/**
 * Created by mohan.gupta on 22/07/16.
 * Like a event or a user info, on which we can do key word analysis and add tags.
 */
public interface ITaggable {
    Map<String, Double> getKeywordWeight();
    void addTag(String tag, Double conf);
}
