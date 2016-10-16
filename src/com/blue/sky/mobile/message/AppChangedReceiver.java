package com.blue.sky.mobile.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppChangedReceiver extends BroadcastReceiver {

    private MessageCallback messageCallback;

    public AppChangedReceiver(MessageCallback messageCallback)
    {
        this.messageCallback = messageCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent){
        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            Log.i(">>>AppChangedReceiver","安装了:" +packageName + "包名的程序");
            intent.putExtra("appStatus", 1);
            intent.putExtra("packageName", packageName);
            messageCallback.onReceiveMessage(context,intent);
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            Log.i(">>>AppChangedReceiver","卸载了:" + packageName + "包名的程序");
            intent.putExtra("appStatus", 0);
            intent.putExtra("packageName", packageName);
            messageCallback.onReceiveMessage(context,intent);
        }
    }
}