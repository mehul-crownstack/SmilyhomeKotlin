package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.adapters.SlidingImageAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

public class GalleryFragment extends BaseFragment {

    private List<String> mSlidingImageList;

    public GalleryFragment(List<String> slidingImageList) {
        mSlidingImageList = slidingImageList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_gallery, container, false);
        setupToolbarUI();
        setupUI();
        showBottomNavigationView(false);
        return mContentView;
    }

    private void setupUI() {
        SlidingImageAdapter slidingImageAdapter = new SlidingImageAdapter(mActivity, mSlidingImageList, null);
        ViewPager viewPager = mContentView.findViewById(R.id.viewPager);
        viewPager.setAdapter(slidingImageAdapter);
        CirclePageIndicator indicator = mContentView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.gallery));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
    }
}
