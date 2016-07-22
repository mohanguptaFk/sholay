package com.sholay.app.engine;

import com.sholay.app.Utils;
import com.sholay.app.datamodel.ITaggable;
import com.sholay.app.datamodel.TagsKeywords;

import java.util.Set;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Tagger {
    private static Tagger instance = new Tagger();

    private Tagger() {

    }

    public static Tagger getInstance() {
        return instance;
    }

    //all the logic which tags the object based on the keywords it has.
    public void tag(ITaggable taggable) {
        if (taggable != null) {
            Set<String> keywords = taggable.getKeywordWeight().keySet();
            for (String keyword : keywords) {
                keyword = Utils.cleanKeyword(keyword);
                Set<String> tags = TagsKeywords.getInstance().getTagsForKeyword(keyword);
                for (String tag: tags) {
                    taggable.addTag(tag, 1.0);
                }
            }
        }
    }
}
