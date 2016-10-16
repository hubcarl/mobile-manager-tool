package com.blue.sky.mobile.manager.assistant.metro;

import android.os.Bundle;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;

/**
 * Created by Administrator on 2014/11/16.
 */
public class MetroSiteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_activity_main);
        UIHelp.setHeaderMenuView(this, "地铁线路");
    }
}
