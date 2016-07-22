package com.sholay.app.datamodel;

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
    public long distanceInMeters; //first activity has distance from source, others distance from previous.

    @JsonProperty
    public String description;

    public OutputActivity(){}

    public OutputActivity(Activity activity) {
        if (activity != null) {
            id = activity.id;
            title = activity.title;
            imageUrl = activity.image;
            description = activity.getDescription();
            type = activity.ac;
        }
    }
}
