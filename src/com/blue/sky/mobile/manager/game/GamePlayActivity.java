package com.blue.sky.mobile.manager.game;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.control.webview.CrossDomainWebView;
import com.blue.sky.mobile.manager.R;


public class GamePlayActivity extends BaseActivity {

    private CrossDomainWebView webView;
    private ProgressBar progressbar;

    private GameInfo item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sky_activity_game_play);

        item = (GameInfo) getIntent().getSerializableExtra("item");

        setHeader(item.getShortName(), true);

        progressbar =  (ProgressBar)findViewById(R.id.webview_progress_bar);
        webView = (CrossDomainWebView) findViewById(R.id.web_app_view);
        webView.openSetting(this);
        webView.setWebChromeClient(new  WebChromeClient());
        webView.loadUrl(item.getGameUrl());
    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        webView.removeAllViews();
//        webView.clearHistory();
//        webView.destroy();
//    }
}
