package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.adapters.SlidingImageAdapter;
import com.smilyhome.css.activities.interfaces.IImageSliderClickListener;
import com.smilyhome.css.activities.models.requests.ProductRequest;
import com.smilyhome.css.activities.models.response.ProductDetailResponse;
import com.smilyhome.css.activities.models.response.ProductImageItem;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.viewpagerindicator.CirclePageIndicator;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends BaseFragment implements IImageSliderClickListener {

    private String mProductId;
    private SlidingImageAdapter mSlidingImageAdapter;
    private TextView productDescriptionTextView;
    private TextView disclaimerTextView;
    private List<String> mSlidingImageList;

    public ProductDetailFragment(String productId) {
        mProductId = productId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        setupToolbarUI();
        setupUI();
        showBottomNavigationView(false);
        return mContentView;
    }

    private void setupUI() {
        mSlidingImageAdapter = new SlidingImageAdapter(mActivity, new ArrayList<>(), this);
        ViewPager viewPager = mContentView.findViewById(R.id.viewPager);
        productDescriptionTextView = mContentView.findViewById(R.id.productDescriptionTextView);
        disclaimerTextView = mContentView.findViewById(R.id.disclaimerTextView);
        viewPager.setAdapter(mSlidingImageAdapter);
        CirclePageIndicator indicator = mContentView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        fetchProductDetail();
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
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
                    ProductDetailResponse productResponse = response.body();
                    if (productResponse != null) {
                        if (Constants.SUCCESS.equalsIgnoreCase(productResponse.getErrorCode())) {
                            List<ProductImageItem> imageList = productResponse.getProductImagesList();
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
                            Utility.writeHtmlCode(productResponse.getProductDescription(), productDescriptionTextView);
                            Utility.writeHtmlCode(productResponse.getProductDisclaimer(), disclaimerTextView);
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void onGalleryImageClicked() {
        launchFragment(new GalleryFragment(mSlidingImageList), true);
    }
}
