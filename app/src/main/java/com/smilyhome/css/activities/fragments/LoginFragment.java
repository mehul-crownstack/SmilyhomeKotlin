package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;

public class LoginFragment extends BaseFragment {

    private boolean mIsDoubleBackPressClicked = false;
    private EditText mobileNumberInputEditText;
    private EditText nameInputEditText;
    private static final String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_login, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        mobileNumberInputEditText = mContentView.findViewById(R.id.mobileNumberInputEditText);
        nameInputEditText = mContentView.findViewById(R.id.nameInputEditText);
        mobileNumberInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (Utility.isNotEmpty(str) && str.length() == getResources().getInteger(R.integer.mobile_length)) {
                    mobileNumberInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.tick_small, 0);
                    nameInputEditText.requestFocus();
                } else {
                    mobileNumberInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.generateOtpTextView) {
            String mobileStr = mobileNumberInputEditText.getText().toString();
            String nameStr = nameInputEditText.getText().toString();
            if (Utility.isEmpty(mobileStr)) {
                mobileNumberInputEditText.setError(getString(R.string.msg_mandatory));
                mobileNumberInputEditText.requestFocus();
                return;
            }
            if (Utility.isEmpty(nameStr)) {
                nameInputEditText.setError(getString(R.string.msg_mandatory));
                nameInputEditText.requestFocus();
                return;
            }
            if (isInternetConnectionAvailable()) {
                new Handler(Looper.getMainLooper()).postDelayed(this::stopProgress, 1500);
                showProgress();
            }
        }
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
        new Handler(Looper.getMainLooper()).postDelayed(() -> mIsDoubleBackPressClicked = false, 1500);
    }
}

