package com.smilyhomeapplication.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class FetchOrderDetailRequest {

    @SerializedName("orderId")
    private String mOrderId;

    public FetchOrderDetailRequest(String orderId) {
        mOrderId = orderId;
    }
}
