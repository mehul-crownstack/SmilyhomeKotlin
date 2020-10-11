package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("warehouse_id")
    @Expose
    private String warehouseId;
    @SerializedName("warehouse_name")
    @Expose
    private String warehouseName;
    @SerializedName("ParentID")
    @Expose
    private String parentID;
    @SerializedName("ParentCatName")
    @Expose
    private String parentCatName;
    @SerializedName("SubParentID")
    @Expose
    private String subParentID;
    @SerializedName("SubParentCatName")
    @Expose
    private String subParentCatName;
    @SerializedName("SubParentChildID")
    @Expose
    private String subParentChildID;
    @SerializedName("SubParentCatChildName")
    @Expose
    private String subParentCatChildName;
    @SerializedName("ProductTypeID")
    @Expose
    private String productTypeID;
    @SerializedName("ProductTypeName")
    @Expose
    private String productTypeName;
    @SerializedName("ProductBrandID")
    @Expose
    private String productBrandID;
    @SerializedName("ProductBrandName")
    @Expose
    private String productBrandName;
    @SerializedName("Product_Code")
    @Expose
    private String productCode;
    @SerializedName("Product_Name")
    @Expose
    private String productName;
    @SerializedName("Product_ShortDesc")
    @Expose
    private String productShortDesc;
    @SerializedName("Product_ExpressDelivery")
    @Expose
    private String productExpressDelivery;
    @SerializedName("Product_StandardDelivery")
    @Expose
    private String productStandardDelivery;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Product_Stock")
    @Expose
    private String productStock;
    @SerializedName("Product_Price")
    @Expose
    private String productPrice;
    @SerializedName("Productfeature")
    @Expose
    private String productfeature;
    @SerializedName("productDiscountType")
    @Expose
    private String productDiscountType;
    @SerializedName("productDiscount")
    @Expose
    private String productDiscount;
    @SerializedName("productDiscountCoupon")
    @Expose
    private String productDiscountCoupon;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_addition_description")
    @Expose
    private String productAdditionDescription;
    @SerializedName("product_warrenty_description")
    @Expose
    private String productWarrentyDescription;
    @SerializedName("product_redressal_description")
    @Expose
    private String productRedressalDescription;
    @SerializedName("product_cancellation_description")
    @Expose
    private String productCancellationDescription;
    @SerializedName("resMetaTitle")
    @Expose
    private String resMetaTitle;
    @SerializedName("resMetakeyword")
    @Expose
    private String resMetakeyword;
    @SerializedName("resMetadescription")
    @Expose
    private String resMetadescription;
    @SerializedName("Product_NamePostion")
    @Expose
    private String productNamePostion;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("Product_Tag")
    @Expose
    private String productTag;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("location")
    @Expose
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getParentCatName() {
        return parentCatName;
    }

    public void setParentCatName(String parentCatName) {
        this.parentCatName = parentCatName;
    }

    public String getSubParentID() {
        return subParentID;
    }

    public void setSubParentID(String subParentID) {
        this.subParentID = subParentID;
    }

    public String getSubParentCatName() {
        return subParentCatName;
    }

    public void setSubParentCatName(String subParentCatName) {
        this.subParentCatName = subParentCatName;
    }

    public String getSubParentChildID() {
        return subParentChildID;
    }

    public void setSubParentChildID(String subParentChildID) {
        this.subParentChildID = subParentChildID;
    }

    public String getSubParentCatChildName() {
        return subParentCatChildName;
    }

    public void setSubParentCatChildName(String subParentCatChildName) {
        this.subParentCatChildName = subParentCatChildName;
    }

    public String getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(String productTypeID) {
        this.productTypeID = productTypeID;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductBrandID() {
        return productBrandID;
    }

    public void setProductBrandID(String productBrandID) {
        this.productBrandID = productBrandID;
    }

    public String getProductBrandName() {
        return productBrandName;
    }

    public void setProductBrandName(String productBrandName) {
        this.productBrandName = productBrandName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShortDesc() {
        return productShortDesc;
    }

    public void setProductShortDesc(String productShortDesc) {
        this.productShortDesc = productShortDesc;
    }

    public String getProductExpressDelivery() {
        return productExpressDelivery;
    }

    public void setProductExpressDelivery(String productExpressDelivery) {
        this.productExpressDelivery = productExpressDelivery;
    }

    public String getProductStandardDelivery() {
        return productStandardDelivery;
    }

    public void setProductStandardDelivery(String productStandardDelivery) {
        this.productStandardDelivery = productStandardDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductfeature() {
        return productfeature;
    }

    public void setProductfeature(String productfeature) {
        this.productfeature = productfeature;
    }

    public String getProductDiscountType() {
        return productDiscountType;
    }

    public void setProductDiscountType(String productDiscountType) {
        this.productDiscountType = productDiscountType;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductDiscountCoupon() {
        return productDiscountCoupon;
    }

    public void setProductDiscountCoupon(String productDiscountCoupon) {
        this.productDiscountCoupon = productDiscountCoupon;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductAdditionDescription() {
        return productAdditionDescription;
    }

    public void setProductAdditionDescription(String productAdditionDescription) {
        this.productAdditionDescription = productAdditionDescription;
    }

    public String getProductWarrentyDescription() {
        return productWarrentyDescription;
    }

    public void setProductWarrentyDescription(String productWarrentyDescription) {
        this.productWarrentyDescription = productWarrentyDescription;
    }

    public String getProductRedressalDescription() {
        return productRedressalDescription;
    }

    public void setProductRedressalDescription(String productRedressalDescription) {
        this.productRedressalDescription = productRedressalDescription;
    }

    public String getProductCancellationDescription() {
        return productCancellationDescription;
    }

    public void setProductCancellationDescription(String productCancellationDescription) {
        this.productCancellationDescription = productCancellationDescription;
    }

    public String getResMetaTitle() {
        return resMetaTitle;
    }

    public void setResMetaTitle(String resMetaTitle) {
        this.resMetaTitle = resMetaTitle;
    }

    public String getResMetakeyword() {
        return resMetakeyword;
    }

    public void setResMetakeyword(String resMetakeyword) {
        this.resMetakeyword = resMetakeyword;
    }

    public String getResMetadescription() {
        return resMetadescription;
    }

    public void setResMetadescription(String resMetadescription) {
        this.resMetadescription = resMetadescription;
    }

    public String getProductNamePostion() {
        return productNamePostion;
    }

    public void setProductNamePostion(String productNamePostion) {
        this.productNamePostion = productNamePostion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
