package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TransactionResponse extends CommonResponse {

    @SerializedName("modeOfPayment")
    private String modeOfPayment;
    @SerializedName("displayMessage")
    private String displayMessage;
    @SerializedName("orderNumber")
    private String orderNumber;
    @SerializedName("totalPrice")
    private String totalPrice;
    @SerializedName("totalPaidPrice")
    private String totalPaidPrice;
    @SerializedName("status")
    private String status;
    @SerializedName("address")
    private UserAddressResponse mUserAddress = new UserAddressResponse();
    @SerializedName("cart")
    private List<CartItem> mCartItemList = new ArrayList<>();

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getTotalPaidPrice() {
        return totalPaidPrice;
    }

    public String getStatus() {
        return status;
    }

    public UserAddressResponse getUserAddress() {
        return mUserAddress;
    }

    public List<CartItem> getCartItemList() {
        return mCartItemList;
    }
}
