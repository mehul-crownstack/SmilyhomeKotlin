package com.smilyhomeapplication.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryResponse extends CommonResponse {

    @SerializedName("data")
    private List<CategoryItem> mProductList = new ArrayList<>();

    @SerializedName("imageBaseUrl")
    private String mImageBaseUrl = "";

    public String getImageBaseUrl() {
        return mImageBaseUrl;
    }

    public List<CategoryItem> getProductList() {
        return mProductList;
    }
}
