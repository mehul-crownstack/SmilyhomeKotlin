package com.smilyhomeapplication.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhomeapplication.css.R;
import com.smilyhomeapplication.css.activities.Constants;
import com.smilyhomeapplication.css.activities.ToolBarManager;
import com.smilyhomeapplication.css.activities.models.requests.TransactionRequest;
import com.smilyhomeapplication.css.activities.models.response.TransactionResponse;
import com.smilyhomeapplication.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import static com.smilyhomeapplication.css.activities.Constants.USER_ID;

public class ChoosePaymentFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private String totalAmount;
    private CheckBox codCheckBox;
    private CheckBox onlineCheckBox;
    private boolean mIsCodSelected;
    private boolean mIsOnlineSelected;

    public ChoosePaymentFragment(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_choose_payment, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeOrderTextView:
            case R.id.onlineOrderContainer:
                if (!mIsCodSelected && !mIsOnlineSelected) {
                    showSnackBar(getString(R.string.err_msg_select_mode_of_payment));
                    return;
                }
                if (mIsCodSelected) {
                    placeOrderServerCall("");
                } else {
                    showProgress();
                    mActivity.startPayment(totalAmount);
                }
                break;
        }
    }

    @Override
    public void onTransactionResponse(String txnId) {
        stopProgress();
        placeOrderServerCall(txnId);
    }

    private void setupUI() {
        TextView orderSummaryHeading = mContentView.findViewById(R.id.orderSummaryHeading);
        onlineCheckBox = mContentView.findViewById(R.id.onlineCheckBox);
        codCheckBox = mContentView.findViewById(R.id.codCheckBox);
        codCheckBox.setOnCheckedChangeListener(this);
        onlineCheckBox.setOnCheckedChangeListener(this);
        onlineCheckBox.setChecked(true);
        orderSummaryHeading.setText(String.format("Total amount to pay %s%s", getString(R.string.currency), totalAmount));
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.choose_payment_option));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.getId() == codCheckBox.getId() && isChecked) {
            mIsCodSelected = true;
            codCheckBox.setChecked(true);
            onlineCheckBox.setChecked(false);
        } else if (compoundButton.getId() == codCheckBox.getId() && !isChecked) {
            mIsCodSelected = false;
        } else if (compoundButton.getId() == onlineCheckBox.getId() && isChecked) {
            mIsOnlineSelected = true;
            codCheckBox.setChecked(false);
            onlineCheckBox.setChecked(true);
        } else if (compoundButton.getId() == onlineCheckBox.getId() && !isChecked) {
            mIsOnlineSelected = false;
        }
    }

    private void placeOrderServerCall(String txnId) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TransactionRequest request = new TransactionRequest();
                    request.setUserId(getStringDataFromSharedPref(USER_ID));
                    request.setModeOfPayment(mIsCodSelected ? Constants.ModeOfPayment.COD : Constants.ModeOfPayment.ONLINE);
                    request.setTxnId(txnId);
                    Call<TransactionResponse> call = RetrofitApi.getAppServicesObjectForData().placeOrderServerCall(request);
                    final Response<TransactionResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<TransactionResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    TransactionResponse transactionResponse = response.body();
                    if (transactionResponse != null) {
                        showToast(transactionResponse.getErrorMessage());
                        if (Constants.SUCCESS.equalsIgnoreCase(transactionResponse.getErrorCode())) {
                            clearFragmentBackStack();
                            launchFragment(new TxnSuccessFragment(transactionResponse), true);
                        }
                    }
                }
            }
        }).start();
    }
}
