package com.smilyhomeapp.css.activities.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    private RetrofitApi() {
        /*
         * This is done just to satisfy the sonar
         * */
    }

    private static final String BASE_URL_USER = "https://smilyhome.in/appapi/usrapi/data/";
    private static final String BASE_URL_PRODUCT = "https://smilyhome.in/appapi/shapi/data/";
    private static final String BASE_URL_DATA = "https://smilyhome.in/appapi/igetapi/data/";

    private static IAppServices sAppServices = null;
    private static IAppServices sAppServicesProduct = null;
    private static IAppServices sAppServicesData = null;

    public static IAppServices getAppServicesObject() {
        if (null == sAppServices) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_USER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
            sAppServices = retrofit.create(IAppServices.class);
        }
        return sAppServices;
    }

    public static IAppServices getAppServicesObjectForProducts() {
        if (null == sAppServicesProduct) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_PRODUCT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
            sAppServicesProduct = retrofit.create(IAppServices.class);
        }
        return sAppServicesProduct;
    }

    public static IAppServices getAppServicesObjectForData() {
        if (null == sAppServicesData) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
            sAppServicesData = retrofit.create(IAppServices.class);
        }
        return sAppServicesData;
    }
}
