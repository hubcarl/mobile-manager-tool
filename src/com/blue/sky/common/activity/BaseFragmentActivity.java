package com.blue.sky.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.mobile.manager.R;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/13.
 */
public class BaseFragmentActivity extends FragmentActivity {

    // 友盟统计
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);//统计时长
    }

}
