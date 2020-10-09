package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class InitiateOtpRequest {

    @SerializedName("phone")
    private String mMobileNumber = "";
    @SerializedName("name")
    private String mUserName = "";

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
