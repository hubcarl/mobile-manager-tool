package com.blue.sky.mobile.manager.assistant.metro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.polites.android.GestureImageView;

/**
 * Created by Administrator on 2014/11/16.
 */
public class MetroMapViewActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_metro_activity_map_view);

        String cityName =  getIntent().getStringExtra("name");
        final String imageUrl = getIntent().getStringExtra("imageUrl");

        Log.d(">>>imageUrl", imageUrl);
        UIHelp.setHeaderMenuView(this, cityName + "地铁线路图");

        imageView = (GestureImageView) findViewById(R.id.imageView);

        ImageLoadUtil.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(loadedImage);
            }
        });
    }
}
