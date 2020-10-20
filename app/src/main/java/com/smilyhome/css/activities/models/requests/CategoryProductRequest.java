package com.smilyhome.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class CategoryProductRequest {

    @SerializedName("maincatid")
    private String mCategoryId;

    public CategoryProductRequest(String categoryId) {
        mCategoryId = categoryId;
    }
}
