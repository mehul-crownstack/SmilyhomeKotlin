package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.CommonRequest;
import com.smilyhome.css.activities.models.requests.UpdateEmailRequest;
import com.smilyhome.css.activities.models.response.CommonResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.smilyhome.css.activities.Constants.USER_ID;

public class UpdateProfileFragment extends BaseFragment {

    private EditText userEmailInputEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        setupToolbarUI();
        setupUI();
        fetchProfileServerCall();
        return mContentView;
    }

    private void setupUI() {
        userEmailInputEditText = mContentView.findViewById(R.id.userEmailInputEditText);
    }

    @Override
    public void onClick(View view) {
        if (R.id.updateProfileTextView == view.getId()) {
            String userEmail = userEmailInputEditText.getText().toString();
            if (Utility.isEmpty(userEmail)) {
                userEmailInputEditText.setError(getString(R.string.msg_mandatory));
                userEmailInputEditText.requestFocus();
                return;
            }
            updateProfileServerCall(userEmail);
        }
    }

    private void updateProfileServerCall(String userEmailAddress) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UpdateEmailRequest request = new UpdateEmailRequest();
                    request.setUserId(getStringDataFromSharedPref(USER_ID));
                    request.setUserMail(userEmailAddress);
                    Call<CommonResponse> call = RetrofitApi.getAppServicesObject().updateProfileServerCall(request);
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
                        fetchProfileServerCall();
                    }
                }
            }
        }).start();
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.update_profile));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void fetchProfileServerCall() {
        showProgress();
        new Thread(() -> {
            try {
                Call<CommonResponse> call = RetrofitApi.getAppServicesObject().fetchProfileServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                final Response<CommonResponse> response = call.execute();
                updateOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        CommonResponse commonResponse = response.body();
                        if (commonResponse != null && Constants.SUCCESS.equalsIgnoreCase(commonResponse.getErrorCode())) {
                            userEmailInputEditText.setText(commonResponse.getErrorMessage());
                        }
                    }
                    stopProgress();
                });
            } catch (IOException e) {
                updateOnUiThread(this::stopProgress);
                Log.e(TAG, e.getMessage(), e);
            }
        }).start();
    }
}