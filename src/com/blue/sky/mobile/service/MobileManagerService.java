package com.blue.sky.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.mobile.manager.file.common.FileDBHelper;

import java.io.File;

/**
 * Created by sky on 2014/12/27.
 */
public class MobileManagerService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(">>>MobileManagerService:", "onCreate");
        startScanFile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

        @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startScanFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long lastScanTime = SharedPreferenceUtil.getInstance(getApplicationContext()).getLong("lastScanTime");
                Log.d(">>>lastScanTime", lastScanTime + "");
                if (lastScanTime == 0 || System.currentTimeMillis() - lastScanTime > 24 * 3600 * 1000) {
                    scanFile(Environment.getExternalStorageDirectory());
                    SharedPreferenceUtil.getInstance(getApplicationContext()).putLong("lastScanTime",System.currentTimeMillis());
                }
            }
        }).start();
    }

    private void scanFile(File file){
        File files[] = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    scanFile(f);
                }
                FileDBHelper.addFile(getApplicationContext(), f);
            }
        }
    }
}
