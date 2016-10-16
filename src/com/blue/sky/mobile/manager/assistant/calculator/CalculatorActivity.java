package com.blue.sky.mobile.manager.assistant.calculator;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.mobile.manager.R;


public class CalculatorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sky_activity_about);
        setHeader("房贷计算器", true);

        WebView webView = (WebView) findViewById(R.id.web_app_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.loadUrl("file:///android_asset/calculator/calculator.html");
    }
}
