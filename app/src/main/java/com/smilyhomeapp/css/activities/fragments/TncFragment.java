package com.smilyhomeapp.css.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhomeapp.css.R;
import com.smilyhomeapp.css.activities.Constants;
import com.smilyhomeapp.css.activities.ToolBarManager;
import com.smilyhomeapp.css.activities.Utility;
import com.smilyhomeapp.css.activities.models.requests.InfoRequest;
import com.smilyhomeapp.css.activities.models.response.CommonResponse;
import com.smilyhomeapp.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TncFragment extends BaseFragment {

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
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.tnc));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void getInformationResponse() {
        showProgress();
        new Thread(() -> {
            try {
                Call<CommonResponse> call = RetrofitApi.getAppServicesObject().getInformationResponse(new InfoRequest(Constants.InfoConstants.MODE_TERM_AND_CONDITION));
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
