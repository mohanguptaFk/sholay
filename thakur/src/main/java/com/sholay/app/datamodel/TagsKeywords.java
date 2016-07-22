package com.sholay.app.datamodel;

import java.util.*;

/**
 * Created by mohan.gupta on 22/07/16.
 * A mapping between all the tags we have and the keywords which lead to them.
 */
public class TagsKeywords {

    public static TagsKeywords instance = new TagsKeywords();

    private Map<String, Set<String>> mapping;

    private TagsKeywords() {
        this.mapping = new HashMap<String, Set<String>>();
    }

    public static TagsKeywords getInstance() {
        return instance;
    }

    public void addKeyword(String tag, String keyword) {
        Set<String> set = getKeywords(tag);
        set.add(keyword);
    }

    public void addKeyword(String tag, Collection<String> keywords) {
        Set<String> set = getKeywords(tag);
        set.addAll(keywords);
    }

    public Set<String> getKeywords(String tag) {
        if (!mapping.containsKey(tag)) {
            mapping.put(tag, new HashSet<String>());
        }
        return mapping.get(tag);
    }

    public boolean validTag(String tag) {
        return mapping.containsKey(tag);
    }

    public Map<String, Set<String>> getMapping() {
        return mapping;
    }


}
