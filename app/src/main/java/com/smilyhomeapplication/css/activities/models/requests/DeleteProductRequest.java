package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class DeleteProductRequest {

    @SerializedName("cartId")
    private String cartId;
    @SerializedName("userId")
    private String userId;

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
