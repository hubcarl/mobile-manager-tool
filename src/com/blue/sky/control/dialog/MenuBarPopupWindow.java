package com.blue.sky.control.dialog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.qrcode.CaptureActivity;

public class MenuBarPopupWindow extends PopupWindow implements OnClickListener{

    private Context context;
    private View mMenuView;

    public MenuBarPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.menu_bar_pop, null);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        //设置按钮监听
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(2*w/5);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.menu_bar_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout2).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

        mMenuView.findViewById(R.id.liner_qrcode).setOnClickListener(MenuBarPopupWindow.this);
        mMenuView.findViewById(R.id.liner_share).setOnClickListener(MenuBarPopupWindow.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.liner_qrcode:
                Intent qrIntent = new Intent(context, CaptureActivity.class);
                context.startActivity(qrIntent);
                MenuBarPopupWindow.this.dismiss();
                break;
            case R.id.liner_share:
                // 启动分享发送到属性
                Intent intent = new Intent(Intent.ACTION_SEND);
                // 分享发送到数据类型
                intent.setType("text/plain");
                // 分享的主题
                intent.putExtra(Intent.EXTRA_SUBJECT, "手机宝－－手机文件管理和生活助手");
                // 分享的内容
                intent.putExtra(Intent.EXTRA_TEXT, "手机宝是一款集图片管理、音乐播放、视频播放、安装包管理、文件管理等功能的手机管理工具," +
                        "同时也是一款包括公交地铁查询、房贷计算器等小工具的生活助手！赶快下载体验吧! http://appshow.sinaapp.com" );
                // 允许intent启动新的activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //目标应用选择对话框的标题
                context.startActivity(Intent.createChooser(intent, "手机宝"));
                MenuBarPopupWindow.this.dismiss();
                break;
        }
    }
}
