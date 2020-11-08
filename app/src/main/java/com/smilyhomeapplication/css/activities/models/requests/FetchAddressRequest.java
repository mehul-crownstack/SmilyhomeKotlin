package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class FetchAddressRequest {

    @SerializedName("userId")
    private String mUserId;

    public FetchAddressRequest(String userId) {
        mUserId = userId;
    }
}
