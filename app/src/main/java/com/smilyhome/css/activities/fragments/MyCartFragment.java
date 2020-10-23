package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.DeleteProductRequest;
import com.smilyhome.css.activities.models.response.CartItem;
import com.smilyhome.css.activities.models.response.MyCartResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends BaseFragment {

    private MyCartAdapter mCartAdapter;
    private TextView cartMessageTextView;
    private TextView mrpTextView;
    private TextView productDiscountTextView;
    private TextView earningDiscountTextView;
    private TextView deliveryChargesTextView;
    private TextView totalAmountToPayTextView;
    private MyCartResponse mCartResponse;
    private View myCartDataContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_my_cart, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        myCartDataContainer = mContentView.findViewById(R.id.myCartDataContainer);
        mrpTextView = mContentView.findViewById(R.id.mrpTextView);
        productDiscountTextView = mContentView.findViewById(R.id.productDiscountTextView);
        earningDiscountTextView = mContentView.findViewById(R.id.earningDiscountTextView);
        deliveryChargesTextView = mContentView.findViewById(R.id.deliveryChargesTextView);
        totalAmountToPayTextView = mContentView.findViewById(R.id.totalAmountToPayTextView);
        RecyclerView cartRecyclerView = mContentView.findViewById(R.id.cartRecyclerView);
        cartMessageTextView = mContentView.findViewById(R.id.cartMessageTextView);
        mCartAdapter = new MyCartAdapter();
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        cartRecyclerView.setAdapter(mCartAdapter);
        showProgress();
        fetchMyCartServerCall();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShoppingTextView2:
            case R.id.continueShoppingTextView:
                launchFragment(new HomeScreenFragment(), false);
                break;
            case R.id.addToBagTextView:
                launchFragment(new OrderConfirmationFragment(mCartResponse), true);
                break;
        }
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.my_cart));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    @Override
    protected void onMyCartResponse(MyCartResponse myCartResponse) {
        showToast(myCartResponse.getErrorMessage());
        if (Constants.SUCCESS.equalsIgnoreCase(myCartResponse.getErrorCode())) {
            mCartResponse = myCartResponse;
            Utility.writeHtmlCode(myCartResponse.getDisplayMessage(), cartMessageTextView);
            mrpTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getTotalMrp()));
            productDiscountTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getProductDiscount()));
            earningDiscountTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getEarningDiscount()));
            deliveryChargesTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getDeliveryCharges()));
            totalAmountToPayTextView.setText(String.format("%s %s", getString(R.string.currency), myCartResponse.getTotalPaybleAmount()));
            List<CartItem> list = myCartResponse.getCartItemList();
            mCartAdapter.setProductItemList(list);
            mCartAdapter.notifyDataSetChanged();
        } else {
            myCartDataContainer.setVisibility(View.GONE);
            mContentView.findViewById(R.id.noItemFoundImageView).setVisibility(View.VISIBLE);
            mContentView.findViewById(R.id.continueShoppingTextView2).setVisibility(View.VISIBLE);
        }
    }

    private void deleteItemFromCartServerCall(String cartId) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DeleteProductRequest request = new DeleteProductRequest();
                    request.setUserId(getStringDataFromSharedPref(Constants.USER_ID));
                    request.setCartId(cartId);
                    Call<MyCartResponse> call = RetrofitApi.getAppServicesObjectForData().deleteItemFromCartServerCall(request);
                    final Response<MyCartResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<MyCartResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    MyCartResponse myCartResponse = response.body();
                    if (myCartResponse != null) {
                        mActivity.setCountOnCartIcon(myCartResponse.getCartItemList().size());
                        onMyCartResponse(myCartResponse);
                    }
                }
            }
        }).start();
    }

    private class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

        private List<CartItem> productItemList = new ArrayList<>();

        public void setProductItemList(List<CartItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.my_cart_item, parent, false);
            return new MyCartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
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
                TextView productDeleteTextView = itemView.findViewById(R.id.productDeleteTextView);
                productDeleteTextView.setOnClickListener(view -> showRemoveProductConfirmationDialog());
                productNameTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getProductId()));
                yourPayTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getProductId()));
                productDiscountTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getProductId()));
                productImageView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getProductId()));
            }

            private void showRemoveProductConfirmationDialog() {
                new AlertDialog.Builder(mActivity)
                    .setMessage(getString(R.string.remove_product_msg))
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        dialog.dismiss();
                        deleteItemFromCartServerCall(productItemList.get(getAdapterPosition()).getCartId());
                    }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
            }
        }
    }
}
