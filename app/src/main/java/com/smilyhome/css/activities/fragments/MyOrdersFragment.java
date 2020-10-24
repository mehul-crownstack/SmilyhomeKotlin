package com.smilyhome.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.FetchAddressRequest;
import com.smilyhome.css.activities.models.response.MyOrderItem;
import com.smilyhome.css.activities.models.response.MyOrderResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends BaseFragment {

    private MyOrderAdapter pendingOrderAdapter;
    private MyOrderAdapter deliveredOrderAdapter;
    private MyOrderAdapter cancelledOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_my_order, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void setupUI() {
        RecyclerView pendingOrderRecyclerView = mContentView.findViewById(R.id.pendingOrderRecyclerView);
        pendingOrderAdapter = new MyOrderAdapter();
        pendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        pendingOrderRecyclerView.setAdapter(pendingOrderAdapter);
        RecyclerView deliveredOrderRecyclerView = mContentView.findViewById(R.id.deliveredOrderRecyclerView);
        deliveredOrderAdapter = new MyOrderAdapter();
        deliveredOrderRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        deliveredOrderRecyclerView.setAdapter(deliveredOrderAdapter);
        RecyclerView cancelledOrderRecyclerView = mContentView.findViewById(R.id.cancelledOrderRecyclerView);
        cancelledOrderAdapter = new MyOrderAdapter();
        cancelledOrderRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        cancelledOrderRecyclerView.setAdapter(cancelledOrderAdapter);
        if (isInternetConnectionAvailable()) {
            fetchMyOrderServerCall();
        }
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.my_orders));
        //ToolBarManager.getInstance().setSubHeaderTitle(getString(R.string.zip_code));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    private void fetchMyOrderServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FetchAddressRequest request = new FetchAddressRequest(getStringDataFromSharedPref(Constants.USER_ID));
                    Call<MyOrderResponse> call = RetrofitApi.getAppServicesObjectForData().fetchMyOrderServerCall(request);
                    final Response<MyOrderResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<MyOrderResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    MyOrderResponse orderResponse = response.body();
                    if (orderResponse != null) {
                        List<MyOrderItem> pendingOrdersList = orderResponse.getPendingOrdersList();
                        if (Utility.isNotEmpty(pendingOrdersList)) {
                            pendingOrderAdapter.setProductItemList(pendingOrdersList);
                            pendingOrderAdapter.notifyDataSetChanged();
                        }
                        List<MyOrderItem> deliveredOrdersList = orderResponse.getDeliveredOrdersList();
                        if (Utility.isNotEmpty(deliveredOrdersList)) {
                            deliveredOrderAdapter.setProductItemList(deliveredOrdersList);
                            deliveredOrderAdapter.notifyDataSetChanged();
                        }
                        List<MyOrderItem> cancelledOrdersList = orderResponse.getCancelledOrdersList();
                        if (Utility.isNotEmpty(cancelledOrdersList)) {
                            cancelledOrderAdapter.setProductItemList(cancelledOrdersList);
                            cancelledOrderAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyCartViewHolder> {

        private List<MyOrderItem> productItemList = new ArrayList<>();

        public void setProductItemList(List<MyOrderItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public MyOrderAdapter.MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.my_order_item, parent, false);
            return new MyOrderAdapter.MyCartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyOrderAdapter.MyCartViewHolder holder, int position) {
            MyOrderItem item = productItemList.get(position);
            holder.orderPlaceTextView.setText(item.getOrderPlacedOn());
            holder.totalItemTextView.setText(item.getTotalItems());
            holder.deliveredItemTextView.setText(item.getDeliveredItems());
            holder.bagIdTextView.setText(item.getBagId());
            holder.orderStatusTextView.setText(item.getOrderStatus());
            holder.itemCountTextView.setText(item.getTotalItems());
            holder.totalAmountTextView.setText(getString(R.string.currency).concat(item.getTotalAmount()));
        }

        @Override
        public int getItemCount() {
            return productItemList.size();
        }

        private class MyCartViewHolder extends RecyclerView.ViewHolder {

            private TextView orderPlaceTextView;
            private TextView totalItemTextView;
            private TextView deliveredItemTextView;
            private TextView bagIdTextView;
            private TextView orderStatusTextView;
            private TextView totalAmountTextView;
            private TextView itemCountTextView;

            public MyCartViewHolder(@NonNull View itemView) {
                super(itemView);
                orderPlaceTextView = itemView.findViewById(R.id.orderPlaceTextView);
                totalItemTextView = itemView.findViewById(R.id.totalItemTextView);
                deliveredItemTextView = itemView.findViewById(R.id.deliveredItemTextView);
                bagIdTextView = itemView.findViewById(R.id.bagIdTextView);
                orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
                totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
                itemCountTextView = itemView.findViewById(R.id.itemCountTextView);
                View orderContainer = itemView.findViewById(R.id.orderContainer);
                View viewDetailsTextView = itemView.findViewById(R.id.viewDetailsTextView);
                orderContainer.setOnClickListener(view -> {
                    String orderId = productItemList.get(getAdapterPosition()).getOrderId();
                    launchFragment(new MyOrderDetailFragment(orderId), true);
                });
                viewDetailsTextView.setOnClickListener(view -> {
                    String orderId = productItemList.get(getAdapterPosition()).getOrderId();
                    launchFragment(new MyOrderDetailFragment(orderId), true);
                });
            }
        }
    }
}
