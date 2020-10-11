package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryResponse extends CommonResponse {

    @SerializedName("data")
    private List<CategoryItem> mProductList = new ArrayList<>();

    public List<CategoryItem> getProductList() {
        return mProductList;
    }
}
