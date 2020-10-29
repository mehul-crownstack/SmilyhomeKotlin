package com.smilyhomeapp.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class CartItem {

    @SerializedName("productId")
    private String productId = "";
    @SerializedName("cartId")
    private String cartId = "";
    @SerializedName("productName")
    private String productName = "";
    @SerializedName("productQuantity")
    private String productQuantity = "";
    @SerializedName("productImage")
    private String image = "";
    @SerializedName("productPrice")
    private String productPrice = "";
    @SerializedName("productSalePrice")
    private String productSalePrice = "";
    @SerializedName("productDiscount")
    private String productDiscount = "";

    public String getProductId() {
        return productId;
    }

    public String getCartId() {
        return cartId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getImage() {
        return image;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductSalePrice() {
        return productSalePrice;
    }

    public String getProductDiscount() {
        return productDiscount;
    }
}
