package com.sholay.app.datamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mohan.gupta on 22/07/16.
 * All the activities known to us.
 */
public class ActivityStore {
    private static ActivityStore instance = new ActivityStore();

    private Set<Activity> allActivities;
    private Map<String, Set<Activity>> tagToActivityMap;

    private ActivityStore() {
        allActivities = new HashSet<Activity>();
        tagToActivityMap = new HashMap<String, Set<Activity>>();
    }

    public static ActivityStore getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        this.allActivities.add(activity);
    }

    //create the reverse loopup index
    public void buildIndex() {
        for (Activity activity : allActivities) {
            Set<String> tags = activity.getTags().keySet();
            for (String tag : tags) {
                addActivityToTag(tag, activity);
            }
        }

    }

    private void addActivityToTag(String tag, Activity activity) {
        if (!tagToActivityMap.containsKey(tag)) {
            tagToActivityMap.put(tag, new HashSet<Activity>());
        }

        tagToActivityMap.get(tag).add(activity);
    }
}
