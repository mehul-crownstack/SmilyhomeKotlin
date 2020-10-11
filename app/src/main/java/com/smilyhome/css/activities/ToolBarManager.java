package com.smilyhome.css.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.fragments.BaseFragment;

public class ToolBarManager {

    private static final ToolBarManager ourInstance = new ToolBarManager();
    private Toolbar mToolbar;

    public static ToolBarManager getInstance() {
        return ourInstance;
    }

    private ToolBarManager() {
    }

    public void setHeaderTitle(String title) {
        TextView v = mToolbar.findViewById(R.id.appTitleTextView);
        v.setText(title);
    }

    public void setSubHeaderTitle(String title) {
        TextView v = mToolbar.findViewById(R.id.subTitleTextView);
        v.setText(title);
    }

    public void onSubHeaderClickListener(final BaseFragment fragment) {
        TextView v = mToolbar.findViewById(R.id.subTitleTextView);
        v.setOnClickListener(view -> {
            if (fragment.getActivity() != null) {
                fragment.onSubHeaderClickListener();
            }
        });
    }

    public void setupToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }

    public void changeToolBarColor(int color) {
        this.mToolbar.setBackgroundColor(color);
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }

    public void onBackPressed(final Fragment fragment) {
        ImageView backImage = mToolbar.findViewById(R.id.backButtonToolbar);
        backImage.setOnClickListener(view -> {
            if (fragment.getActivity() != null) {
                fragment.getActivity().onBackPressed();
            }
        });
    }

    public void hideToolBar(MainActivity mActivity, boolean toHide) {
        if (toHide) {
            mActivity.findViewById(R.id.toolbarLayout).setVisibility(View.GONE);
        } else {
            mActivity.findViewById(R.id.toolbarLayout).setVisibility(View.VISIBLE);
        }
    }

    public void hideBackPressFromToolBar(MainActivity mActivity, boolean toHide) {
        if (toHide) {
            mActivity.findViewById(R.id.backButtonToolbar).setVisibility(View.INVISIBLE);
        } else {
            mActivity.findViewById(R.id.backButtonToolbar).setVisibility(View.VISIBLE);
        }
    }
}
