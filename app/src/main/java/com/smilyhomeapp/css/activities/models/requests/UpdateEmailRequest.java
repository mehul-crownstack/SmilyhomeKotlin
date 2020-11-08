package com.smilyhomeapp.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class UpdateEmailRequest {

    @SerializedName("userMail")
    private String userMail;
    @SerializedName("userId")
    private String userId;

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
