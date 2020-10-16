package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailResponse extends CommonResponse {

    @SerializedName("id")
    private String mProductId = "";
    @SerializedName("brandName")
    private String mBrandName = "";
    @SerializedName("productCode")
    private String mProductCode = "";
    @SerializedName("productName")
    private String mProductName = "";
    @SerializedName("expressDelivery")
    private String mExpressDelivery = "";
    @SerializedName("standardDelivery")
    private String mStandardDelivery = "";
    @SerializedName("image")
    private String mProductImage = "";
    @SerializedName("productPrice")
    private String mProductPrice = "";
    @SerializedName("productSalePrice")
    private String mProductSalePrice = "";
    @SerializedName("productDisclaimer")
    private String mProductDisclaimer = "";
    @SerializedName("productDiscount")
    private String mProductDiscount = "";
    @SerializedName("productDescription")
    private String mProductDescription = "";
    @SerializedName("dataImages")
    List<ProductImageItem> mProductImagesList = new ArrayList<>();

    public String getProductDisclaimer() {
        return mProductDisclaimer;
    }

    public List<ProductImageItem> getProductImagesList() {
        return mProductImagesList;
    }

    public String getProductId() {
        return mProductId;
    }

    public String getBrandName() {
        return mBrandName;
    }

    public String getProductCode() {
        return mProductCode;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getExpressDelivery() {
        return mExpressDelivery;
    }

    public String getStandardDelivery() {
        return mStandardDelivery;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public String getProductPrice() {
        return mProductPrice;
    }

    public String getProductSalePrice() {
        return mProductSalePrice;
    }

    public String getProductDiscount() {
        return mProductDiscount;
    }

    public String getProductDescription() {
        return mProductDescription;
    }
}
