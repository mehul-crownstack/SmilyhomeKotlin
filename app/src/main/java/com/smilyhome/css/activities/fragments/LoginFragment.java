package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.OtpBroadCastReceiver;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.interfaces.IAutoReadOtpListener;
import com.smilyhome.css.activities.models.requests.InitiateOtpRequest;
import com.smilyhome.css.activities.models.requests.ValidateOtpRequest;
import com.smilyhome.css.activities.models.response.CommonResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import static com.smilyhome.css.activities.Constants.USER_ID;

public class LoginFragment extends BaseFragment implements IAutoReadOtpListener {

    private boolean mIsDoubleBackPressClicked = false;
    private EditText mobileNumberInputEditText;
    private EditText nameInputEditText;
    private EditText otpInputEditText;
    private static final String TAG = "LoginFragment";
    private View loginContainer;
    private View otpContainer;
    private String mOtpReceivedStr = "";
    private String mUserId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBottomNavigationView(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_login, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        mobileNumberInputEditText = mContentView.findViewById(R.id.mobileNumberInputEditText);
        nameInputEditText = mContentView.findViewById(R.id.nameInputEditText);
        otpInputEditText = mContentView.findViewById(R.id.otpInputEditText);
        new OtpBroadCastReceiver().setOtpListener(this);
        mobileNumberInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (Utility.isNotEmpty(str) && str.length() == getResources().getInteger(R.integer.mobile_length)) {
                    mobileNumberInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.tick_small, 0);
                    nameInputEditText.requestFocus();
                } else {
                    mobileNumberInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        loginContainer = mContentView.findViewById(R.id.loginContainer);
        otpContainer = mContentView.findViewById(R.id.otpContainer);
        loginContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generateOtpTextView:
                String mobileStr = mobileNumberInputEditText.getText().toString();
                String nameStr = nameInputEditText.getText().toString();
                if (Utility.isEmpty(mobileStr)) {
                    mobileNumberInputEditText.setError(getString(R.string.msg_mandatory));
                    mobileNumberInputEditText.requestFocus();
                    return;
                }
                if (mobileStr.length() < getResources().getInteger(R.integer.mobile_length)) {
                    mobileNumberInputEditText.setError(getString(R.string.not_valid_mobile_number));
                    mobileNumberInputEditText.requestFocus();
                    return;
                }
                if (Utility.isEmpty(nameStr)) {
                    nameInputEditText.setError(getString(R.string.msg_mandatory));
                    nameInputEditText.requestFocus();
                    return;
                }
                if (isInternetConnectionAvailable()) {
                    initiateOtpServerCall(nameStr, mobileStr);
                }
                break;
            case R.id.validateOtpTextView:
                if (isInternetConnectionAvailable()) {
                    validateOtpServerCall();
                }
                break;
            default:
                break;
        }
    }

    private void initiateOtpServerCall(String userName, String mobileNumber) {
        showProgress();
        hideKeyboard();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InitiateOtpRequest request = new InitiateOtpRequest();
                    request.setUserName(userName);
                    request.setMobileNumber(mobileNumber);
                    Call<CommonResponse> call = RetrofitApi.getAppServicesObject().initiateOtpServerCall(request);
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (Constants.SUCCESS.equalsIgnoreCase(commonResponse.getErrorCode())) {
                            mUserId = commonResponse.getUserId();
                            otpContainer.setVisibility(View.VISIBLE);
                            loginContainer.setVisibility(View.GONE);
                            otpInputEditText.setText(null);
                        }
                        showToast(commonResponse.getErrorMessage());
                    }
                }
            }
        }).start();
    }

    private void validateOtpServerCall() {
        if (Utility.isEmpty(mOtpReceivedStr)) {
            mOtpReceivedStr = otpInputEditText.getText().toString();
        }
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ValidateOtpRequest request = new ValidateOtpRequest(mOtpReceivedStr, mUserId);
                    Call<CommonResponse> call = RetrofitApi.getAppServicesObject().validateOtpServerCall(request);
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                hideKeyboard();
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        if (Constants.SUCCESS.equalsIgnoreCase(commonResponse.getErrorCode())) {
                            storeStringDataInSharedPref(USER_ID, mUserId);
                            clearFragmentBackStack();
                            launchFragment(new HomeScreenFragment(), false);
                        }
                        showToast(commonResponse.getErrorMessage());
                    }
                }
            }
        }).start();
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
    }

    @Override
    public boolean onBackPressed() {
        if (mIsDoubleBackPressClicked) {
            super.onBackPressedToExit();
            return true;
        }
        Snackbar.make(mContentView, getString(R.string.back_press_msg), Snackbar.LENGTH_SHORT).show();
        mIsDoubleBackPressClicked = true;
        new Handler(Looper.getMainLooper()).postDelayed(() -> mIsDoubleBackPressClicked = false, 1500);
        return true;
    }

    @Override
    public void onOtpReceived(String otp) {
        otpInputEditText.setText(otp);
        if (otpContainer.getVisibility() == View.VISIBLE) {
            mOtpReceivedStr = otp;
            mContentView.findViewById(R.id.validateOtpTextView).callOnClick();
        }
    }
}