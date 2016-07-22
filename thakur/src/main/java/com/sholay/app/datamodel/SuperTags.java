package com.sholay.app.datamodel;

import java.util.*;

/**
 * Created by mohan.gupta on 22/07/16.
 * mapping example. Entertainment-->Movies, plays etc.
 */
public class SuperTags {
    public  enum SuperTagsTypes {CHILL, ADVENTURE, FAMILY, COUPLES, SINGLE, FRIENDS, OUTDOOR, INDOOR, PRIMARY, SECONDARY, TERTIARY};

    private static SuperTags instance = new SuperTags();

    private HashMap<SuperTagsTypes, Set<String>> superTagsMapping; //supertag to tag.

    private SuperTags() {
        this.superTagsMapping = new HashMap<SuperTagsTypes, Set<String>>();
    }

    public static SuperTags getInstance() {
        return instance;
    }

    public void addMapping(SuperTagsTypes type, String tag) {
        Set<String> set = getTagsForType(type);
        set.add(tag);
    }

    public void addMapping(SuperTagsTypes type, Collection<String> tags) {
        Set<String> set = getTagsForType(type);
        set.addAll(tags);
    }

    public Set<String> getTagsForType(SuperTagsTypes type) {
        if (!superTagsMapping.containsKey(type)) {
            superTagsMapping.put(type, new HashSet<String>());
        }
        return superTagsMapping.get(type);
    }
}
