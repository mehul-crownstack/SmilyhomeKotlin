package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductResponse extends CommonResponse {

    @SerializedName("data")
    private List<ProductItem> mProductList = new ArrayList<>();

    public List<ProductItem> getProductList() {
        return mProductList;
    }
}
