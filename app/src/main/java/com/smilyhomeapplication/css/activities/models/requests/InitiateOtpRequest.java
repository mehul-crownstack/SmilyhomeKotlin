package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class InitiateOtpRequest {

    @SerializedName("phone")
    private String mMobileNumber = "";
    @SerializedName("name")
    private String mUserName = "";
    @SerializedName("smsHashKey")
    private String mSmsHashKey = "";

    public void setSmsHashKey(String smsHashKey) {
        mSmsHashKey = smsHashKey;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
