package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOrderResponse extends CommonResponse {

    @SerializedName("pendingOrders")
    private List<MyOrderItem> mPendingOrdersList = new ArrayList<>();

    @SerializedName("deliveredOrders")
    private List<MyOrderItem> mDeliveredOrdersList = new ArrayList<>();

    @SerializedName("cancelledOrders")
    private List<MyOrderItem> mCancelledOrdersList = new ArrayList<>();

    public List<MyOrderItem> getDeliveredOrdersList() {
        return mDeliveredOrdersList;
    }

    public List<MyOrderItem> getCancelledOrdersList() {
        return mCancelledOrdersList;
    }

    public List<MyOrderItem> getPendingOrdersList() {
        return mPendingOrdersList;
    }
}
