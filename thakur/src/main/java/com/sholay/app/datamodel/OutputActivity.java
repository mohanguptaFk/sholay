package com.sholay.app.datamodel;

import com.sholay.app.Utils;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by mohan.gupta on 22/07/16.
 * Activity object that is send as response. A subset of fields.
 */
public class OutputActivity {

    @JsonProperty
    public String id; //unique id for the activity.
    @JsonProperty
    public ActivityTypes type;

    @JsonProperty
    public String title; //user visible
    @JsonProperty
    public long startTime;
    @JsonProperty
    public long endTime;

    @JsonProperty
    public String imageUrl; //actual web url.

    @JsonProperty
    public double distanceInMeters; //first activity has distance from source, others distance from previous.

    @JsonProperty
    public String description;

    public OutputActivity(){}

    public OutputActivity(Activity activity, UserInfo userInfo) {
        if (activity != null) {
            id = activity.id;
            title = activity.title;
            imageUrl = activity.image;
            description = activity.getDescription();
            type = activity.ac;
            //distanceInMeters = Utils.distance(Utils.tryParseDouble(userInfo.lat), Utils.tryParseDouble(userInfo.lng), Utils.tryParseDouble(activity.lat), Utils.tryParseDouble(activity.lng),0,0);
        }
    }
}
