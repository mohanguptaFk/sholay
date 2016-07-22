package com.sholay.app.datamodel;

import com.sholay.app.engine.Controller;

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

    public void addActivity(Activity activity) throws ParseException {
        if (activity.equals(ActivityTypes.EATING)) {
            try {
                Activity lunchAc = activity.setTimes(dateInMilis(Controller.today + " 11:30:00"),dateInMilis(Controller.today + " 03:30:00"), Controller.twoHour);
                Activity dinnerAc = activity.setTimes(dateInMilis(Controller.today + " 19:30:00"),dateInMilis(Controller.today + " 03:30:00"), Controller.twoHour);
                this.allActivities.add(lunchAc);
                this.allActivities.add(dinnerAc);
                 lunchAc = activity.setTimes(dateInMilis(Controller.tomorow + " 11:30:00"),dateInMilis(Controller.tomorow + " 03:30:00"), Controller.twoHour);
                 dinnerAc = activity.setTimes(dateInMilis(Controller.tomorow + " 19:30:00"), dateInMilis(Controller.tomorow + " 03:30:00"),Controller.twoHour);
                this.allActivities.add(lunchAc);
                this.allActivities.add(dinnerAc);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (activity.equals(ActivityTypes.PLACE)) {
            Activity lunchAc = activity.setTimes(dateInMilis(Controller.today + " 09:30:00"),dateInMilis(Controller.today + " 05:30:00"), Controller.twoHour);
            this.allActivities.add(lunchAc);
            lunchAc = activity.setTimes(dateInMilis(Controller.tomorow + " 09:30:00"), dateInMilis(Controller.tomorow + " 05:30:00"), Controller.twoHour);
            this.allActivities.add(lunchAc);

        } else {
            this.allActivities.add(activity);
        }
    }


    public static long dateInMilis(String day) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date date = sdf.parse(day);
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

    public Set<Activity> getAllActivities() {
        return allActivities;
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
