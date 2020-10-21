package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class ProductFeatureItem {

    @SerializedName("label")
    private String mFeatureLabel = "";
    @SerializedName("value")
    private String mFeatureValue = "";

    public String getFeatureLabel() {
        return mFeatureLabel;
    }

    public String getFeatureValue() {
        return mFeatureValue;
    }
}
