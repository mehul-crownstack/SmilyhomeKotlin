package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class MyOrderCartItem {

    @SerializedName("itemImage")
    private String itemImage = "";
    @SerializedName("itemName")
    private String itemName = "";
    @SerializedName("itemQuantity")
    private String itemQuantity = "";
    @SerializedName("itemOrderId")
    private String itemOrderId = "";
    @SerializedName("ItemCartId")
    private String itemCartId = "";
    @SerializedName("itemSalePrice")
    private String itemSalePrice = "";
    @SerializedName("itemStatus")
    private String itemStatus = "";

    public String getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemOrderId() {
        return itemOrderId;
    }

    public String getItemCartId() {
        return itemCartId;
    }

    public String getItemSalePrice() {
        return itemSalePrice;
    }

    public String getItemStatus() {
        return itemStatus;
    }
}
