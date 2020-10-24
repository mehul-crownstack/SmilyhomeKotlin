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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.models.requests.CancelProductRequest;
import com.smilyhome.css.activities.models.requests.FetchOrderDetailRequest;
import com.smilyhome.css.activities.models.response.MyOrderCartItem;
import com.smilyhome.css.activities.models.response.MyOrderDetailResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MyOrderDetailFragment extends BaseFragment {

    private String mOrderId;
    private TextView orderPlaceTextView;
    private TextView paymentModeTextView;
    private TextView bagIdTextView;
    private TextView orderStatusTextView;
    private TextView totalAmountTextView;
    private TextView orderValueTextView;
    private TextView discountTextView;
    private TextView deliveryFeesTextView;
    private TextView totalAmountTextView2;
    private TextView nameAddressTextView;
    private TextView mobileAddressTextView;
    private TextView addressTextView;
    private MyCartAdapter mCartAdapter;

    public MyOrderDetailFragment(String orderId) {
        mOrderId = orderId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_my_order_detail, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        mCartAdapter = new MyCartAdapter();
        orderPlaceTextView = mContentView.findViewById(R.id.orderPlaceTextView);
        paymentModeTextView = mContentView.findViewById(R.id.paymentModeTextView);
        bagIdTextView = mContentView.findViewById(R.id.bagIdTextView);
        orderStatusTextView = mContentView.findViewById(R.id.orderStatusTextView);
        totalAmountTextView = mContentView.findViewById(R.id.totalAmountTextView);
        orderValueTextView = mContentView.findViewById(R.id.orderValueTextView);
        discountTextView = mContentView.findViewById(R.id.discountTextView);
        deliveryFeesTextView = mContentView.findViewById(R.id.deliveryFeesTextView);
        totalAmountTextView2 = mContentView.findViewById(R.id.totalAmountTextView2);
        nameAddressTextView = mContentView.findViewById(R.id.nameAddressTextView);
        mobileAddressTextView = mContentView.findViewById(R.id.mobileAddressTextView);
        addressTextView = mContentView.findViewById(R.id.addressTextView);
        RecyclerView cartRecyclerView = mContentView.findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        cartRecyclerView.setAdapter(mCartAdapter);
        fetchMyOrderServerCall();
    }

    private void fetchMyOrderServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<MyOrderDetailResponse> call = RetrofitApi.getAppServicesObjectForData().fetchOrderDetailServerCall(new FetchOrderDetailRequest(mOrderId));
                    final Response<MyOrderDetailResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<MyOrderDetailResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    MyOrderDetailResponse detailResponse = response.body();
                    if (detailResponse != null && detailResponse.getErrorCode().equalsIgnoreCase(Constants.SUCCESS)) {
                        setupUIFromResponse(detailResponse);
                    }
                }
            }
        }).start();
    }

    private void cancelProductServerCall(String orderId, String cartItemId) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CancelProductRequest request = new CancelProductRequest();
                    request.setOrderId(orderId);
                    request.setItemCartId(cartItemId);
                    Call<MyOrderDetailResponse> call = RetrofitApi.getAppServicesObjectForData().cancelProductServerCall(request);
                    final Response<MyOrderDetailResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<MyOrderDetailResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    MyOrderDetailResponse detailResponse = response.body();
                    if (detailResponse != null && detailResponse.getErrorCode().equalsIgnoreCase(Constants.SUCCESS)) {
                        setupUIFromResponse(detailResponse);
                    }
                }
            }
        }).start();
    }

    private void setupUIFromResponse(MyOrderDetailResponse detailResponse) {
        orderPlaceTextView.setText(detailResponse.getOrderPlaced());
        paymentModeTextView.setText(detailResponse.getPaymentMode());
        bagIdTextView.setText(detailResponse.getOrderBagId());
        orderStatusTextView.setText(detailResponse.getOrderStatus());
        discountTextView.setText(detailResponse.getOrderDiscount());
        nameAddressTextView.setText(detailResponse.getAddress().getUserName());
        mobileAddressTextView.setText(detailResponse.getAddress().getUserPhone());
        addressTextView.setText(detailResponse.getAddress().getFullAddress());
        totalAmountTextView.setText(String.format("%s %s", getString(R.string.currency), detailResponse.getTotalAmount()));
        orderValueTextView.setText(String.format("%s %s", getString(R.string.currency), detailResponse.getOrderValue()));
        deliveryFeesTextView.setText(String.format("%s %s", getString(R.string.currency), detailResponse.getOrderDeliveryFee()));
        totalAmountTextView2.setText(String.format("%s %s", getString(R.string.currency), detailResponse.getOrderTotalAmount()));
        List<MyOrderCartItem> orderItemsList = detailResponse.getOrderItemsList();
        mCartAdapter.setProductItemList(orderItemsList);
        mCartAdapter.notifyDataSetChanged();
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.order_detail));
        ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

        private List<MyOrderCartItem> productItemList = new ArrayList<>();

        public void setProductItemList(List<MyOrderCartItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public MyCartAdapter.MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.my_order_detail_cart_item, parent, false);
            return new MyCartAdapter.MyCartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCartAdapter.MyCartViewHolder holder, int position) {
            MyOrderCartItem item = productItemList.get(position);
            Picasso.get().load(item.getItemImage()).placeholder(R.drawable.default_image).into(holder.productImageView);
            holder.productNameTextView.setText(item.getItemName());
            String quantityCount = item.getItemQuantity();
            holder.quantityTextView.setText(String.format("%s %s", quantityCount, quantityCount.equalsIgnoreCase("1") ? "item" : "items"));
            holder.orderNumberTextView.setText(item.getItemOrderId());
            holder.orderStatusTextView.setText(item.getItemStatus());
            if (item.getItemStatus().equalsIgnoreCase("Active")) {
                holder.orderStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.theme_green_dark));
                holder.productDeleteTextView.setVisibility(View.VISIBLE);
            } else if (item.getItemStatus().equalsIgnoreCase("Cancelled")) {
                holder.orderStatusTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.red_background));
                holder.productDeleteTextView.setVisibility(View.INVISIBLE);
            } else {
                holder.orderStatusTextView.setVisibility(View.INVISIBLE);
                holder.productDeleteTextView.setVisibility(View.INVISIBLE);
            }
            holder.yourPayTextView.setText(String.format("%s %s", getString(R.string.currency), item.getItemSalePrice()));
        }

        @Override
        public int getItemCount() {
            return productItemList.size();
        }

        private class MyCartViewHolder extends RecyclerView.ViewHolder {

            private ImageView productImageView;
            private TextView quantityTextView;
            private TextView productNameTextView;
            private TextView orderNumberTextView;
            private TextView orderStatusTextView;
            private TextView productDeleteTextView;
            private TextView yourPayTextView;

            public MyCartViewHolder(@NonNull View itemView) {
                super(itemView);
                quantityTextView = itemView.findViewById(R.id.quantityTextView);
                yourPayTextView = itemView.findViewById(R.id.yourPayTextView);
                productDeleteTextView = itemView.findViewById(R.id.productDeleteTextView);
                productImageView = itemView.findViewById(R.id.productImageView);
                orderNumberTextView = itemView.findViewById(R.id.orderNumberTextView);
                orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
                TextView productDeleteTextView = itemView.findViewById(R.id.productDeleteTextView);
                productDeleteTextView.setOnClickListener(view -> showCancelProductDialog());
            }

            private void showCancelProductDialog() {
                new AlertDialog.Builder(mActivity)
                    .setMessage(getString(R.string.cancel_item_message))
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        dialog.dismiss();
                        cancelProductServerCall(productItemList.get(getAdapterPosition()).getItemOrderId(),
                                                productItemList.get(getAdapterPosition()).getItemCartId());
                    }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
            }
        }
    }
}
