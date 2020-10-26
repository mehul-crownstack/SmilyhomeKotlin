package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.FetchAddressRequest;
import com.smilyhome.css.activities.models.requests.SaveAddressRequest;
import com.smilyhome.css.activities.models.requests.ZipMappingRequest;
import com.smilyhome.css.activities.models.response.CommonResponse;
import com.smilyhome.css.activities.models.response.UserAddressResponse;
import com.smilyhome.css.activities.models.response.ZipCodeResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import static com.smilyhome.css.activities.Constants.USER_ID;

public class UpdateAddressFragment extends BaseFragment {

    private EditText userNameInputEditText;
    private EditText phoneNumberInputEditText;
    private EditText addressInputEditText;
    private EditText zipCodeInputEditText;
    private TextView zipCodeCityTextView;
    private static final String TAG = "UpdateAddressFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_update_address, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    @Override
    public void onClick(View view) {
        if (R.id.updateAddressTextView == view.getId()) {
            String name = userNameInputEditText.getText().toString();
            String address = addressInputEditText.getText().toString();
            String code = zipCodeInputEditText.getText().toString();
            if (Utility.isEmpty(name)) {
                userNameInputEditText.setError(getString(R.string.msg_mandatory));
                userNameInputEditText.requestFocus();
                return;
            }
            if (Utility.isEmpty(address)) {
                addressInputEditText.setError(getString(R.string.msg_mandatory));
                addressInputEditText.requestFocus();
                return;
            }
            if (Utility.isEmpty(code)) {
                zipCodeInputEditText.setError(getString(R.string.msg_mandatory));
                zipCodeInputEditText.requestFocus();
                return;
            }
            if (getResources().getInteger(R.integer.zip_code_length) != code.length()) {
                zipCodeInputEditText.setError(getString(R.string.msg_invalid_zip_code_length));
                zipCodeInputEditText.requestFocus();
                return;
            }
            saveAddressServerCall(name, address, code);
        }
    }

    private void setupUI() {
        userNameInputEditText = mContentView.findViewById(R.id.userNameInputEditText);
        phoneNumberInputEditText = mContentView.findViewById(R.id.phoneNumberInputEditText);
        addressInputEditText = mContentView.findViewById(R.id.addressInputEditText);
        zipCodeInputEditText = mContentView.findViewById(R.id.zipCodeInputEditText);
        zipCodeCityTextView = mContentView.findViewById(R.id.zipCodeCityTextView);
        fetchUserAddressFromServer();
        zipCodeInputEditText.addTextChangedListener(new TextWatcher() {
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
                String text = editable.toString();
                if (Utility.isNotEmpty(text) && text.length() == getResources().getInteger(R.integer.zip_code_length)) {
                    hideKeyboard();
                    checkZipCodeServerCall(text);
                } else {
                    zipCodeCityTextView.setText(null);
                    zipCodeCityTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void fetchUserAddressFromServer() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FetchAddressRequest request = new FetchAddressRequest(getStringDataFromSharedPref(USER_ID));
                    Call<UserAddressResponse> call = RetrofitApi.getAppServicesObject().fetchUserAddressServerCall(request);
                    final Response<UserAddressResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<UserAddressResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    UserAddressResponse userAddressResponse = response.body();
                    if (userAddressResponse != null) {
                        showToast(userAddressResponse.getErrorMessage());
                        if (Constants.SUCCESS.equalsIgnoreCase(userAddressResponse.getErrorCode())) {
                            phoneNumberInputEditText.setText(userAddressResponse.getUserPhone());
                            userNameInputEditText.setText(userAddressResponse.getUserName());
                            zipCodeInputEditText.setText(userAddressResponse.getAddZipcode());
                            addressInputEditText.setText(userAddressResponse.getFullAddress());
                            phoneNumberInputEditText.setEnabled(false);
                        }
                    }
                }
            }
        }).start();
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.update_address));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void checkZipCodeServerCall(String zipCode) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ZipCodeResponse> call = RetrofitApi.getAppServicesObject().fetchZipCodeServerCall(new ZipMappingRequest(zipCode));
                    final Response<ZipCodeResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<ZipCodeResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    ZipCodeResponse zipCodeResponse = response.body();
                    if (zipCodeResponse != null) {
                        if (zipCodeResponse.getErrorCode().equalsIgnoreCase(Constants.SUCCESS)) {
                            showToast(zipCodeResponse.getErrorMessage());
                            zipCodeCityTextView.setVisibility(View.VISIBLE);
                            zipCodeCityTextView.setText(zipCodeResponse.getCity());
                        } else {
                            showMessage(zipCodeResponse.getErrorMessage());
                            zipCodeCityTextView.setText(null);
                            zipCodeCityTextView.setVisibility(View.GONE);
                        }
                    } else {
                        zipCodeCityTextView.setText(null);
                        zipCodeCityTextView.setVisibility(View.GONE);
                    }
                } else {
                    zipCodeCityTextView.setText(null);
                    zipCodeCityTextView.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    private void saveAddressServerCall(String userName, String zipAddress, String zipCode) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SaveAddressRequest request = new SaveAddressRequest();
                    request.setUserId(getStringDataFromSharedPref(USER_ID));
                    request.setFullAddress(zipAddress);
                    request.setUserName(userName);
                    request.setZipCode(zipCode);
                    Call<CommonResponse> call = RetrofitApi.getAppServicesObject().saveAddressServerCall(request);
                    final Response<CommonResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse != null) {
                        showToast(commonResponse.getErrorMessage());
                        fetchUserAddressFromServer();
                        fetchUserAddressServerCall();
                    }
                }
            }
        }).start();
    }
}
