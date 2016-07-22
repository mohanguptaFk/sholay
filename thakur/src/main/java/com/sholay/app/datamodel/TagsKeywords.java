package com.sholay.app.datamodel;

import com.sholay.app.Constants;
import com.sholay.app.Utils;

import java.util.*;

/**
 * Created by mohan.gupta on 22/07/16.
 * A mapping between all the tags we have and the keywords which lead to them.
 */
public class TagsKeywords {

    public static TagsKeywords instance = new TagsKeywords();

    private Map<String, Set<String>> mapping;

    // keeps the types of classifications of each tag.
    private Map<String, Set<SuperTags.SuperTagsTypes>> classification;



    private TagsKeywords() {

        this.mapping = new HashMap<String, Set<String>>();
        this.classification = new HashMap<String, Set<SuperTags.SuperTagsTypes>>();
    }

    public static TagsKeywords getInstance() {
        return instance;
    }


    public void addKeyword(String tag, String[] keywords) {
        Set<String> set = getKeywords(tag);
        set.addAll(Arrays.asList(keywords));
    }

    public void addclassification(String tag, SuperTags.SuperTagsTypes klass) {
        Set<SuperTags.SuperTagsTypes> set = classification.get(tag);
        set.add(klass);
    }

    public Set<String> getKeywords(String tag) {
        if (!mapping.containsKey(tag)) {
            mapping.put(tag, new HashSet<String>());
            classification.put(tag, new HashSet<SuperTags.SuperTagsTypes>());
        }
        return mapping.get(tag);
    }

    public Set<String> getTagsForClass(SuperTags.SuperTagsTypes type) {
        if (classification != null) {
            Set<String> set = new HashSet<String>();
            for (String tag : classification.keySet()) {
                if (classification.get(tag).contains(type)) {
                    set.add(tag);
                }
            }
            return set;
        }
        return new HashSet<String>();
    }

    public boolean validTag(String tag) {
        return mapping.containsKey(tag);
    }

    public Set<String> getTagsForKeyword(String keyword) {
        Set<String> set = new HashSet<String>();
        for (String tag: mapping.keySet()) {
            if (mapping.get(tag).contains(keyword)) {
                set.add(tag);
            }
        }
        return set;
    }

    public Map<String, Set<String>> getMapping() {
        return mapping;
    }

    public  void init() {
        String filePath= "tagsProcessed.txt";
        String SEPERATOR =":::";
        List<String> lines = Utils.readAllLines(filePath);

        TagsKeywords tags = TagsKeywords.getInstance();
        for (String line : lines) {
            String[] splits = line.split(SEPERATOR);

            String tag = splits[0].toLowerCase().trim();
            String keywords= splits[1].toLowerCase().trim();
            String primary = splits[2].toLowerCase().trim();
            String inOut = splits[3].toLowerCase().trim();
            String passive = splits[4].toLowerCase().trim();
            String familyType = splits[5].toLowerCase().trim();

            tags.addKeyword(tag, keywords.split(","));

            for (String s : inOut.split(",")) {
                if (s.equalsIgnoreCase(Constants.ACTIVITY_INDOOR)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.INDOOR);
                }
                if (s.equalsIgnoreCase(Constants.ACTIVITY_OUTDOOR)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.OUTDOOR);
                }
            }

            for (String s : passive.split(",")) {
                if (s.equalsIgnoreCase(Constants.ACTIVITY_CHILL)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.CHILL);
                }
                if (s.equalsIgnoreCase(Constants.ACTIVITY_ADVENTURE)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.ADVENTURE);
                }
            }


            for (String s : primary.split(",")) {
                if (s.equalsIgnoreCase(Constants.ACTIVITY_TYPE_PRIMARY)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.PRIMARY);
                }
                if (s.equalsIgnoreCase(Constants.ACTIVITY_TYPE_SECONDARY)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.SECONDARY);
                }
                if (s.equalsIgnoreCase(Constants.ACTIVITY_TYPE_TERTIARY)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.TERTIARY);
                }
            }

            for (String s : familyType.split(",")) {
                if (s.equalsIgnoreCase(Constants.FAMILY_TYPE_FAMILY)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.FAMILY);
                }
                if (s.equalsIgnoreCase(Constants.FAMILY_TYPE_COUPLES)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.COUPLES);
                }
                if (s.equalsIgnoreCase(Constants.FAMILY_TYPE_FRIENDS)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.FRIENDS);
                }
                if (s.equalsIgnoreCase(Constants.FAMILY_TYPE_SINGLE)) {
                    tags.addclassification(tag, SuperTags.SuperTagsTypes.SINGLE);
                }
            }

        }

    }


}
