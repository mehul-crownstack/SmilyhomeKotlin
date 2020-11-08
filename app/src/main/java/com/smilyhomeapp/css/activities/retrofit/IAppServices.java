package com.smilyhomeapp.css.activities.retrofit;

import com.smilyhomeapp.css.activities.models.requests.AddToCartRequest;
import com.smilyhomeapp.css.activities.models.requests.CancelProductRequest;
import com.smilyhomeapp.css.activities.models.requests.CategoryProductRequest;
import com.smilyhomeapp.css.activities.models.requests.CommonRequest;
import com.smilyhomeapp.css.activities.models.requests.DeleteProductRequest;
import com.smilyhomeapp.css.activities.models.requests.FetchAddressRequest;
import com.smilyhomeapp.css.activities.models.requests.FetchOrderDetailRequest;
import com.smilyhomeapp.css.activities.models.requests.FetchProductRequest;
import com.smilyhomeapp.css.activities.models.requests.InfoRequest;
import com.smilyhomeapp.css.activities.models.requests.InitiateOtpRequest;
import com.smilyhomeapp.css.activities.models.requests.ProductRequest;
import com.smilyhomeapp.css.activities.models.requests.SaveAddressRequest;
import com.smilyhomeapp.css.activities.models.requests.TransactionRequest;
import com.smilyhomeapp.css.activities.models.requests.UpdateEmailRequest;
import com.smilyhomeapp.css.activities.models.requests.ValidateOtpRequest;
import com.smilyhomeapp.css.activities.models.requests.ZipMappingRequest;
import com.smilyhomeapp.css.activities.models.response.CommonResponse;
import com.smilyhomeapp.css.activities.models.response.MyCartResponse;
import com.smilyhomeapp.css.activities.models.response.MyOrderDetailResponse;
import com.smilyhomeapp.css.activities.models.response.MyOrderResponse;
import com.smilyhomeapp.css.activities.models.response.ProductCategoryResponse;
import com.smilyhomeapp.css.activities.models.response.ProductDetailResponse;
import com.smilyhomeapp.css.activities.models.response.ProductResponse;
import com.smilyhomeapp.css.activities.models.response.ProgramAndFeatureResponse;
import com.smilyhomeapp.css.activities.models.response.TransactionResponse;
import com.smilyhomeapp.css.activities.models.response.UserAddressResponse;
import com.smilyhomeapp.css.activities.models.response.ZipCodeResponse;
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
    Call<MyCartResponse> addToCartServerCall(@Body AddToCartRequest request);

    @POST("getcartitems.php")
    Call<MyCartResponse> fetchMyCartServerCall(@Body CommonRequest request);

    @POST("deletefromcart.php")
    Call<MyCartResponse> deleteItemFromCartServerCall(@Body DeleteProductRequest request);

    @POST("productbymaincat.php")
    Call<ProductResponse> fetchCategoryProductsServerCall(@Body CategoryProductRequest request);

    @POST("zipmapping.php")
    Call<ZipCodeResponse> fetchZipCodeServerCall(@Body ZipMappingRequest request);

    @POST("updateaddress.php")
    Call<CommonResponse> saveAddressServerCall(@Body SaveAddressRequest request);

    @POST("updatemail.php")
    Call<CommonResponse> updateProfileServerCall(@Body UpdateEmailRequest request);

    @POST("fetch_profile.php")
    Call<CommonResponse> fetchProfileServerCall(@Body CommonRequest request);

    @POST("getuseraddress.php")
    Call<UserAddressResponse> fetchUserAddressServerCall(@Body FetchAddressRequest request);

    @POST("placeorder.php")
    Call<TransactionResponse> placeOrderServerCall(@Body TransactionRequest request);

    @POST("myorders.php")
    Call<MyOrderResponse> fetchMyOrderServerCall(@Body FetchAddressRequest request);

    @POST("orderdetails.php")
    Call<MyOrderDetailResponse> fetchOrderDetailServerCall(@Body FetchOrderDetailRequest request);

    @POST("cancelitem.php")
    Call<MyOrderDetailResponse> cancelProductServerCall(@Body CancelProductRequest request);

    @POST("fetch_information.php")
    Call<CommonResponse> getInformationResponse(@Body InfoRequest request);

    @POST("fetch_program_feature.php")
    Call<ProgramAndFeatureResponse> fetchProgramFeatureServerCall(@Body CommonRequest request);
}
