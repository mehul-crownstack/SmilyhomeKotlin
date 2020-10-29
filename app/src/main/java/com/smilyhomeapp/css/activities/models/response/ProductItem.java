package com.smilyhomeapp.css.activities.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("ProductCode")
    @Expose
    private String productCode = "";
    @SerializedName("ProductName")
    @Expose
    private String productName = "";
    @SerializedName("ProductShortDesc")
    @Expose
    private String productShortDesc = "";
    @SerializedName("Image")
    @Expose
    private String image = "";
    @SerializedName("ProductPrice")
    @Expose
    private String productPrice = "";
    @SerializedName("ProductSalePrice")
    @Expose
    private String productSalePrice = "";
    @SerializedName("ProductDiscount")
    @Expose
    private String productDiscount = "";

    public String getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductShortDesc() {
        return productShortDesc;
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
