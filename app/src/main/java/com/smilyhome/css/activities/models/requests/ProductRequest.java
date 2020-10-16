package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class ProductRequest {

    @SerializedName("productid")
    private String mProductId;

    public ProductRequest(String productId) {
        mProductId = productId;
    }
}
