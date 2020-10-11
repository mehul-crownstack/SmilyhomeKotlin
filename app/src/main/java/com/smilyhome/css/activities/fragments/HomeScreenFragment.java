package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;

public class HomeScreenFragment extends BaseFragment {

    private boolean mIsDoubleBackPressClicked = false;
    private HotDealsAdapter mHotDealsAdapter;
    private AajKaOfferAdapter mAajKaOfferAdapter;
    private AajKaOfferAdapter mSuperSaverAdapter;
    private TopCategoryAdapter mTopCategoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboard();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        RecyclerView hotDealRecyclerView = mContentView.findViewById(R.id.hotDealRecyclerView);
        mHotDealsAdapter = new HotDealsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        hotDealRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(hotDealRecyclerView.getContext(), linearLayoutManager.getOrientation());
        hotDealRecyclerView.addItemDecoration(dividerItemDecoration);
        hotDealRecyclerView.setAdapter(mHotDealsAdapter);

        RecyclerView aajKaOfferRecyclerView = mContentView.findViewById(R.id.aajKaOfferRecyclerView);
        mAajKaOfferAdapter = new AajKaOfferAdapter();
        GridLayoutManager aajKaOfferLayoutManager = new GridLayoutManager(mActivity, 2);
        aajKaOfferRecyclerView.setLayoutManager(aajKaOfferLayoutManager);
        DividerItemDecoration aajKaOfferItemDecoration = new DividerItemDecoration(aajKaOfferRecyclerView.getContext(), aajKaOfferLayoutManager.getOrientation());
        aajKaOfferRecyclerView.addItemDecoration(aajKaOfferItemDecoration);
        aajKaOfferRecyclerView.setAdapter(mAajKaOfferAdapter);

        RecyclerView topCategoryRecyclerView = mContentView.findViewById(R.id.topCategoryRecyclerView);
        mTopCategoryAdapter = new TopCategoryAdapter();
        LinearLayoutManager topCategoryLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        topCategoryRecyclerView.setLayoutManager(topCategoryLayoutManager);
        DividerItemDecoration topCategoryDecoration = new DividerItemDecoration(topCategoryRecyclerView.getContext(), topCategoryLayoutManager.getOrientation());
        topCategoryRecyclerView.addItemDecoration(topCategoryDecoration);
        topCategoryRecyclerView.setAdapter(mTopCategoryAdapter);

        RecyclerView superSaverRecyclerView = mContentView.findViewById(R.id.superSaverRecyclerView);
        mSuperSaverAdapter = new AajKaOfferAdapter();
        GridLayoutManager superSaverLayoutManager = new GridLayoutManager(mActivity, 2);
        superSaverRecyclerView.setLayoutManager(superSaverLayoutManager);
        DividerItemDecoration superSaverDecoration = new DividerItemDecoration(superSaverRecyclerView.getContext(), superSaverLayoutManager.getOrientation());
        superSaverRecyclerView.addItemDecoration(superSaverDecoration);
        superSaverRecyclerView.setAdapter(mSuperSaverAdapter);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    @Override
    public void onSubHeaderClickListener() {
        showToast(getString(R.string.zip_code));
    }

    @Override
    public void onBackPressed() {
        if (mIsDoubleBackPressClicked) {
            super.onBackPressedToExit();
            return;
        }
        showSnackBar(getString(R.string.back_press_msg));
        mIsDoubleBackPressClicked = true;
        new Handler(Looper.getMainLooper()).postDelayed(() -> mIsDoubleBackPressClicked = false, 1500);
    }

    public class HotDealsAdapter extends RecyclerView.Adapter<HotDealsAdapter.HotDealsAdapterViewHolder> {

        @NonNull
        @Override
        public HotDealsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.hot_deal_item, parent, false);
            return new HotDealsAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HotDealsAdapterViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        private class HotDealsAdapterViewHolder extends RecyclerView.ViewHolder {

            private TextView buyTextView;

            public HotDealsAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                buyTextView = itemView.findViewById(R.id.buyTextView);
                buyTextView.setOnClickListener(view -> {
                    showToast("buy now clicked");
                });
            }
        }
    }

    public class AajKaOfferAdapter extends RecyclerView.Adapter<AajKaOfferAdapter.AajKaOfferAdapterViewHolder> {

        @NonNull
        @Override
        public AajKaOfferAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.aaj_ka_offer_item, parent, false);
            return new AajKaOfferAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AajKaOfferAdapterViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 7;
        }

        private class AajKaOfferAdapterViewHolder extends RecyclerView.ViewHolder {

            private TextView buyTextView;

            public AajKaOfferAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                buyTextView = itemView.findViewById(R.id.buyTextView);
                buyTextView.setOnClickListener(view -> {
                    showToast("buy now clicked");
                });
            }
        }
    }

    public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.TopCategoryViewHolder> {

        @NonNull
        @Override
        public TopCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.top_category_item, parent, false);
            return new TopCategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopCategoryViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        private class TopCategoryViewHolder extends RecyclerView.ViewHolder {

            public TopCategoryViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
