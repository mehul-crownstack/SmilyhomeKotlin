package com.smilyhome.css.activities.retrofit;

import com.smilyhome.css.activities.models.requests.AddToCartRequest;
import com.smilyhome.css.activities.models.requests.CommonRequest;
import com.smilyhome.css.activities.models.requests.FetchProductRequest;
import com.smilyhome.css.activities.models.requests.InitiateOtpRequest;
import com.smilyhome.css.activities.models.requests.ProductRequest;
import com.smilyhome.css.activities.models.requests.ValidateOtpRequest;
import com.smilyhome.css.activities.models.response.CommonResponse;
import com.smilyhome.css.activities.models.response.ProductCategoryResponse;
import com.smilyhome.css.activities.models.response.ProductDetailResponse;
import com.smilyhome.css.activities.models.response.ProductResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAppServices {

    @POST("usersignup.php")
    Call<CommonResponse> initiateOtpServerCall(@Body InitiateOtpRequest request);

    @POST("otpverify.php")
    Call<CommonResponse> validateOtpServerCall(@Body ValidateOtpRequest request);

    @POST("homesection.php")
    Call<ProductResponse> fetchProductsServerCall(@Body FetchProductRequest request);

    @POST("maincat.php")
    Call<ProductCategoryResponse> fetchProductCategoryServerCall();

    @POST("logout.php")
    Call<CommonResponse> logoutServerCall(@Body CommonRequest request);

    @POST("fetchproductbyid.php")
    Call<ProductDetailResponse> productDetailServerCall(@Body ProductRequest request);

    @POST("latestuploading.php")
    Call<ProductResponse> fetchLatestProductServerCall();

    @POST("addtocart.php")
    Call<CommonResponse> addToCartServerCall(@Body AddToCartRequest request);
}
