package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class CancelProductRequest {

    @SerializedName("orderId")
    private String mOrderId;
    @SerializedName("ItemCartId")
    private String mItemCartId;

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public void setItemCartId(String itemCartId) {
        mItemCartId = itemCartId;
    }
}
