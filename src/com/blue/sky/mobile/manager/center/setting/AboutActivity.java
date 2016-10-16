package com.blue.sky.mobile.manager.center.setting;

import android.os.Bundle;
import android.webkit.WebView;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.mobile.manager.R;


public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sky_activity_about);
        setHeader("关于手机宝", true);

        WebView webView = (WebView) findViewById(R.id.web_app_view);
        webView.loadUrl("file:///android_asset/about.html");
    }
}
