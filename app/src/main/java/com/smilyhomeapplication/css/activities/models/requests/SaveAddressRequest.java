package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class SaveAddressRequest {

    @SerializedName("userId")
    private String userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("fullAddress")
    private String fullAddress;
    @SerializedName("zipCode")
    private String zipCode;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
