package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class FetchProductRequest {

    @SerializedName("SectionID")
    private int mProductMode;

    public FetchProductRequest(int productMode) {
        mProductMode = productMode;
    }
}
