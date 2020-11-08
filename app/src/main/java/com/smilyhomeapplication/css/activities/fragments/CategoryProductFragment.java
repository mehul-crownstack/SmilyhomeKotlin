package com.smilyhomeapplication.css.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhomeapplication.css.R;
import com.smilyhomeapplication.css.activities.Constants;
import com.smilyhomeapplication.css.activities.ToolBarManager;
import com.smilyhomeapplication.css.activities.Utility;
import com.smilyhomeapplication.css.activities.models.requests.AddToCartRequest;
import com.smilyhomeapplication.css.activities.models.requests.CategoryProductRequest;
import com.smilyhomeapplication.css.activities.models.response.MyCartResponse;
import com.smilyhomeapplication.css.activities.models.response.ProductItem;
import com.smilyhomeapplication.css.activities.models.response.ProductResponse;
import com.smilyhomeapplication.css.activities.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class CategoryProductFragment extends BaseFragment {

    private String mCategoryId = "";
    private CategoryProductAdapter mProductAdapter;

    public CategoryProductFragment(String categoryId) {
        mCategoryId = categoryId;
    }

    @Override
    public void onStart() {
        super.onStart();
        showBottomNavigationView(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_category_product, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    private void fetchCategoryProductsServerCall() {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ProductResponse> call = RetrofitApi.getAppServicesObjectForProducts().fetchCategoryProductsServerCall(new CategoryProductRequest(mCategoryId));
                    final Response<ProductResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<ProductResponse> response) {
                stopProgress();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        showToast(productResponse.getErrorMessage());
                        List<ProductItem> list = productResponse.getProductList();
                        if (Utility.isNotEmpty(list)) {
                            mProductAdapter.setProductItemList(list);
                            mProductAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).start();
    }

    private void setupUI() {
        fetchCategoryProductsServerCall();
        RecyclerView hotDealRecyclerView = mContentView.findViewById(R.id.recyclerView);
        mProductAdapter = new CategoryProductAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        hotDealRecyclerView.setLayoutManager(linearLayoutManager);
        hotDealRecyclerView.setAdapter(mProductAdapter);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }

    @Override
    protected void onUpdatedAddToCartResponse(MyCartResponse response) {
        showToast(response.getErrorMessage());
        launchFragment(new MyCartFragment(), true);
    }

    private class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder> {

        private List<ProductItem> productItemList = new ArrayList<>();

        public void setProductItemList(List<ProductItem> productItemList) {
            this.productItemList = productItemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.hot_deal_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductItem item = productItemList.get(position);
            holder.productNameTextView.setText(item.getProductName());
            Utility.writeHtmlCode(item.getProductShortDesc(), holder.productDescriptionTextView);
            holder.productPriceTextView.setText(getString(R.string.currency).concat(item.getProductPrice()));
            holder.productDiscountTextView.setText(item.getProductDiscount().concat("% off"));
            holder.productDiscountPriceTextView.setText(getString(R.string.currency).concat(item.getProductSalePrice()));
            Utility.writeStrikeOffText(holder.productPriceTextView);
            Picasso.get().load(item.getImage()).placeholder(R.drawable.default_image).into(holder.productImageView);
        }

        @Override
        public int getItemCount() {
            return productItemList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView productDescriptionTextView;
            private TextView productNameTextView;
            private TextView productPriceTextView;
            private TextView productDiscountPriceTextView;
            private TextView productDiscountTextView;
            private ImageView productImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                productDiscountTextView = itemView.findViewById(R.id.productDiscountTextView);
                productImageView = itemView.findViewById(R.id.productImageView);
                productDescriptionTextView = itemView.findViewById(R.id.productDescriptionTextView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
                productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
                productDiscountPriceTextView = itemView.findViewById(R.id.productDiscountPriceTextView);
                TextView buyTextView = itemView.findViewById(R.id.buyTextView);
                productNameTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productDescriptionTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productPriceTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                productDiscountPriceTextView.setOnClickListener(view -> launchProductDetailFragment(productItemList.get(getAdapterPosition()).getId()));
                buyTextView.setOnClickListener(view -> {
                    AddToCartRequest request = new AddToCartRequest();
                    request.setProductId(productItemList.get(getAdapterPosition()).getId());
                    request.setUserId(getStringDataFromSharedPref(Constants.USER_ID));
                    request.setProductQuantity("1");
                    request.setProductImage(productItemList.get(getAdapterPosition()).getImage());
                    request.setProductName(productItemList.get(getAdapterPosition()).getProductName());
                    addToCartServerCall(request);
                });
            }
        }
    }
}
