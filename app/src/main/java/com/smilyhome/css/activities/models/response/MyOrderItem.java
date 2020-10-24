package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class MyOrderItem {

    @SerializedName("orderPlacedOn")
    private String orderPlacedOn = "";
    @SerializedName("totalItems")
    private String totalItems = "";
    @SerializedName("deliveredItems")
    private String deliveredItems = "";
    @SerializedName("orderId")
    private String orderId = "";
    @SerializedName("bagId")
    private String bagId = "";
    @SerializedName("orderStatus")
    private String orderStatus = "";
    @SerializedName("totalAmount")
    private String totalAmount = "";

    public String getBagId() {
        return bagId;
    }

    public String getOrderPlacedOn() {
        return orderPlacedOn;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public String getDeliveredItems() {
        return deliveredItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}
