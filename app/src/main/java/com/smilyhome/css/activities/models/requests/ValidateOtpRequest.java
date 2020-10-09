package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class ValidateOtpRequest {

    @SerializedName("otpreq")
    private String mEnteredOtp = "";
    @SerializedName("userid")
    private String mUserId = "";

    public ValidateOtpRequest(String enteredOtp, String userId) {
        mEnteredOtp = enteredOtp;
        mUserId = userId;
    }
}
