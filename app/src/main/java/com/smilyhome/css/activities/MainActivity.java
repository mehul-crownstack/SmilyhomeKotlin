package com.smilyhome.css.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.smilyhome.css.BuildConfig;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.adapters.BottomSheetAdapter;
import com.smilyhome.css.activities.fragments.BaseFragment;
import com.smilyhome.css.activities.fragments.CategoryProductFragment;
import com.smilyhome.css.activities.fragments.HomeScreenFragment;
import com.smilyhome.css.activities.fragments.LoginFragment;
import com.smilyhome.css.activities.fragments.MyCartFragment;
import com.smilyhome.css.activities.fragments.MyOrdersFragment;
import com.smilyhome.css.activities.interfaces.IBottomNavigationItemClickListener;
import com.smilyhome.css.activities.models.requests.CommonRequest;
import com.smilyhome.css.activities.models.response.CategoryItem;
import com.smilyhome.css.activities.models.response.CommonResponse;
import com.smilyhome.css.activities.retrofit.RetrofitApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.smilyhome.css.activities.Constants.SHARED_PREF_NAME;
import static com.smilyhome.css.activities.Constants.USER_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, IBottomNavigationItemClickListener, PaymentResultListener {

    private BottomNavigationView bottomNavigationView;
    private List<String> mMenuBottomNavigationList = new ArrayList<>();
    private List<String> mProfileBottomNavigationList = new ArrayList<>();
    private BottomSheetDialog mBottomSheetDialog;
    private String[] mPermissionArray = new String[]{
        Manifest.permission.RECEIVE_SMS
    };
    private static final String TAG = "MainActivity";
    private Checkout mCheckoutInstance = new Checkout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbarLayout = findViewById(R.id.toolbarLayout);
        ToolBarManager.getInstance().setupToolbar(toolbarLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (!checkPermission()) {
            requestPermission();
        }
        mCheckoutInstance.setImage(R.drawable.app_icon);
        Checkout.preload(this);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        if (Utility.isNotEmpty(prefs.getString(USER_ID, ""))) {
            launchFragment(new HomeScreenFragment(), true);
        } else {
            launchFragment(new LoginFragment(), true);
        }
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(mPermissionArray[0]) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(mPermissionArray[0])) {
                    showToast("SMS is needed for Auto Read Functionality");
                }
                requestPermissions(mPermissionArray, Constants.PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }

    public void showSnackBar(final String msg, View view) {
        runOnUiThread(() -> Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        BaseFragment current = getCurrentFragment();
        if (current.onBackPressed()) {
            // To flip between view in personalize card fragment onBackPressed
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }
    }

    public void onClick(View v) {
        BaseFragment fragment = getCurrentFragment();
        if (null != fragment) {
            fragment.onClick(v);
        }
    }

    public void navigationItemClick(int childPosition) {
        switch (childPosition) {
            case 0:
                bottomNavigationView.setSelectedItemId(R.id.nav_menu);
                break;
            case 1:
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(R.id.nav_my_orders);
                break;
            case 3:
                bottomNavigationView.setSelectedItemId(R.id.nav_cart);
                break;
            case 4:
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                break;
        }
    }

    public void showBottomNavigationView(boolean toShow) {
        bottomNavigationView.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }

    public BaseFragment getCurrentFragment() {
        FragmentManager mgr = getSupportFragmentManager();
        List<Fragment> list = mgr.getFragments();
        int count = mgr.getBackStackEntryCount();
        if (0 == count) {
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof BaseFragment) {
                        return (BaseFragment) list.get(i);
                    }
                }
            }
            return null;
        }
        FragmentManager.BackStackEntry entry = mgr.getBackStackEntryAt(count - 1);
        return (BaseFragment) mgr.findFragmentByTag(entry.getName());
    }

    public void launchFragment(final Fragment fragment, final boolean addBackStack) {
        runOnUiThread(() -> doSwitchToScreen(fragment, addBackStack));
    }

    private void doSwitchToScreen(Fragment fragment, boolean addToBackStack) {
        if (null == fragment) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        String fragmentTag = fragment.getClass().getCanonicalName();
        try {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.homeFrame, fragment, fragmentTag);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e("doSwitchToScreen ", e.getMessage(), e);
            try {
                fragmentTransaction.replace(R.id.homeFrame, fragment, fragmentTag);
                if (addToBackStack) {
                    fragmentTransaction.addToBackStack(fragmentTag);
                }
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e2) {
                Log.e("doSwitchToScreen", e.getMessage(), e);
            }
        }
    }

    public boolean isInternetConnectionAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        BaseFragment currentFragment = getCurrentFragment();
        switch (item.getItemId()) {
            case R.id.nav_menu:
                mBottomSheetDialog = null;
                mBottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_menu, findViewById(R.id.bottomSheetContainer));
                RecyclerView recyclerView = bottomSheetView.findViewById(R.id.bottomSheetRecyclerView);
                BottomSheetAdapter adapter = new BottomSheetAdapter();
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(manager);
                DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
                recyclerView.addItemDecoration(decoration);
                prepareMenuBottomNavigationList();
                adapter.setListener(this);
                adapter.setMode(Constants.BottomSheetMode.MENU);
                adapter.setBottomNavigationList(mMenuBottomNavigationList);
                recyclerView.setAdapter(adapter);
                mBottomSheetDialog.setContentView(bottomSheetView);
                mBottomSheetDialog.show();
                return false;
            case R.id.nav_home:
                if (!(currentFragment instanceof HomeScreenFragment)) {
                    launchFragment(new HomeScreenFragment(), true);
                }
                break;
            case R.id.nav_my_orders:
                if (!(currentFragment instanceof MyOrdersFragment)) {
                    launchFragment(new MyOrdersFragment(), true);
                }
                break;
            case R.id.nav_cart:
                if (!(currentFragment instanceof MyCartFragment)) {
                    launchFragment(new MyCartFragment(), true);
                }
                break;
            case R.id.nav_profile:
                mBottomSheetDialog = null;
                mBottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetViewProfile = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_menu, findViewById(R.id.bottomSheetContainer));
                RecyclerView recyclerViewProfile = bottomSheetViewProfile.findViewById(R.id.bottomSheetRecyclerView);
                BottomSheetAdapter sheetAdapter = new BottomSheetAdapter();
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerViewProfile.setLayoutManager(layoutManager);
                DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewProfile.getContext(), layoutManager.getOrientation());
                recyclerViewProfile.addItemDecoration(itemDecoration);
                prepareProfileBottomNavigationList();
                sheetAdapter.setListener(this);
                sheetAdapter.setMode(Constants.BottomSheetMode.PROFILE);
                sheetAdapter.setBottomNavigationList(mProfileBottomNavigationList);
                recyclerViewProfile.setAdapter(sheetAdapter);
                mBottomSheetDialog.setContentView(bottomSheetViewProfile);
                mBottomSheetDialog.show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    private void prepareMenuBottomNavigationList() {
        if (Utility.isNotEmpty(mMenuBottomNavigationList)) {
            mMenuBottomNavigationList.clear();
        }
        mMenuBottomNavigationList.add("Share APP");
        mMenuBottomNavigationList.add("Survey");
        mMenuBottomNavigationList.add("Shop by Category");
        mMenuBottomNavigationList.add("Programs & Features");
        mMenuBottomNavigationList.add("Terms and Conditions");
        mMenuBottomNavigationList.add("Contact Us");
    }

    private void prepareProfileBottomNavigationList() {
        if (Utility.isNotEmpty(mProfileBottomNavigationList)) {
            mProfileBottomNavigationList.clear();
        }
        mProfileBottomNavigationList.add("Update Address");
        mProfileBottomNavigationList.add("Update profile");
        mProfileBottomNavigationList.add("Rate Us");
        mProfileBottomNavigationList.add("Logout");
    }

    @Override
    public void onItemClicked(int position, String mode) {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        if (Constants.BottomSheetMode.MENU.equalsIgnoreCase(mode)) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    mBottomSheetDialog = null;
                    mBottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_menu, findViewById(R.id.bottomSheetContainer));
                    RecyclerView recyclerView = bottomSheetView.findViewById(R.id.bottomSheetRecyclerView);
                    TextView bottomSheetTitleTextView = bottomSheetView.findViewById(R.id.bottomSheetTitleTextView);
                    bottomSheetTitleTextView.setText(getString(R.string.select_category));
                    BottomSheetAdapter adapter = new BottomSheetAdapter();
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(manager);
                    DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
                    recyclerView.addItemDecoration(decoration);
                    prepareMenuBottomNavigationList();
                    adapter.setListener(this);
                    adapter.setMode(Constants.BottomSheetMode.CATEGORY);
                    List<String> categoryNameList = new ArrayList<>();
                    for (CategoryItem categoryItem : Constants.getCategoryList()) {
                        categoryNameList.add(categoryItem.getCategoryName());
                    }
                    adapter.setBottomNavigationList(categoryNameList);
                    recyclerView.setAdapter(adapter);
                    mBottomSheetDialog.setContentView(bottomSheetView);
                    mBottomSheetDialog.show();
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        } else if (Constants.BottomSheetMode.PROFILE.equalsIgnoreCase(mode)) {
            switch (position) {
                case 0:
                    break;
                case 3:
                    showLogoutMessage();
                    break;
            }
        } else {
            if (Utility.isNotEmpty(Constants.getCategoryList())) {
                launchFragment(new CategoryProductFragment(Constants.getCategoryList().get(position).getId()), true);
            }
        }
    }

    private void showLogoutMessage() {
        new AlertDialog.Builder(MainActivity.this)
            .setMessage(getString(R.string.msg_logout))
            .setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                logoutServerCall();
            })
            .show();
    }

    private void logoutServerCall() {
        BaseFragment currentFragment = getCurrentFragment();
        if (currentFragment == null) {
            return;
        }
        currentFragment.showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<CommonResponse> call = RetrofitApi.getAppServicesObject().logoutServerCall(new CommonRequest(currentFragment.getStringDataFromSharedPref(USER_ID)));
                    final Response<CommonResponse> response = call.execute();
                    runOnUiThread(() -> handleResponse(response));
                } catch (Exception e) {
                    currentFragment.stopProgress();
                    showToast(e.getMessage());
                }
            }

            private void handleResponse(Response<CommonResponse> response) {
                currentFragment.stopProgress();
                if (response.isSuccessful()) {
                    CommonResponse productResponse = response.body();
                    if (productResponse != null) {
                        if (Constants.SUCCESS.equalsIgnoreCase(productResponse.getErrorCode())) {
                            currentFragment.storeStringDataInSharedPref(USER_ID, "");
                            currentFragment.clearFragmentBackStack();
                            launchFragment(new LoginFragment(), false);
                        }
                        showToast(productResponse.getErrorMessage());
                    }
                }
            }
        }).start();
    }

    public void setCountOnCartIcon(int count) {
        bottomNavigationView.getOrCreateBadge(R.id.nav_cart).setNumber(count);
    }

    public void startPayment(String amount) {
        if (BuildConfig.DEBUG) {
            mCheckoutInstance.setKeyID("rzp_test_x9JZRqkA1akOFR");
        } else {
            mCheckoutInstance.setKeyID("rzp_live_ijj4PoFg4l6sD7");
        }
        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", "Reference No. #" + new Random(6).nextInt());
            options.put("image", R.drawable.app_icon);
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Utility.isEmpty(amount) ? "1" : (Double.parseDouble(amount)) * 100));
            /*options.put("prefill.email", prefs.getString(USER_EMAIL, ""));
            options.put("prefill.contact",mBookAppointmentRequest.getPhoneNumber());*/
            mCheckoutInstance.open(this, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String transactionId) {
        if (getCurrentFragment() != null) {
            getCurrentFragment().onTransactionResponse(transactionId);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Error " + s, Toast.LENGTH_SHORT).show();
        if (getCurrentFragment() != null) {
            getCurrentFragment().stopProgress();
        }
    }
}