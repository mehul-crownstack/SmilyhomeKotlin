package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class CommonRequest {

    @SerializedName("userid")
    private String mUserId;

    public CommonRequest(String userId) {
        mUserId = userId;
    }
}
