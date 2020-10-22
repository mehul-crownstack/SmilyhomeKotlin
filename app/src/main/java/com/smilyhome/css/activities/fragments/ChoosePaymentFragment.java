package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;

public class ChoosePaymentFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private String totalAmount;
    private CheckBox codCheckBox;
    private CheckBox onlineCheckBox;
    private boolean mIsCodSelected;

    public ChoosePaymentFragment(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_choose_payment, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        TextView orderSummaryHeading = mContentView.findViewById(R.id.orderSummaryHeading);
        onlineCheckBox = mContentView.findViewById(R.id.onlineCheckBox);
        codCheckBox = mContentView.findViewById(R.id.codCheckBox);
        codCheckBox.setOnCheckedChangeListener(this);
        onlineCheckBox.setOnCheckedChangeListener(this);
        orderSummaryHeading.setText(String.format("Total amount to pay %s%s", getString(R.string.currency), totalAmount));
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.choose_payment_option));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.getId() == codCheckBox.getId() && isChecked) {
            mIsCodSelected = true;
            codCheckBox.setChecked(true);
            onlineCheckBox.setChecked(false);
        } else if (compoundButton.getId() == onlineCheckBox.getId() && isChecked) {
            mIsCodSelected = false;
            codCheckBox.setChecked(false);
            onlineCheckBox.setChecked(true);
        }
    }
}
