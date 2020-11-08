package com.smilyhomeapp.css.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.smilyhomeapp.css.R;
import com.smilyhomeapp.css.activities.ToolBarManager;
import com.smilyhomeapp.css.activities.Utility;
import com.smilyhomeapp.css.activities.models.response.CartItem;
import com.smilyhomeapp.css.activities.models.response.TransactionResponse;
import com.smilyhomeapp.css.activities.models.response.UserAddressResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TxnSuccessFragment extends BaseFragment {

    private TransactionResponse transactionResponse;
    private boolean mIsDoubleBackPressClicked = false;

    public TxnSuccessFragment(TransactionResponse transactionResponse) {
        this.transactionResponse = transactionResponse;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_txn_success, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        if (transactionResponse != null) {
            TextView cartMessageTextView = mContentView.findViewById(R.id.cartMessageTextView);
            TextView nameAddressTextView = mContentView.findViewById(R.id.nameAddressTextView);
            TextView mobileAddressTextView = mContentView.findViewById(R.id.mobileAddressTextView);
            TextView addressTextView = mContentView.findViewById(R.id.addressTextView);
            TextView paymentMethodTextView = mContentView.findViewById(R.id.paymentMethodTextView);
            TextView orderStatusTextView = mContentView.findViewById(R.id.orderStatusTextView);
            TextView orderIdTextView = mContentView.findViewById(R.id.orderIdTextView);
            TextView totalOrderValueTextView = mContentView.findViewById(R.id.totalOrderValueTextView);
            TextView totalAmountToPayTextView = mContentView.findViewById(R.id.totalAmountToPayTextView);
            Utility.writeHtmlCode(transactionResponse.getDisplayMessage(), cartMessageTextView);
            nameAddressTextView.setText(transactionResponse.getUserAddress().getUserName());
            mobileAddressTextView.setText(transactionResponse.getUserAddress().getUserPhone());
            orderIdTextView.setText(transactionResponse.getOrderNumber());
            totalOrderValueTextView.setText(String.format("%s%s", getString(R.string.currency), transactionResponse.getTotalPrice()));
            totalAmountToPayTextView.setText(String.format("%s%s", getString(R.string.currency), transactionResponse.getTotalPaidPrice()));
            UserAddressResponse userAddressResponse = transactionResponse.getUserAddress();
            addressTextView.setText(String.format("%s,%s\n\n%s, %s", userAddressResponse.getFullAddress(),
                                                  userAddressResponse.getAddZipcode(),
                                                  userAddressResponse.getCityName(),
                                                  userAddressResponse.getStateName()));
            paymentMethodTextView.setText(transactionResponse.getModeOfPayment());
            orderStatusTextView.setText(transactionResponse.getStatus());
            RecyclerView cartRecyclerView = mContentView.findViewById(R.id.cartRecyclerView);
            MyCartAdapter adapter = new MyCartAdapter(transactionResponse.getCartItemList());
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            cartRecyclerView.setAdapter(adapter);
        }
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
    public void onClick(View view) {
        if (view.getId() == R.id.continueShoppingTextView) {
            launchFragment(new HomeScreenFragment(), false);
        }
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        //ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

        private List<CartItem> productItemList;

        public MyCartAdapter(List<CartItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public MyCartAdapter.MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.my_cart_item, parent, false);
            return new MyCartAdapter.MyCartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCartAdapter.MyCartViewHolder holder, int position) {
            CartItem item = productItemList.get(position);
            String quantityCount = item.getProductQuantity();
            holder.quantityTextView.setText(String.format("%s %s", quantityCount, quantityCount.equalsIgnoreCase("1") ? "item" : "items"));
            holder.productNameTextView.setText(item.getProductName());
            holder.productPriceTextView.setText(getString(R.string.currency).concat(item.getProductPrice()));
            holder.productDiscountPriceTextView.setText(getString(R.string.currency).concat(item.getProductSalePrice()));
            double salePrice = Double.parseDouble(Utility.isNotEmpty(item.getProductSalePrice()) ? item.getProductSalePrice() : "0");
            double quantity = Double.parseDouble(Utility.isNotEmpty(quantityCount) ? quantityCount : "0");
            holder.yourPayTextView.setText(String.format("%s %s", getString(R.string.currency), salePrice * quantity));
            holder.productDiscountTextView.setText(item.getProductDiscount().concat("% off"));
            Utility.writeStrikeOffText(holder.productPriceTextView);
            Picasso.get().load(item.getImage()).placeholder(R.drawable.default_image).into(holder.productImageView);
        }

        @Override
        public int getItemCount() {
            return productItemList.size();
        }

        private class MyCartViewHolder extends RecyclerView.ViewHolder {

            private ImageView productImageView;
            private TextView quantityTextView;
            private TextView productNameTextView;
            private TextView productPriceTextView;
            private TextView productDiscountPriceTextView;
            private TextView productDiscountTextView;
            private TextView yourPayTextView;

            public MyCartViewHolder(@NonNull View itemView) {
                super(itemView);
                quantityTextView = itemView.findViewById(R.id.quantityTextView);
                yourPayTextView = itemView.findViewById(R.id.yourPayTextView);
                productDiscountTextView = itemView.findViewById(R.id.productDiscountTextView);
                productImageView = itemView.findViewById(R.id.productImageView);
                productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
                productDiscountPriceTextView = itemView.findViewById(R.id.productDiscountPriceTextView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
                itemView.findViewById(R.id.productDeleteTextView).setVisibility(View.INVISIBLE);
            }
        }
    }
}
