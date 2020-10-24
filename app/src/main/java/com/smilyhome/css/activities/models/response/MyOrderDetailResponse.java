package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOrderDetailResponse extends CommonResponse {

    @SerializedName("orderPlaced")
    private String orderPlaced;
    @SerializedName("orderBagId")
    private String orderBagId;
    @SerializedName("orderStatus")
    private String orderStatus;
    @SerializedName("paymentMode")
    private String paymentMode;
    @SerializedName("totalAmount")
    private String totalAmount;
    @SerializedName("orderValue")
    private String orderValue;
    @SerializedName("orderDiscount")
    private String orderDiscount;
    @SerializedName("orderDeliveryFee")
    private String orderDeliveryFee;
    @SerializedName("orderTotalAmount")
    private String orderTotalAmount;
    @SerializedName("address")
    private UserAddressResponse address;
    @SerializedName("orderItems")
    private List<MyOrderCartItem> orderItemsList = new ArrayList<>();

    public UserAddressResponse getAddress() {
        return address;
    }

    public List<MyOrderCartItem> getOrderItemsList() {
        return orderItemsList;
    }

    public String getOrderPlaced() {
        return orderPlaced;
    }

    public String getOrderBagId() {
        return orderBagId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public String getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }
}
