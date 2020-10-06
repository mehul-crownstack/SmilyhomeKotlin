package com.smilyhome.css.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.smilyhome.css.BuildConfig;
import com.smilyhome.css.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView appVersionTextView = findViewById(R.id.appVersionTextView);
        appVersionTextView.setText("v".concat(BuildConfig.VERSION_NAME));
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }, Constants.SPLASH_TIME_INTERVAL);
    }
}