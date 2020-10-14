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
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import com.smilyhome.css.R;
import com.smilyhome.css.activities.fragments.BaseFragment;
import com.smilyhome.css.activities.fragments.HomeScreenFragment;
import com.smilyhome.css.activities.fragments.LoginFragment;

import java.util.List;

import static com.smilyhome.css.activities.Constants.SHARED_PREF_NAME;
import static com.smilyhome.css.activities.Constants.USER_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private String[] mPermissionArray = new String[]{
        Manifest.permission.RECEIVE_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbarLayout = findViewById(R.id.toolbarLayout);
        ToolBarManager.getInstance().setupToolbar(toolbarLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (!checkPermission()) {
            requestPermission();
        }
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
        if (getCurrentFragment() != null) {
            getCurrentFragment().onBackPressed();
        } else {
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
                bottomNavigationView.setSelectedItemId(R.id.nav_wallet);
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
        BaseFragment current = getCurrentFragment();
        if (null != current) {
            manager.popBackStackImmediate();
        }
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
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_menu, findViewById(R.id.bottomSheetContainer));
                RecyclerView recyclerView = bottomSheetView.findViewById(R.id.bottomSheetRecyclerView);
                BottomSheetAdapter adapter = new BottomSheetAdapter();
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(manager);
                DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
                recyclerView.addItemDecoration(decoration);
                recyclerView.setAdapter(adapter);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                return false;
            case R.id.nav_home:
                if (!(currentFragment instanceof HomeScreenFragment)) {
                    showToast(getString(R.string.home));
                }
                break;
            case R.id.nav_wallet:
                showToast(getString(R.string.wallet));
                break;
            case R.id.nav_cart:
                showToast(getString(R.string.cart));
                break;
            case R.id.nav_profile:
                showToast(getString(R.string.profile));
                break;
        }
        return true;
    }
}