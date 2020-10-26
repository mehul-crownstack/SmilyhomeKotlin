package com.smilyhome.css.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.ToolBarManager;
import com.smilyhome.css.activities.WebViewClientImpl;

public class WebViewFragment extends BaseFragment {

    private String mUrl;

    public WebViewFragment(String url) {
        mUrl = url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_web_view, container, false);
        setupToolbarUI();
        setupUI();
        return mContentView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupUI() {
        WebView webView = mContentView.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebViewClientImpl webViewClient = new WebViewClientImpl(mActivity, mUrl);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(mUrl);
    }

    private void setupToolbarUI() {
        ToolBarManager.getInstance().hideToolBar(mActivity, false);
        ToolBarManager.getInstance().hideBackPressFromToolBar(mActivity, true);
        ToolBarManager.getInstance().showAppIconInToolbar(mActivity, true);
        ToolBarManager.getInstance().setHeaderTitle(getString(R.string.app_name));
        ToolBarManager.getInstance().onSubHeaderClickListener(this);
    }
}
