package com.smilyhomeapp.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class ProgramAndFeatureItem extends CommonResponse {

    @SerializedName("label")
    private String label;

    @SerializedName("link")
    private String link;

    public String getLabel() {
        return label;
    }

    public String getLink() {
        return link;
    }
}
