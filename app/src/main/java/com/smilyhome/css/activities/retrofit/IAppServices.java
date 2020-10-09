package com.smilyhome.css.activities.retrofit;

import com.smilyhome.css.activities.models.requests.InitiateOtpRequest;
import com.smilyhome.css.activities.models.response.CommonResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAppServices {

    @POST("usersignup.php")
    Call<CommonResponse> initiateOtpServerCall(@Body InitiateOtpRequest request);
}
