package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyCartResponse extends CommonResponse {

    @SerializedName("totalMrp")
    private String totalMrp = "";
    @SerializedName("paybleAmount")
    private String paybleAmount = "";
    @SerializedName("productDiscount")
    private String productDiscount = "";
    @SerializedName("earningDiscount")
    private String earningDiscount = "";
    @SerializedName("deliveryCharges")
    private String deliveryCharges = "";
    @SerializedName("totalPaybleAmount")
    private String totalPaybleAmount = "";
    @SerializedName("data")
    private List<CartItem> mCartItemList = new ArrayList<>();

    public String getTotalMrp() {
        return totalMrp;
    }

    public String getPaybleAmount() {
        return paybleAmount;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public String getEarningDiscount() {
        return earningDiscount;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public String getTotalPaybleAmount() {
        return totalPaybleAmount;
    }

    public List<CartItem> getCartItemList() {
        return mCartItemList;
    }
}
