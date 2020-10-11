package com.smilyhome.css.activities.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.Constants;
import com.smilyhome.css.activities.MainActivity;
import com.smilyhome.css.activities.Utility;
import com.smilyhome.css.activities.models.requests.FetchProductRequest;
import com.smilyhome.css.activities.models.response.ProductItem;
import com.smilyhome.css.activities.models.response.ProductResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.smilyhome.css.activities.Constants.SHARED_PREF_NAME;

public class BaseFragment extends Fragment implements View.OnClickListener {

    protected MainActivity mActivity;
    private ProgressDialog mProgressDialog;
    protected View mContentView;
    private static final String TAG = "BaseFragment";

    void showProgress() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    public void stopProgress() {
        try {
            mActivity.runOnUiThread(() -> {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    void updateOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void storeStringDataInSharedPref(String keyName, String value) {
        if (getActivity() != null) {
            SharedPreferences.Editor editor = mActivity.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
            editor.putString(keyName, value);
            editor.apply();
        }
    }

    public String getStringDataFromSharedPref(String keyName) {
        SharedPreferences prefs = mActivity.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(keyName, "");
    }

    void showToast(String msg) {
        mActivity.runOnUiThread(() -> mActivity.showToast(msg));
    }

    void showSnackBar(String msg) {
        mActivity.runOnUiThread(() -> mActivity.showSnackBar(msg, mContentView));
    }

    @Override
    public void onClick(View view) {
        /*
         * Just a override method to invoke the back pressed of the fragments
         * */
    }

    protected void onBackPressedToExit() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    protected boolean isInternetConnectionAvailable() {
        if (mActivity.isInternetConnectionAvailable()) {
            return true;
        }
        new AlertDialog.Builder(mActivity)
            .setTitle(R.string.error)
            .setMessage("Please turn on Internet connection")
            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
        return false;
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    void fetchProductsServerCall(int mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FetchProductRequest request = new FetchProductRequest(mode);
                    Call<ProductResponse> call = RetrofitApi.getAppServicesObjectForProducts().fetchProductsServerCall(request);
                    final Response<ProductResponse> response = call.execute();
                    updateOnUiThread(() -> handleResponse(response, mode));
                } catch (Exception e) {
                    stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<ProductResponse> response, int mode) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        if (Constants.SUCCESS.equalsIgnoreCase(productResponse.getErrorCode())) {
                            onProductUpdated(productResponse.getProductList(), mode);
                        } else {
                            showToast(productResponse.getErrorMessage());
                        }
                    }
                } else {
                    stopProgress();
                }
            }
        }).start();
    }

    protected void onProductUpdated(List<ProductItem> productItemList, int mode) {

    }

    public void onBackPressed() {
    }

    protected void showImageDialog(String imageStr) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.image_dialog);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        if (Utility.isEmpty(imageStr)) {
            return;
        } else {
            Picasso.get().load(imageStr).into(imageView);
        }
        dialog.show();
    }

    public void onSubHeaderClickListener() {
    }

    void launchFragment(Fragment fragment, boolean addBackStack) {
        mActivity.launchFragment(fragment, addBackStack);
    }

    void clearFragmentBackStack() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
