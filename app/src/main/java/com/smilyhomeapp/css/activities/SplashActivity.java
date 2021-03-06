package com.smilyhomeapp.css.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.smilyhomeapp.css.BuildConfig;
import com.smilyhomeapp.css.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView appVersionTextView = findViewById(R.id.appVersionTextView);
        appVersionTextView.setText("v".concat(BuildConfig.VERSION_NAME));
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(SplashActivity.this);
        String smsHashString = appSignatureHelper.getAppSignatures().get(0);
        Log.v("SMS_HASH", "FINAL HASH ::" + smsHashString);
        Constants.setSMSHashString(smsHashString);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }, Constants.SPLASH_TIME_INTERVAL);
    }
}