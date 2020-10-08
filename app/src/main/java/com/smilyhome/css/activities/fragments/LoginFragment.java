package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;

public class LoginFragment extends BaseFragment {

    private boolean mIsDoubleBackPressClicked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_login, container, false);
        setupToolbarUI();
        return mContentView;
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, true);
    }

    @Override
    public void onBackPressed() {
        if (mIsDoubleBackPressClicked) {
            super.onBackPressedToExit();
            return;
        }
        showSnackBar(getString(R.string.back_press_msg));
        mIsDoubleBackPressClicked = true;
        new Handler().postDelayed(() -> mIsDoubleBackPressClicked = false, 1500);
    }
}
