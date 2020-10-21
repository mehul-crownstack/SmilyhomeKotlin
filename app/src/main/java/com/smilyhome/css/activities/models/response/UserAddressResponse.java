package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

public class UserAddressResponse extends CommonResponse {

    @SerializedName("userName")
    private String userName;
    @SerializedName("userPhone")
    private String userPhone;
    @SerializedName("fullAddress")
    private String fullAddress;
    @SerializedName("addZipcode")
    private String addZipcode;
    @SerializedName("getState")
    private String stateName;
    @SerializedName("getCity")
    private String cityName;

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getAddZipcode() {
        return addZipcode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getCityName() {
        return cityName;
    }
}
