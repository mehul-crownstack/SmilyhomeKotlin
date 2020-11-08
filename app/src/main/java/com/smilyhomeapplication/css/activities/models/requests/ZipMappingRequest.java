package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class ZipMappingRequest {

    @SerializedName("zipCode")
    private String mZipCode;

    public ZipMappingRequest(String zipcode) {
        mZipCode = zipcode;
    }
}
