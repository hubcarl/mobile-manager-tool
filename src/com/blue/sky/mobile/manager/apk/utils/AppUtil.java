package com.blue.sky.mobile.manager.apk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.blue.sky.mobile.manager.apk.model.AppInfo;

import java.io.File;

/**
 * Created by Administrator on 2014/10/19.
 */
public class AppUtil {

    /*
    * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
    * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
    * http://code.google.com/p/android/issues/detail?id=9151
    */
    public static AppInfo getApkInfo(Context context, String apkPath) {
        AppInfo apkInfo = null;
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                appInfo.sourceDir = apkPath;
                appInfo.publicSourceDir = apkPath;

                apkInfo = new AppInfo();
                apkInfo.setAppName(pm.getApplicationLabel(appInfo).toString());
                apkInfo.setFilePath(apkPath);
                apkInfo.setPackageName(appInfo.packageName);
                apkInfo.setVersionCode(info.versionCode);
                apkInfo.setVersionName(info.versionName);
                apkInfo.setSize(FileUtil.format(new File(apkPath).length()));
                apkInfo.setIcon(appInfo.loadIcon(pm));
            }
        }
        return apkInfo;
    }

    public static AppInfo getAppInfoByPackageName(Context context, String packageName){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        AppInfo tmpInfo = new AppInfo();
        tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
        tmpInfo.setPackageName(packageInfo.packageName);
        tmpInfo.setVersionCode(packageInfo.versionCode);
        tmpInfo.setVersionName(packageInfo.versionName);
        tmpInfo.setSize(FileUtil.format(new File(packageInfo.applicationInfo.publicSourceDir).length()));
        tmpInfo.setIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
        return tmpInfo;
    }

    public static boolean isAppInstalled(Context context, AppInfo apkInfo) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(apkInfo.getPackageName(), PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static boolean isAppUpgrade(Context context, AppInfo apkInfo) {
        PackageManager pm = context.getPackageManager();
        boolean isUpgrade = false;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(apkInfo.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(apkInfo.getVersionCode()>packageInfo.versionCode){
                isUpgrade = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            isUpgrade = false;
        }
        return isUpgrade;
    }
}
