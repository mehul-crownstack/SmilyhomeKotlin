package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem {

    @SerializedName("id")
    @Expose
    private String id = "";
    /*@SerializedName("warehouse_id")
    @Expose
    private String warehouseId;
    @SerializedName("warehouse_name")
    @Expose
    private String warehouseName;*/
    /*@SerializedName("ParentID")
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
    private String productTypeID;*/
    /*@SerializedName("ProductTypeName")
    @Expose
    private String productTypeName;
    @SerializedName("ProductBrandID")
    @Expose
    private String productBrandID;
    @SerializedName("ProductBrandName")
    @Expose
    private String productBrandName;*/
    @SerializedName("ProductCode")
    @Expose
    private String productCode = "";
    @SerializedName("ProductName")
    @Expose
    private String productName = "";
    @SerializedName("ProductShortDesc")
    @Expose
    private String productShortDesc = "";
    /*@SerializedName("Product_ExpressDelivery")
    @Expose
    private String productExpressDelivery;
    @SerializedName("Product_StandardDelivery")
    @Expose
    private String productStandardDelivery;
    @SerializedName("status")
    @Expose
    private String status;*/
    @SerializedName("Image")
    @Expose
    private String image = "";
    /*@SerializedName("Product_Stock")
    @Expose
    private String productStock;*/
    @SerializedName("ProductPrice")
    @Expose
    private String productPrice = "";
    @SerializedName("ProductSalePrice")
    @Expose
    private String productSalePrice = "";
    /*@SerializedName("productDiscountType")
    @Expose
    private String productDiscountType;*/
    @SerializedName("ProductDiscount")
    @Expose
    private String productDiscount = "";
    /*@SerializedName("productDiscountCoupon")
    @Expose
    private String productDiscountCoupon;*/
    /*@SerializedName("product_description")
    @Expose
    private String productDescription;*/
    /*@SerializedName("product_addition_description")
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
    private String location;*/

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
