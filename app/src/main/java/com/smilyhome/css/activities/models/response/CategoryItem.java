package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("Warehouse")
    @Expose
    private String warehouse;
    @SerializedName("RestaurantCuisineName")
    @Expose
    private String restaurantCuisineName;
    @SerializedName("RestaurantCuisineImg")
    @Expose
    private String restaurantCuisineImg;
    @SerializedName("RestaurantCuisineThumbImg")
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
    private String metaDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getRestaurantCuisineName() {
        return restaurantCuisineName;
    }

    public void setRestaurantCuisineName(String restaurantCuisineName) {
        this.restaurantCuisineName = restaurantCuisineName;
    }

    public String getRestaurantCuisineImg() {
        return restaurantCuisineImg;
    }

    public void setRestaurantCuisineImg(String restaurantCuisineImg) {
        this.restaurantCuisineImg = restaurantCuisineImg;
    }

    public String getRestaurantCuisineThumbImg() {
        return restaurantCuisineThumbImg;
    }

    public void setRestaurantCuisineThumbImg(String restaurantCuisineThumbImg) {
        this.restaurantCuisineThumbImg = restaurantCuisineThumbImg;
    }

    public String getHomeDisplay() {
        return homeDisplay;
    }

    public void setHomeDisplay(String homeDisplay) {
        this.homeDisplay = homeDisplay;
    }

    public String getSortPosition() {
        return sortPosition;
    }

    public void setSortPosition(String sortPosition) {
        this.sortPosition = sortPosition;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

    public String getParentCategoryDescription() {
        return parentCategoryDescription;
    }

    public void setParentCategoryDescription(String parentCategoryDescription) {
        this.parentCategoryDescription = parentCategoryDescription;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }
}
