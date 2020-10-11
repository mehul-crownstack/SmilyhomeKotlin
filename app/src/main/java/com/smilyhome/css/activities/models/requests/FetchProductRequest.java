package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class FetchProductRequest {

    @SerializedName("SectionID")
    private String mProductMode = "";

    public FetchProductRequest(String productMode) {
        mProductMode = productMode;
    }
}
