package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {

    @SerializedName("productId")
    private String mProductId;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("productQuantity")
    private String mProductQuantity;
    @SerializedName("imageName")
    private String mProductImage;
    @SerializedName("productName")
    private String mProductName;

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public void setProductImage(String productImage) {
        mProductImage = productImage;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setProductQuantity(String productQuantity) {
        mProductQuantity = productQuantity;
    }
}
