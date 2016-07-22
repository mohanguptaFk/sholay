package com.sholay.app.datamodel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
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
//        if (activity.equals(ActivityTypes.EATING)) {
////            newAc = activity.setStartTime();
//            tagToActivityMap.put("lunch", )

//        }
    { this.allActivities.add(activity);}
    }


    private long dateInMilis() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateInString = "22-01-2015 10:20:56";
        Date date = sdf.parse(dateInString);

        System.out.println(dateInString);
        System.out.println("Date - Time in milliseconds : " + date.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
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
            tagToActivityMap.put(tag.toLowerCase(), new HashSet<Activity>());
        }

        tagToActivityMap.get(tag.toLowerCase()).add(activity);
    }

    public Set<Activity> getActivity(String tag) {
        return tagToActivityMap.get(tag.toLowerCase());
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
            if (tagToActivityMap.containsKey(tag)) {
                finalOutput.addAll(tagToActivityMap.get(tag));
            }
        }

        return finalOutput;
    }
}
