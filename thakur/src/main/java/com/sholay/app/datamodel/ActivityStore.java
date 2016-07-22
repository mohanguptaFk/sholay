package com.sholay.app.datamodel;



import java.util.*;

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

    public Set<Activity> getAllActivities() {
        return allActivities;
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

    //ex (play OR food) AND (outdoor)
    public Set<Activity> getANDsOfORs(List<Set<String>> andTagClauses) {
        List<Set<Activity>> sets = new ArrayList<Set<Activity>>();
        Map<Activity, Integer> activityCount = new HashMap<Activity, Integer>();
        Set<Activity> finalOutput = new HashSet<Activity>();

        for (Set<String> tags : andTagClauses) {
            Set<Activity> ors = getORs(tags);
            for (Activity activity : ors) {
                if (!activityCount.containsKey(activity)) {
                    activityCount.put(activity, 0);
                }
                activityCount.put(activity, activityCount.get(activity) + 1);
            }
        }


        for (Activity activity : activityCount.keySet()) {
            if (activityCount.get(activity) == andTagClauses.size()) {
                finalOutput.add(activity);
            }
        }

        return finalOutput;
    }

    public Set<Activity> getORs(Set<String> tags) {
        Set<Activity> finalOutput = new HashSet<Activity>();

        for (String tag : tags) {
            finalOutput.addAll(tagToActivityMap.get(tag));
        }

        return finalOutput;
    }
}
