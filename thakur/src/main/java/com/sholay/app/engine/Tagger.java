package com.sholay.app.engine;

import com.sholay.app.datamodel.ITaggable;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Tagger {
    private Tagger instance = new Tagger();

    private Tagger() {

    }

    public Tagger getInstance() {
        return instance;
    }

    //all the logic which tags the object based on the keywords it has.
    public void tag(ITaggable taggable) {

    }
}
