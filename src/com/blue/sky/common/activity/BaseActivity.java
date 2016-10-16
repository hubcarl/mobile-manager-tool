package com.blue.sky.common.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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
public class BaseActivity extends Activity{

    public InputMethodManager manager; // 软键盘管理，用于点击空白处隐藏软键盘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 点击空白出隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    public void hideSoftInput() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); // 强制隐藏键盘
        }
    }


    private Toast toast = null;

    /**
     * 默认时间LENGTH_LONG
     *
     * @param msg
     */
    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 不会一直重复重复重复重复的提醒了
     *
     * @param msg
     * @param length
     *            显示时间
     */
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    protected void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected int getScreenMode() {
        return getResources().getConfiguration().orientation;
    }

    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }

    protected void startActivity(Class<?> clazz, String key, Serializable value) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    protected void startActivity(Class<?> clazz, String key, String value) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    protected void startActivity(Class<?> clazz, String key, int value) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    protected void startActivity(Class<?> clazz,Map<String,String> params) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        for (Map.Entry<String, String> entry: params.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(intent);
    }

    protected void setImageView(int id, String imgUrl, ImageLoadUtil.ImageStyle imageStyle){
        ImageView image = ((ImageView) findViewById(id));
        ImageLoadUtil.loadImage(image, imgUrl, imageStyle);
    }

    protected void setImageViewByResId(int id, String imgUrl, int resId){
        ImageView image = ((ImageView) findViewById(id));
        ImageLoadUtil.loadImageByResId(image, imgUrl, resId);
    }

    protected void setTextView(int id, String value){
        ((TextView)findViewById(id)).setText(value);
    }

    protected  void setTitle(String name)
    {
        ((TextView)findViewById(R.id.commonTitle)).setText(name);
    }


    protected void setHeader(String title, boolean isBack)
    {
        setTitle(title);
        if(isBack){
            TextView leftButton = ((TextView)findViewById(R.id.leftButton));
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected  void setHeader(String title, boolean isBack, String rightText, View.OnClickListener rightListener){
        setHeader(title,isBack);
        if (Strings.isNotEmpty(rightText)) {
            findViewById(R.id.rightButton).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.rightButton)).setText(rightText);
            if(rightListener!=null){
                findViewById(R.id.rightButton).setOnClickListener(rightListener);
            }
        }
        else
        {
            findViewById(R.id.rightButton).setVisibility(View.GONE);
        }
    }

    protected  void setHeader(String title, boolean isBack, String rightText, int resId, View.OnClickListener rightListener){
        setHeader(title,isBack);
        TextView rightButton = (TextView)findViewById(R.id.rightButton);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setText(rightText);
        if(rightListener!=null){
            findViewById(R.id.rightButton).setOnClickListener(rightListener);
        }
        if(resId>0){
            rightButton.setBackgroundResource(resId);
        }
    }

    protected View initUIView(int id){
       return  findViewById(id);
    }

    protected TextView initTextView(int id){
        return (TextView)findViewById(id);
    }

    protected ImageView initImageView(int id){
        return (ImageView)findViewById(id);
    }

    protected void setIntCache(String key, int value){
        SharedPreferenceUtil.getInstance(this).putInt(key, value);
    }

    protected void getIntCache(String key){
        SharedPreferenceUtil.getInstance(this).getInt(key);
    }

    protected void setStringCache(String key, String value){
        SharedPreferenceUtil.getInstance(this).putString(key, value);
    }

    // 友盟统计
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName()); //统计页面
        MobclickAgent.onPause(this);//统计时长
    }

}
