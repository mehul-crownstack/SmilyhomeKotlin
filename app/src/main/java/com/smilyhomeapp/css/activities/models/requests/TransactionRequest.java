package com.smilyhomeapp.css.activities.models.requests;

import com.google.gson.annotations.SerializedName;

public class TransactionRequest {

    @SerializedName("userId")
    private String mUserId;
    @SerializedName("modeOfPayment")
    private String modeOfPayment;
    @SerializedName("txnId")
    private String txnId;

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
