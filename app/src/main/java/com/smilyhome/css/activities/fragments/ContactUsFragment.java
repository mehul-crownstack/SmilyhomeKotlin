package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.InfoRequest;
import com.smilyhome.css.activities.models.response.CommonResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ContactUsFragment extends BaseFragment {

    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_static_data, container, false);
        mTextView = mContentView.findViewById(R.id.textView);
        setupToolbarUI();
        getInformationResponse();
        return mContentView;
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.contact_us));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void getInformationResponse() {
        showProgress();
        new Thread(() -> {
            try {
                Call<CommonResponse> call = RetrofitApi.getAppServicesObject().getInformationResponse(new InfoRequest(Constants.InfoConstants.MODE_CONTACT_US));
                final Response<CommonResponse> response = call.execute();
                updateOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        CommonResponse commonResponse = response.body();
                        if (commonResponse != null && Constants.SUCCESS.equalsIgnoreCase(commonResponse.getErrorCode())) {
                            Utility.writeHtmlCode(commonResponse.getErrorMessage(), mTextView);
                        } else if (commonResponse != null) {
                            showToast(commonResponse.getErrorMessage());
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
