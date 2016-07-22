package com.sholay.app.engine;

import com.sholay.app.datamodel.*;

import java.util.ArrayList;
import java.util.*;

/**
 * Created by mohan.gupta on 22/07/16.
 */
public class Recommender {

    private UserInfo userInfo;
    private Set<Activity> macthingSet; //null matching set means matches everything.

    private Activity bestPrimaryActivity;
    private Activity bestBefore;
    private Activity bestAfter;

    public Recommender(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.userInfo.startTime = userInfo.startTime + (long)1.5*60*60*1000; //traffic
    }
    public List<OutputActivity> recommend() {

        // All the user specified criteria + primary type event.
        Set<SuperTags.SuperTagsTypes> filterSuperTags = userInfo.superTags;
        if (filterSuperTags == null) filterSuperTags = new HashSet<SuperTags.SuperTagsTypes>();
        filterSuperTags.add(SuperTags.SuperTagsTypes.PRIMARY);

//        bestPrimaryActivity = ActivityStore.getInstance().getAllActivities().iterator().next();
//        List<OutputActivity> list = new ArrayList<OutputActivity>();
//        list.add(new OutputActivity(bestPrimaryActivity));

        // create the matching set.
        getMatchingSet(filterSuperTags);

        // get the best ranking event based on user tags.
        getBestPrimaryEvent();



        // stick non-primary events.
        getNonPrimaryEvents();

        List<OutputActivity> list = new ArrayList<OutputActivity>();
        list.add(new OutputActivity(bestBefore));
        list.add(new OutputActivity(bestPrimaryActivity));
        list.add(new OutputActivity(bestAfter));

        return list;
    }

    public void getMatchingSet(Set<SuperTags.SuperTagsTypes> filterSuperTags) {
        //apply filtering.
        if (filterSuperTags != null && !filterSuperTags.isEmpty()) {
            List<Set<String>> clauses = new ArrayList<Set<String>>();
            for (SuperTags.SuperTagsTypes type : filterSuperTags) {
                Set<String> set = TagsKeywords.getInstance().getTagsForClass(type);
                if (set != null && !set.isEmpty()) {
                    clauses.add(set);
                }
            }

            Set<Activity> set = null;
            //from the set of clauses get the common ones, kinda a AND of these caluses.
            if (clauses.size() > 0) {
                Set<String> ors = new HashSet<String>(clauses.get(0));
                for (int i =1; i < clauses.size(); i++) {
                    ors.retainAll(clauses.get(i));
                }
                set = ActivityStore.getInstance().getORs(ors);
            }

            //remove the things out of the time specified.
            Set<Activity> timeFiltered = null;
            if (set != null) {
                timeFiltered = new HashSet<Activity>();
                for (Activity activity : set) {
                    if (doesTimeOverlap(activity)) {
                        timeFiltered.add(activity);
                    }
                }
            }

             macthingSet = timeFiltered;

            //macthingSet = ActivityStore.getInstance().getANDsOfORs(clauses);
        }
    }

    private boolean doesTimeOverlap(Activity activity) {
        // an activity can be an open activity like park.
        if (activity.type == ActivityTypes.PLACE) {
            if (userInfo.startTime <= (activity.endTime - activity.duration)) {
                return true;
            }
        }
        //for movie you should be able to reach on time.
        if (activity.type == ActivityTypes.MOVIE) {
            if (userInfo.startTime  <= activity.startTime) {
                return true;
            }
        }
        else {
            long midTime = (userInfo.getStartTime() + userInfo.getEndTime()) / 2;
            if (userInfo.startTime  < midTime) {
                return true;
            }
        }

        return false;
    }
    private void getBestPrimaryEvent() {
        Set<Activity> set = macthingSet;

        if (set == null) {
            set = ActivityStore.getInstance().getAllActivities();
        }

        Set<String> userTags = userInfo.getTagsConf().keySet();

        //find the activity which matches most of these tags.
        int bestMacthCount = 0;
        Activity bestActivity = null;
        for (Activity activity : set) {
            Set<String> intersection = new HashSet<String>(activity.getTags().keySet()); // use the copy constructor
            intersection.retainAll(userTags);
            if (intersection.size() > bestMacthCount) {
                bestMacthCount = intersection.size();
                bestActivity = activity;
            }
        }

        if (bestActivity == null) {
            bestActivity = set.iterator().hasNext()? set.iterator().next() : null;
        }

        bestPrimaryActivity = bestActivity;
    }

    public void getNonPrimaryEvents() {
        long trafficDelta = 30*60*1000; //sec
        if (bestPrimaryActivity != null) {
            long beforeActivityStart = userInfo.startTime -trafficDelta;
            long beforeActivityEnd = bestPrimaryActivity.startTime - trafficDelta;

            long afterActivityStart = bestPrimaryActivity.endTime +trafficDelta;
            long afterActivityEnd = userInfo.endTime +trafficDelta;

            //get all non-primary activities sorted by user affinity.
            Set<String> tags = new HashSet<String>();
            tags.addAll(TagsKeywords.getInstance().getTagsForClass(SuperTags.SuperTagsTypes.SECONDARY));
            tags.addAll(TagsKeywords.getInstance().getTagsForClass(SuperTags.SuperTagsTypes.TERTIARY));

            Set<Activity> set = ActivityStore.getInstance().getORs(tags);

            //sort these via user affinity.
            Set<String> userTags = userInfo.getTagsConf().keySet();

            List<ScoredActivity> sorted = new ArrayList<ScoredActivity>();
            for (Activity activity : set) {
                Set<String> tempSet = new HashSet<String>(userTags);
                tempSet.retainAll(activity.getTags().keySet());
                sorted.add(new ScoredActivity(activity, tempSet.size()));
            }
            Collections.sort(sorted, new ActivityCompare());

            //now pick the first which meets the time constrains.
            Activity beforePrimary = null;
            Activity afterPrimary = null;
            for (ScoredActivity sa: sorted) {
                Activity a = sa.activity;
                // we do not already have an event and this event starts after our time and ends before our window.
                if (a.type != ActivityTypes.MOVIE) {
                    long duration = a.endTime - beforeActivityStart;
                    if (beforePrimary == null && (
                            duration >= 30*60*1000)) {
                        beforePrimary = a;
                    }

                    duration = a.endTime - afterActivityStart;
                    if (afterPrimary == null && (
                            duration >= 30*60*1000)) {
                        afterPrimary = a;
                    }

                }
                else {
                    if (beforePrimary == null && (
                            beforeActivityStart <= a.startTime && a.endTime <= beforeActivityEnd)) {
                        beforePrimary = a;
                    }

                    if (afterPrimary == null && (
                            afterActivityStart <= a.startTime && a.endTime  <= afterActivityEnd)) {
                        afterPrimary = a;
                    }

                }


                if (beforePrimary != null && afterPrimary != null) {
                    break;
                }
            }

            this.bestBefore = beforePrimary;
            this.bestAfter = afterPrimary;
        }

    }

    public static class ScoredActivity {
        public  Activity activity;
        public long score;

        public ScoredActivity(Activity activity, long score) {
            this.activity =activity;
            this.score = score;
        }
    }

    public static class ActivityCompare implements Comparator<ScoredActivity> {


        public int compare(ScoredActivity o1, ScoredActivity o2) {
            // write comparison logic here like below , it's just a sample
            return (int) (o2.score - o1.score); //reverse sorted.
        }
    }


}
