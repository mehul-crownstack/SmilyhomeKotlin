package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.response.CategoryItem;
import com.smilyhome.css.activities.models.response.ProductCategoryResponse;
import com.smilyhome.css.activities.models.response.ProductItem;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean mIsDoubleBackPressClicked = false;
    private HotDealsAdapter mHotDealsAdapter;
    private HotDealsAdapter mWhatsNewAdapter;
    private AajKaOfferAdapter mAajKaOfferAdapter;
    private AajKaOfferAdapter mSuperSaverAdapter;
    private AajKaOfferAdapter mTrendingAdapter;
    private AajKaOfferAdapter mFeaturedAdapter;
    private TopCategoryAdapter mTopCategoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onStart() {
        super.onStart();
        showBottomNavigationView(true);
        navigationItemClick(1);
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
        swipeRefreshLayout = mContentView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView hotDealRecyclerView = mContentView.findViewById(R.id.hotDealRecyclerView);
        mHotDealsAdapter = new HotDealsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        hotDealRecyclerView.setLayoutManager(linearLayoutManager);
        hotDealRecyclerView.setAdapter(mHotDealsAdapter);
        RecyclerView whatsNewRecyclerView = mContentView.findViewById(R.id.whatsNewRecyclerView);
        mWhatsNewAdapter = new HotDealsAdapter();
        LinearLayoutManager whatsNewLayoutManager = new LinearLayoutManager(mActivity);
        whatsNewRecyclerView.setLayoutManager(whatsNewLayoutManager);
        whatsNewRecyclerView.setAdapter(mWhatsNewAdapter);
        RecyclerView aajKaOfferRecyclerView = mContentView.findViewById(R.id.aajKaOfferRecyclerView);
        mAajKaOfferAdapter = new AajKaOfferAdapter();
        GridLayoutManager aajKaOfferLayoutManager = new GridLayoutManager(mActivity, 2);
        aajKaOfferRecyclerView.setLayoutManager(aajKaOfferLayoutManager);
        aajKaOfferRecyclerView.setAdapter(mAajKaOfferAdapter);
        RecyclerView topCategoryRecyclerView = mContentView.findViewById(R.id.topCategoryRecyclerView);
        mTopCategoryAdapter = new TopCategoryAdapter();
        GridLayoutManager topCategoryLayoutManager = new GridLayoutManager(mActivity, 3);
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
        RecyclerView trendingRecyclerView = mContentView.findViewById(R.id.trendingRecyclerView);
        mTrendingAdapter = new AajKaOfferAdapter();
        GridLayoutManager trendingLayoutManager = new GridLayoutManager(mActivity, 2);
        trendingRecyclerView.setLayoutManager(trendingLayoutManager);
        DividerItemDecoration trendingDecoration = new DividerItemDecoration(trendingRecyclerView.getContext(), trendingLayoutManager.getOrientation());
        trendingRecyclerView.addItemDecoration(trendingDecoration);
        trendingRecyclerView.setAdapter(mTrendingAdapter);
        RecyclerView featuredRecyclerView = mContentView.findViewById(R.id.featuredRecyclerView);
        mFeaturedAdapter = new AajKaOfferAdapter();
        GridLayoutManager featuredLayoutManager = new GridLayoutManager(mActivity, 2);
        featuredRecyclerView.setLayoutManager(featuredLayoutManager);
        DividerItemDecoration featuredDecoration = new DividerItemDecoration(featuredRecyclerView.getContext(), featuredLayoutManager.getOrientation());
        featuredRecyclerView.addItemDecoration(featuredDecoration);
        featuredRecyclerView.setAdapter(mFeaturedAdapter);
        showProgress();
        fetchProductCategoryServerCall();
        fetchProductsServerCall(Constants.HomeScreenProductMode.SUPER_SAVER);
        fetchProductsServerCall(Constants.HomeScreenProductMode.AAJ_KA_OFFER);
        fetchProductsServerCall(Constants.HomeScreenProductMode.TOP_HOT_DEAL);
        fetchProductsServerCall(Constants.HomeScreenProductMode.TRENDING);
        fetchProductsServerCall(Constants.HomeScreenProductMode.FEATURED);
        fetchLatestProductServerCall(Constants.HomeScreenProductMode.WHAT_S_NEW);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void fetchProductCategoryServerCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ProductCategoryResponse> call = RetrofitApi.getAppServicesObjectForProducts().fetchProductCategoryServerCall();
                    final Response<ProductCategoryResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<ProductCategoryResponse> response) {
                if (response.isSuccessful()) {
                    ProductCategoryResponse productResponse = response.body();
                    if (productResponse != null) {
                        if (Utility.isNotEmpty(Constants.getCategoryList())) {
                            Constants.getCategoryList().clear();
                        }
                        if (Constants.SUCCESS.equalsIgnoreCase(productResponse.getErrorCode())) {
                            mTopCategoryAdapter.setImageBaseUrl(productResponse.getImageBaseUrl());
                            Constants.setCategoryList(productResponse.getProductList());
                            mTopCategoryAdapter.setCategoryList(productResponse.getProductList());
                            mTopCategoryAdapter.notifyDataSetChanged();
                        } else {
                            showToast(productResponse.getErrorMessage());
                        }
                    }
                } else {
                    stopProgress();
                }
            }
        }).start();
    }

    @Override
    protected void onProductUpdated(List<ProductItem> productItemList, int mode) {
        switch (mode) {
            case Constants.HomeScreenProductMode.FEATURED:
                mFeaturedAdapter.setProductItemList(productItemList);
                mFeaturedAdapter.notifyDataSetChanged();
                break;
            case Constants.HomeScreenProductMode.AAJ_KA_OFFER:
                mAajKaOfferAdapter.setProductItemList(productItemList);
                mAajKaOfferAdapter.notifyDataSetChanged();
                break;
            case Constants.HomeScreenProductMode.SUPER_SAVER:
                mSuperSaverAdapter.setProductItemList(productItemList);
                mSuperSaverAdapter.notifyDataSetChanged();
                break;
            case Constants.HomeScreenProductMode.TOP_HOT_DEAL:
                mHotDealsAdapter.setProductItemList(productItemList);
                mHotDealsAdapter.notifyDataSetChanged();
                break;
            case Constants.HomeScreenProductMode.TRENDING:
                mTrendingAdapter.setProductItemList(productItemList);
                mTrendingAdapter.notifyDataSetChanged();
                break;
            case Constants.HomeScreenProductMode.WHAT_S_NEW:
                mWhatsNewAdapter.setProductItemList(productItemList);
                mWhatsNewAdapter.notifyDataSetChanged();
                break;
        }
        stopProgress();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSubHeaderClickListener() {
        showToast(getString(R.string.zip_code));
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
    public void onRefresh() {
        fetchProductCategoryServerCall();
        fetchProductsServerCall(Constants.HomeScreenProductMode.SUPER_SAVER);
        fetchProductsServerCall(Constants.HomeScreenProductMode.AAJ_KA_OFFER);
        fetchProductsServerCall(Constants.HomeScreenProductMode.TOP_HOT_DEAL);
        fetchProductsServerCall(Constants.HomeScreenProductMode.TRENDING);
        fetchProductsServerCall(Constants.HomeScreenProductMode.FEATURED);
        fetchLatestProductServerCall(Constants.HomeScreenProductMode.WHAT_S_NEW);
    }

    public class HotDealsAdapter extends RecyclerView.Adapter<HotDealsAdapter.HotDealsAdapterViewHolder> {

        private List<ProductItem> productItemList = new ArrayList<>();

        public void setProductItemList(List<ProductItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public HotDealsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.hot_deal_item, parent, false);
            return new HotDealsAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HotDealsAdapterViewHolder holder, int position) {
            ProductItem item = productItemList.get(position);
            holder.productNameTextView.setText(item.getProductName());
            Utility.writeHtmlCode(item.getProductShortDesc(), holder.productDescriptionTextView);
            holder.productPriceTextView.setText(getString(R.string.currency).concat(item.getProductPrice()));
            holder.productDiscountTextView.setText(item.getProductDiscount().concat("% off"));
            holder.productDiscountPriceTextView.setText(getString(R.string.currency).concat(item.getProductSalePrice()));
            Utility.writeStrikeOffText(holder.productPriceTextView);
            Picasso.get().load(item.getImage()).placeholder(R.drawable.default_image).into(holder.productImageView);
        }

        @Override
        public int getItemCount() {
            return productItemList.size();
        }

        private class HotDealsAdapterViewHolder extends RecyclerView.ViewHolder {

            private TextView productDescriptionTextView;
            private TextView productNameTextView;
            private TextView productPriceTextView;
            private TextView productDiscountPriceTextView;
            private TextView productDiscountTextView;
            private ImageView productImageView;
            private Button buyTextView;

            public HotDealsAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                productDiscountTextView = itemView.findViewById(R.id.productDiscountTextView);
                productImageView = itemView.findViewById(R.id.productImageView);
                productDescriptionTextView = itemView.findViewById(R.id.productDescriptionTextView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
                productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
                productDiscountPriceTextView = itemView.findViewById(R.id.productDiscountPriceTextView);
                buyTextView = itemView.findViewById(R.id.buyTextView);
                productNameTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productDescriptionTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productPriceTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productDiscountPriceTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
            }
        }
    }

    private class AajKaOfferAdapter extends RecyclerView.Adapter<AajKaOfferAdapter.AajKaOfferAdapterViewHolder> {

        private List<ProductItem> productItemList = new ArrayList<>();

        public void setProductItemList(List<ProductItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public AajKaOfferAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.aaj_ka_offer_item, parent, false);
            return new AajKaOfferAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AajKaOfferAdapterViewHolder holder, int position) {
            ProductItem item = productItemList.get(position);
            holder.productNameTextView.setText(item.getProductName());
            holder.productPriceTextView.setText(getString(R.string.currency).concat(item.getProductPrice()));
            holder.productDiscountPriceTextView.setText(getString(R.string.currency).concat(item.getProductSalePrice()));
            holder.productDiscountTextView.setText(item.getProductDiscount().concat("% off"));
            Utility.writeStrikeOffText(holder.productPriceTextView);
            Picasso.get().load(item.getImage()).placeholder(R.drawable.default_image).into(holder.productImageView);
        }

        @Override
        public int getItemCount() {
            return productItemList.size();
        }

        private class AajKaOfferAdapterViewHolder extends RecyclerView.ViewHolder {

            private ImageView productImageView;
            private TextView productNameTextView;
            private TextView productPriceTextView;
            private TextView productDiscountPriceTextView;
            private TextView productDiscountTextView;
            private TextView buyTextView;

            public AajKaOfferAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                productDiscountTextView = itemView.findViewById(R.id.productDiscountTextView);
                productImageView = itemView.findViewById(R.id.productImageView);
                productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
                productDiscountPriceTextView = itemView.findViewById(R.id.productDiscountPriceTextView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
                buyTextView = itemView.findViewById(R.id.buyTextView);
                productNameTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productPriceTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productDiscountPriceTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                buyTextView.setOnClickListener(view -> {
                    showToast("buy now clicked");
                });
            }
        }
    }

    private class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.TopCategoryViewHolder> {

        private List<CategoryItem> mCategoryList = new ArrayList<>();
        private String imageBaseUrl;

        public void setImageBaseUrl(String imageBaseUrl) {
            this.imageBaseUrl = imageBaseUrl;
        }

        public void setCategoryList(List<CategoryItem> categoryList) {
            mCategoryList = categoryList;
        }

        @NonNull
        @Override
        public TopCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.top_category_item, parent, false);
            return new TopCategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopCategoryViewHolder holder, int position) {
            CategoryItem item = mCategoryList.get(position);
            Picasso.get().load(imageBaseUrl + item.getCategoryImage()).placeholder(R.drawable.default_image).into(holder.productImageView);
            holder.productNameTextView.setText(item.getCategoryName());
        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }

        private class TopCategoryViewHolder extends RecyclerView.ViewHolder {

            private ImageView productImageView;
            private TextView productNameTextView;

            public TopCategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                productImageView = itemView.findViewById(R.id.productImageView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
            }
        }
    }
}
