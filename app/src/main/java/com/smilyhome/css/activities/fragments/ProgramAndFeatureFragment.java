package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.CommonRequest;
import com.smilyhome.css.activities.models.response.ProgramAndFeatureItem;
import com.smilyhome.css.activities.models.response.ProgramAndFeatureResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.smilyhome.css.activities.Constants.USER_ID;

public class ProgramAndFeatureFragment extends BaseFragment {

    private ProgramFeatureAdapter mAdapter = new ProgramFeatureAdapter();
    private TextView pageDisplayTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_program_and_feature, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        pageDisplayTextView = mContentView.findViewById(R.id.pageDisplayTextView);
        RecyclerView recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mAdapter);
        fetchProgramFeatureServerCall();
    }

    private void fetchProgramFeatureServerCall() {
        showProgress();
        new Thread(() -> {
            try {
                Call<ProgramAndFeatureResponse> call = RetrofitApi.getAppServicesObjectForProducts().fetchProgramFeatureServerCall(new CommonRequest(getStringDataFromSharedPref(USER_ID)));
                final Response<ProgramAndFeatureResponse> response = call.execute();
                updateOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        ProgramAndFeatureResponse featureResponse = response.body();
                        if (featureResponse != null) {
                            showToast(featureResponse.getErrorMessage());
                            if (Constants.SUCCESS.equalsIgnoreCase(featureResponse.getErrorCode())) {
                                List<ProgramAndFeatureItem> list = featureResponse.getProgramAndFeatureList();
                                Utility.writeHtmlCode(featureResponse.getPageDisplay(), pageDisplayTextView);
                                if (Utility.isNotEmpty(list)) {
                                    mAdapter.setFeatureItemList(list);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    stopProgress();
                });
            } catch (Exception e) {
                updateOnUiThread(this::stopProgress);
                Log.e(TAG, e.getMessage(), e);
            }
        }).start();
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.program_and_feature));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private class ProgramFeatureAdapter extends RecyclerView.Adapter<ProgramFeatureAdapter.MyCartViewHolder> {

        private List<ProgramAndFeatureItem> mFeatureItemList = new ArrayList<>();

        public void setFeatureItemList(List<ProgramAndFeatureItem> featureItemList) {
            this.mFeatureItemList = featureItemList;
        }

        @NonNull
        @Override
        public ProgramFeatureAdapter.MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.program_feature_item, parent, false);
            return new ProgramFeatureAdapter.MyCartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProgramFeatureAdapter.MyCartViewHolder holder, int position) {
            ProgramAndFeatureItem item = mFeatureItemList.get(position);
            holder.textView.setText(item.getLabel());
        }

        @Override
        public int getItemCount() {
            return mFeatureItemList.size();
        }

        private class MyCartViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public MyCartViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                textView.setOnClickListener(view -> launchFragment(new WebViewFragment(mFeatureItemList.get(getAdapterPosition()).getLink()), true));
            }
        }
    }
}
