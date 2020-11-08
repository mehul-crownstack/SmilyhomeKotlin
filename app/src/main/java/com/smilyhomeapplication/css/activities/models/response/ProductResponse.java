package com.smilyhomeapplication.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductResponse extends CommonResponse {

    @SerializedName("data")
    private List<ProductItem> mProductList = new ArrayList<>();

    @SerializedName("imageBaseUrl")
    private String mImageBaseUrl = "";

    public String getImageBaseUrl() {
        return mImageBaseUrl;
    }

    public List<ProductItem> getProductList() {
        return mProductList;
    }
}
