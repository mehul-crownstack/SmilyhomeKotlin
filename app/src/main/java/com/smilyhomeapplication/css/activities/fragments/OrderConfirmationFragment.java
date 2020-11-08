package com.smilyhomeapplication.css.activities.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.smilyhomeapplication.css.R;
import com.smilyhomeapplication.css.activities.Constants;
import com.smilyhomeapplication.css.activities.ToolBarManager;
import com.smilyhomeapplication.css.activities.Utility;
import com.smilyhomeapplication.css.activities.models.requests.FetchAddressRequest;
import com.smilyhomeapplication.css.activities.models.requests.SaveAddressRequest;
import com.smilyhomeapplication.css.activities.models.requests.ZipMappingRequest;
import com.smilyhomeapplication.css.activities.models.response.CommonResponse;
import com.smilyhomeapplication.css.activities.models.response.MyCartResponse;
import com.smilyhomeapplication.css.activities.models.response.UserAddressResponse;
import com.smilyhomeapplication.css.activities.models.response.ZipCodeResponse;
import com.smilyhomeapplication.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import static com.smilyhomeapplication.css.activities.Constants.USER_ID;

public class OrderConfirmationFragment extends BaseFragment {

    private MyCartResponse myCartResponse;
    private boolean mIsDeliveryAvailable = false;
    private TextView addAddressTextView;
    private TextView zipCodeTextView;
    private TextView fullAddressTextView;
    private TextView nameAddress;
    private TextView zipCodeCityTextView;
    private Dialog mAddressDialog;
    private static final String TAG = "OrderConfirmation";

    public OrderConfirmationFragment(MyCartResponse cartResponse) {
        this.myCartResponse = cartResponse;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_order_confirmation, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        TextView mrpTextView = mContentView.findViewById(R.id.mrpTextView);
        TextView productDiscountTextView = mContentView.findViewById(R.id.productDiscountTextView);
        TextView earningDiscountTextView = mContentView.findViewById(R.id.earningDiscountTextView);
        TextView deliveryChargesTextView = mContentView.findViewById(R.id.deliveryChargesTextView);
        TextView totalAmountToPayTextView = mContentView.findViewById(R.id.totalAmountToPayTextView);
        zipCodeTextView = mContentView.findViewById(R.id.zipCodeTextView);
        fullAddressTextView = mContentView.findViewById(R.id.fullAddressTextView);
        nameAddress = mContentView.findViewById(R.id.noAddress);
        addAddressTextView = mContentView.findViewById(R.id.addAddressTextView);
        mrpTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getTotalMrp()));
        productDiscountTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getProductDiscount()));
        earningDiscountTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getEarningDiscount()));
        deliveryChargesTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getDeliveryCharges()));
        totalAmountToPayTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getTotalPaybleAmount()));
        fetchUserAddressServerCallForOrderConfirmation();
        fetchUserAddressServerCall();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (mAddressDialog != null) {
                    TextInputEditText nameInputEditText = mAddressDialog.findViewById(R.id.nameInputEditText);
                    TextInputEditText addressInputEditText = mAddressDialog.findViewById(R.id.addressInputEditText);
                    TextInputEditText zipPinInputEditText = mAddressDialog.findViewById(R.id.zipPinInputEditText);
                    String name = nameInputEditText.getText().toString();
                    String address = addressInputEditText.getText().toString();
                    String code = zipPinInputEditText.getText().toString();
                    if (Utility.isEmpty(name)) {
                        nameInputEditText.setError(getString(R.string.msg_mandatory));
                        nameInputEditText.requestFocus();
                        return;
                    }
                    if (Utility.isEmpty(address)) {
                        addressInputEditText.setError(getString(R.string.msg_mandatory));
                        addressInputEditText.requestFocus();
                        return;
                    }
                    if (Utility.isEmpty(code)) {
                        zipPinInputEditText.setError(getString(R.string.msg_mandatory));
                        zipPinInputEditText.requestFocus();
                        return;
                    }
                    if (getResources().getInteger(R.integer.zip_code_length) != code.length()) {
                        zipPinInputEditText.setError(getString(R.string.msg_invalid_zip_code_length));
                        zipPinInputEditText.requestFocus();
                        return;
                    }
                    saveAddressServerCall(name, address, code);
                }
                closeAddressDialog();
                break;
            case R.id.cancel:
                closeAddressDialog();
                break;
            case R.id.proceedToPayTextView:
                if (mIsDeliveryAvailable) {
                    launchFragment(new ChoosePaymentFragment(myCartResponse.getTotalPaybleAmount()), true);
                } else {
                    showToast(getString(R.string.no_delivery_msg));
                }
                break;
            case R.id.addAddressTextView:
                showInsertAddressDialog();
                break;
            default:
                break;
        }
    }

    private void checkZipCodeServerCall(String zipCode) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mIsDeliveryAvailable = false;
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
                            mIsDeliveryAvailable = true;
                            showToast(zipCodeResponse.getErrorMessage());
                            zipCodeCityTextView.setVisibility(View.VISIBLE);
                            zipCodeCityTextView.setText(zipCodeResponse.getCity());
                        } else {
                            showMessage(zipCodeResponse.getErrorMessage());
                            closeAddressDialog();
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
                        fetchUserAddressServerCallForOrderConfirmation();
                        fetchUserAddressServerCall();
                    }
                }
            }
        }).start();
    }

    private void fetchUserAddressServerCallForOrderConfirmation() {
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
                            if (View.VISIBLE != fullAddressTextView.getVisibility()) {
                                fullAddressTextView.setVisibility(View.VISIBLE);
                            }
                            if (View.VISIBLE != zipCodeTextView.getVisibility()) {
                                zipCodeTextView.setVisibility(View.VISIBLE);
                            }
                            mIsDeliveryAvailable = true;
                            zipCodeTextView.setText(String.format("Mob : %s", userAddressResponse.getUserPhone()));
                            nameAddress.setText(userAddressResponse.getUserName());
                            fullAddressTextView.setText(String.format("%s,%s\n\n%s, %s", userAddressResponse.getFullAddress(),
                                                                      userAddressResponse.getAddZipcode(),
                                                                      userAddressResponse.getCityName(),
                                                                      userAddressResponse.getStateName()));
                            addAddressTextView.setText(getString(R.string.change_address));
                        }
                    }
                }
            }
        }).start();
    }

    private void closeAddressDialog() {
        if (mAddressDialog != null && mAddressDialog.isShowing()) {
            mAddressDialog.dismiss();
        }
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.select_address));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void showInsertAddressDialog() {
        mAddressDialog = new Dialog(mActivity);
        mAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAddressDialog.setCancelable(true);
        mAddressDialog.setContentView(R.layout.dialog_add_address);
        View submit = mAddressDialog.findViewById(R.id.submit);
        View cancel = mAddressDialog.findViewById(R.id.cancel);
        zipCodeCityTextView = mAddressDialog.findViewById(R.id.zipCodeCityTextView);
        TextInputEditText zipPinInputEditText = mAddressDialog.findViewById(R.id.zipPinInputEditText);
        zipPinInputEditText.addTextChangedListener(new TextWatcher() {
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
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mAddressDialog.show();
    }
}
