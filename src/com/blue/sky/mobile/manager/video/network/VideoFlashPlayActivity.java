package com.blue.sky.mobile.manager.video.network;

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
import com.blue.sky.common.utils.Constants;
import com.blue.sky.common.utils.NetWorkHelper;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.control.webview.CrossDomainWebView;
import com.blue.sky.mobile.manager.R;

/**
 * Created by Administrator on 2014/7/27.
 */
public class VideoFlashPlayActivity extends BaseActivity {

    private CrossDomainWebView webView;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_flash_play);

        String videoUrl = getIntent().getStringExtra("path");

        progressbar =  (ProgressBar)findViewById(R.id.webview_progress_bar);
        webView = (CrossDomainWebView) findViewById(R.id.web_app_view);
        webView.openSetting(this);
        webView.setWebChromeClient(new  WebChromeClient());
        String htmlContent= "<embed src='"+videoUrl+"' type='application/x-shockwave-flash' allowFullScreen='true' width='100%' height='100%' allowNetworking='all' wmode='opaque' allowScriptAccess='always'></embed>";
        webView.loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.clearHistory();
        webView.destroy();
    }
}
