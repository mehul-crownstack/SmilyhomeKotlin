package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.adapters.SlidingImageAdapter;
import com.smilyhome.css.activities.interfaces.IImageSliderClickListener;
import com.smilyhome.css.activities.models.requests.AddToCartRequest;
import com.smilyhome.css.activities.models.requests.ProductRequest;
import com.smilyhome.css.activities.models.response.MyCartResponse;
import com.smilyhome.css.activities.models.response.ProductDetailResponse;
import com.smilyhome.css.activities.models.response.ProductFeatureItem;
import com.smilyhome.css.activities.models.response.ProductImageItem;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.viewpagerindicator.CirclePageIndicator;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends BaseFragment implements IImageSliderClickListener, RadioGroup.OnCheckedChangeListener {

    private String mProductId;
    private SlidingImageAdapter mSlidingImageAdapter;
    private TextView productDescriptionTextView;
    private TextView productNameTextView;
    private ProductDetailResponse mProductDetailResponse;
    private TextView productPriceTextView;
    private TextView productDiscountPriceTextView;
    private TextView productDiscountTextView;
    private TextView yourPayTextView;
    private TextView productDisclaimerTextView;
    private RadioButton buyOneRadioButton;
    private RadioButton buyMoreRadioButton;
    private double mProductPrice;
    private String[] mBuyMoreList;
    private List<String> mSlidingImageList;
    private int mSelectedQuantity = 1;
    private RecyclerView featuresRecyclerView;

    public ProductDetailFragment(String productId) {
        mProductId = productId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showBottomNavigationView(true);
    }

    private void setupUI() {
        mSlidingImageAdapter = new SlidingImageAdapter(mActivity, new ArrayList<>(), this);
        ViewPager viewPager = mContentView.findViewById(R.id.viewPager);
        productDescriptionTextView = mContentView.findViewById(R.id.productDescriptionTextView);
        productPriceTextView = mContentView.findViewById(R.id.productPriceTextView);
        productDiscountPriceTextView = mContentView.findViewById(R.id.productDiscountPriceTextView);
        productDiscountTextView = mContentView.findViewById(R.id.productDiscountTextView);
        yourPayTextView = mContentView.findViewById(R.id.yourPayTextView);
        productNameTextView = mContentView.findViewById(R.id.productNameTextView);
        buyOneRadioButton = mContentView.findViewById(R.id.buyOneRadioButton);
        buyMoreRadioButton = mContentView.findViewById(R.id.buyMoreRadioButton);
        productDisclaimerTextView = mContentView.findViewById(R.id.productDisclaimerTextView);
        RadioGroup buyRadioGroup = mContentView.findViewById(R.id.buyRadioGroup);
        buyRadioGroup.setOnCheckedChangeListener(this);
        viewPager.setAdapter(mSlidingImageAdapter);
        CirclePageIndicator indicator = mContentView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        fetchProductDetail();
        buyOneRadioButton.setChecked(true);
        featuresRecyclerView = mContentView.findViewById(R.id.featuresRecyclerView);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.product_detail));
        //ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addToBagTextView:
            case R.id.buyNowTextView:
                AddToCartRequest request = new AddToCartRequest();
                request.setProductId(mProductDetailResponse.getProductId());
                request.setUserId(getStringDataFromSharedPref(Constants.USER_ID));
                request.setProductQuantity(String.valueOf(mSelectedQuantity));
                request.setProductImage(mProductDetailResponse.getProductImage());
                request.setProductName(mProductDetailResponse.getProductName());
                addToCartServerCall(request);
                break;
            case R.id.shareEarnTextView:
                mActivity.shareApplication();
                break;
            default:
                break;
        }
    }

    private void fetchProductDetail() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ProductDetailResponse> call = RetrofitApi.getAppServicesObjectForProducts().productDetailServerCall(new ProductRequest(mProductId));
                    final Response<ProductDetailResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<ProductDetailResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    mProductDetailResponse = response.body();
                    if (mProductDetailResponse != null) {
                        if (Constants.SUCCESS.equalsIgnoreCase(mProductDetailResponse.getErrorCode())) {
                            List<ProductImageItem> imageList = mProductDetailResponse.getProductImagesList();
                            mSlidingImageList = new ArrayList<>();
                            if (Utility.isNotEmpty(imageList)) {
                                for (ProductImageItem productImage : imageList) {
                                    mSlidingImageList.add(productImage.getProductImage());
                                }
                            }
                            if (Utility.isEmpty(imageList)) {
                                mSlidingImageList.add(getString(R.string.sample_category_desc));
                            }
                            mSlidingImageAdapter.setArrayList(mSlidingImageList);
                            mSlidingImageAdapter.notifyDataSetChanged();
                            Utility.writeHtmlCode(mProductDetailResponse.getProductDescription(), productDescriptionTextView);
                            Utility.writeHtmlCode(mProductDetailResponse.getProductName(), productNameTextView);
                            String disclaimerText = "Disclaimer : " + mProductDetailResponse.getProductDisclaimer();
                            Utility.writeHtmlCode(disclaimerText, productDisclaimerTextView);
                            productPriceTextView.setText(getString(R.string.currency).concat(mProductDetailResponse.getProductPrice()));
                            productDiscountPriceTextView.setText(getString(R.string.currency).concat(mProductDetailResponse.getProductSalePrice()));
                            Utility.writeStrikeOffText(productPriceTextView);
                            mProductPrice = Double.parseDouble(Utility.isEmpty(mProductDetailResponse.getProductSalePrice()) ? "0" : mProductDetailResponse.getProductSalePrice());
                            buyOneRadioButton.setText(String.format("Buy 1 for %s %s", getString(R.string.currency), mProductPrice));
                            yourPayTextView.setText(String.format("%s %s", getString(R.string.currency), mProductPrice));
                            productDiscountTextView.setText(mProductDetailResponse.getProductDiscount().concat("% off"));
                            mBuyMoreList = new String[]{"Buy 1 for " + getString(R.string.currency) + mProductPrice,
                                "Buy 2 for " + getString(R.string.currency) + (2 * mProductPrice),
                                "Buy 3 for " + getString(R.string.currency) + (3 * mProductPrice),
                                "Buy 4 for " + getString(R.string.currency) + (4 * mProductPrice),
                                "Buy 5 for " + getString(R.string.currency) + (5 * mProductPrice)};
                        }
                        TextView expressDeliveryTextView = mContentView.findViewById(R.id.expressDeliveryTextView);
                        Utility.writeHtmlCode(mProductDetailResponse.getExpressDelivery(), expressDeliveryTextView);
                        TextView standardDeliveryTextView = mContentView.findViewById(R.id.standardDeliveryTextView);
                        Utility.writeHtmlCode(mProductDetailResponse.getStandardDelivery(), standardDeliveryTextView);
                        setupFeatureRecyclerView(mProductDetailResponse);
                    }
                }
            }
        }).start();
    }

    private void setupFeatureRecyclerView(ProductDetailResponse response) {
        List<ProductFeatureItem> featureItemList = new ArrayList<>();
        if (Utility.isNotEmpty(response.getProductDataSize())) {
            featureItemList.addAll(response.getProductDataSize());
        }
        if (Utility.isNotEmpty(response.getProductDataAttribute())) {
            featureItemList.addAll(response.getProductDataAttribute());
        }
        FeaturesAdapter adapter = new FeaturesAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        featuresRecyclerView.setLayoutManager(manager);
        featuresRecyclerView.setAdapter(adapter);
        adapter.setFeatureList(featureItemList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGalleryImageClicked() {
        launchFragment(new GalleryFragment(mSlidingImageList), true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.buyOneRadioButton:
                mSelectedQuantity = 1;
                yourPayTextView.setText(String.format("%s %s", getString(R.string.currency), mProductPrice));
                break;
            case R.id.buyMoreRadioButton:
                showListAlertDialog(mBuyMoreList, R.id.buyMoreRadioButton, "Select ".concat(mProductDetailResponse.getProductName()));
                break;
        }
    }

    @Override
    protected void onAlertDialogItemClicked(String selectedStr, int id, int position) {
        if (id == R.id.buyMoreRadioButton) {
            mSelectedQuantity = position + 1;
            buyMoreRadioButton.setText(mBuyMoreList[position]);
            double payAmount = mProductPrice * mSelectedQuantity;
            yourPayTextView.setText(String.format("%s %s", getString(R.string.currency), payAmount));
        }
    }

    @Override
    protected void onUpdatedAddToCartResponse(MyCartResponse response) {
        showToast(response.getErrorMessage());
        launchFragment(new MyCartFragment(), true);
    }

    private static class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.TopCategoryViewHolder> {

        private List<ProductFeatureItem> mFeatureList = new ArrayList<>();

        public void setFeatureList(List<ProductFeatureItem> featureList) {
            mFeatureList = featureList;
        }

        @NonNull
        @Override
        public FeaturesAdapter.TopCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.product_features_item, parent, false);
            return new FeaturesAdapter.TopCategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeaturesAdapter.TopCategoryViewHolder holder, int position) {
            ProductFeatureItem item = mFeatureList.get(position);
            Utility.writeHtmlCode(item.getFeatureLabel(), holder.featureLabelTextView);
            Utility.writeHtmlCode(item.getFeatureValue(), holder.featureValueTextView);
        }

        @Override
        public int getItemCount() {
            return mFeatureList.size();
        }

        private static class TopCategoryViewHolder extends RecyclerView.ViewHolder {

            private TextView featureLabelTextView;
            private TextView featureValueTextView;

            public TopCategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                featureLabelTextView = itemView.findViewById(R.id.featureLabelTextView);
                featureValueTextView = itemView.findViewById(R.id.featureValueTextView);
            }
        }
    }
}
