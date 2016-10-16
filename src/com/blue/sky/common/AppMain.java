package com.blue.sky.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.blue.sky.common.component.util.SystemUtils;
import com.blue.sky.common.component.util.TimeUtils;
import com.blue.sky.common.sdk.SDK;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.control.download.providers.downloads.DownloadService;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.center.user.UserInfo;
import com.blue.sky.mobile.manager.center.user.UserSetting;
import com.blue.sky.mobile.manager.music.module.network.utils.MusicPlayController;
import com.umeng.analytics.MobclickAgent;
import io.vov.vitamio.Vitamio;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppMain extends Application {

    public static final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileManager/log/";
    public static final String NAME = getCurrentDateString() + ".txt";


    public static MusicPlayController MUSIC = new MusicPlayController();

    private static AppMain mInstance;

    public static AppMain getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //捕捉系统崩溃日志
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

        startService(new Intent(this, DownloadService.class));
        //startService(new Intent(this, MobileManagerService.class));
        registerSDK();

        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void registerSDK() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageLoadUtil.configuration(getApplicationContext());
                } catch (Exception ex) {
                    Log.e(">>>AppMain ImageLoader", ex.toString());
                }

                try {
                    SDK.init56SDK(getApplicationContext());
                } catch (Exception ex) {
                    Log.e(">>>AppMain 56SDK", ex.toString());
                }

//                try{
//                    SDK.initWDJSDK(getApplicationContext());
//                }catch (Exception ex){
//                    Log.e(">>>AppMain WDJSDK", ex.toString());
//                }

                try {
                    SDK.registerVideoComponent(getApplicationContext());
                } catch (Exception ex) {
                    Log.e(">>>AppMain Video", ex.toString());
                }
            }
        }).start();
    }



    /**
     * 捕获错误信息的handler
     */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            String errorMsg = "-----------------------------" + SystemUtils.getDeviceDetailInfo()
                    + "-----------------------------" +getThrowableInfo(ex);
            MobclickAgent.reportError(getApplicationContext(), errorMsg);
            writeCrashLog(errorMsg);
        }
    };

    private boolean writeCrashLog(String errorMsg) {
        boolean isDealing = false;
        Log.e(">>>AppMain uncaughtException", errorMsg);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            RandomAccessFile randomAccessFile = null;
            try {
                String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MobileManager" + File.separator + "logs" + File.separator;
                File file = new File(fileName);
                if (!file.exists())
                    file.mkdirs();
                randomAccessFile = new RandomAccessFile(fileName + TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATE_FORMAT_DATE) + ".log", "rw");
                long fileLength = randomAccessFile.length();
                randomAccessFile.seek(fileLength);
                randomAccessFile.writeBytes(errorMsg);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                        isDealing = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return isDealing;
    }

    private static String getThrowableInfo(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return TimeUtils.getTime(System.currentTimeMillis()) + ":" + stringWriter.toString();
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    private static String getCurrentDateString() {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date nowDate = new Date();
        result = sdf.format(nowDate);
        return result;
    }


    public static class Cookies {

        private static UserInfo userInfo;

        public static UserInfo getUserInfo() {
            if (userInfo == null) {
                userInfo = new UserInfo(AppMain.getInstance());
            }
            return userInfo;
        }

        private static UserSetting userSetting;

        public static UserSetting getUserSetting() {
            if (userSetting == null) {
                userSetting = new UserSetting(AppMain.getInstance());
            }
            return userSetting;
        }


        public static boolean isLogin() {
            return Strings.isNotEmpty(getUserInfo().getUserId());
        }

        /**
         * 清空用户登录信息
         */
        public static void clear() {
            if (userInfo != null) {
                userInfo = null;
            }
            AppMain.getInstance().getSharedPreferences(UserInfo.CODE_STUDY_USER, Context.MODE_PRIVATE).edit().clear().commit();
        }
    }
}
