package com.smilyhomeapplication.css.activities.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem {

    @SerializedName("id")
    @Expose
    private String id;
    /*@SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("Warehouse")
    @Expose
    private String warehouse;*/
    @SerializedName("mainCatName")
    @Expose
    private String categoryName;
    @SerializedName("catImage")
    @Expose
    private String categoryImage;
    /*@SerializedName("RestaurantCuisineThumbImg")
    @Expose
    private String restaurantCuisineThumbImg;
    @SerializedName("HomeDisplay")
    @Expose
    private String homeDisplay;
    @SerializedName("sortPosition")
    @Expose
    private String sortPosition;
    @SerializedName("discount_available")
    @Expose
    private String discountAvailable;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("created_ip")
    @Expose
    private String createdIp;
    @SerializedName("parent_categoryDescription")
    @Expose
    private String parentCategoryDescription;
    @SerializedName("meta_Title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_keyword")
    @Expose
    private String metaKeyword;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription; */

    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }
}
