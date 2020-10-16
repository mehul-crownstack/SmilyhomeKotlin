package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class ProductImageItem {

    @SerializedName("productImages")
    private String mProductImage = "";

    public String getProductImage() {
        return mProductImage;
    }
}
