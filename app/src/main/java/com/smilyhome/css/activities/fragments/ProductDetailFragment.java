package com.smilyhome.css.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.ProductRequest;
import com.smilyhome.css.activities.models.response.ProductDetailResponse;
import com.smilyhome.css.activities.models.response.ProductImageItem;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends BaseFragment {

    private String mProductId;
    private SlidingImageAdapter mSlidingImageAdapter;
    private TextView productDescriptionTextView;
    private TextView disclaimerTextView;

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
        showToast(mProductId);
        mSlidingImageAdapter = new SlidingImageAdapter(mActivity, new ArrayList<>());
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
                            List<String> slidingImageList = new ArrayList<>();
                            if (Utility.isNotEmpty(imageList)) {
                                for (ProductImageItem productImage : imageList) {
                                    slidingImageList.add(productImage.getProductImage());
                                }
                            }
                            if (Utility.isEmpty(imageList)) {
                                slidingImageList.add(getString(R.string.sample_category_desc));
                            }
                            mSlidingImageAdapter.setArrayList(slidingImageList);
                            mSlidingImageAdapter.notifyDataSetChanged();
                            Utility.writeHtmlCode(productResponse.getProductDescription(), productDescriptionTextView);
                            Utility.writeHtmlCode(productResponse.getProductDisclaimer(), disclaimerTextView);
                        }
                    }
                }
            }
        }).start();
    }

    private static class SlidingImageAdapter extends PagerAdapter {

        private List<String> mArrayList;
        private LayoutInflater inflater;

        public void setArrayList(List<String> arrayList) {
            mArrayList = arrayList;
        }

        SlidingImageAdapter(Context context, List<String> images) {
            this.mArrayList = images;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.sliding_images, view, false);
            final ImageView imageView = imageLayout.findViewById(R.id.image);
            Picasso.get().load(mArrayList.get(position)).placeholder(R.drawable.default_image).into(imageView);
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
