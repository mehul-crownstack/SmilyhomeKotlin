package com.smilyhome.css.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends BaseFragment {

    private String mProductId = "";

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

    private void setupUI() {
        showToast(mProductId);
        SlidingImageAdapter adapter = new SlidingImageAdapter(mActivity, new ArrayList<>());
        ViewPager viewPager = mContentView.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        CirclePageIndicator indicator = mContentView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private static class SlidingImageAdapter extends PagerAdapter {

        private List<String> mArrayList;
        private LayoutInflater inflater;

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
            //return mArrayList.size();
            return 10;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.sliding_images, view, false);
            final ImageView imageView = imageLayout.findViewById(R.id.image);
            //Picasso.get().load(mArrayList.get(position)).placeholder(R.drawable.default_image).into(imageView);
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
