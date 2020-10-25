package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class InfoRequest {

    @SerializedName("mode")
    private String mode = "";

    public InfoRequest(String mode) {
        this.mode = mode;
    }

}
