package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class ZipCodeResponse extends CommonResponse {

    @SerializedName("city")
    private String mCity;

    public String getCity() {
        return mCity;
    }
}
