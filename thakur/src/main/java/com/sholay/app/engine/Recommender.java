package com.sholay.app.engine;

import com.sholay.app.datamodel.Activity;
import com.sholay.app.datamodel.OutputActivity;
import com.sholay.app.datamodel.UserInfo;
import com.sun.tools.javac.util.List;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Recommender {

    private UserInfo userInfo;
    public Recommender(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public List<OutputActivity> recommend() {
        return null;
    }
}
